package br.uel.ce.clustering.dbscan;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * DBSCAN paralelo para computadores com memória compartilhada.
 *
 * Inspirado no PDSDBSCAN-S de Patwary et al. (2012), este algoritmo
 * identifica os pontos de núcleo (core) e usa Union-Find para reunir
 * os pontos que fazem parte de um mesmo agrupamento.
 *
 * O trabalho é dividido em quatro etapas:
 * 1. Identificar, em paralelo, quais pontos são de núcleo.
 * 2. Unir os núcleos vizinhos dentro de cada partição, sem disputas
 *    entre threads, e guardar as ligações que atravessam partições.
 * 3. Processar essas ligações em paralelo, com operações atômicas,
 *    formando os componentes finais sem bloquear toda a estrutura.
 * 4. Atribuir identificadores aos clusters e classificar os demais
 *    pontos como borda ou ruído.
 *
 * A busca de vizinhos ainda é por força bruta: no pior caso,
 * seu custo é O(n² × d), sendo d o número de dimensões.
 * Os rótulos seguem a convenção do projeto: -2 para ruído
 * e 0, 1, 2... para os clusters identificados.
 *
 * Responsáveis pelo projeto: Adriano e Sofia.
 */
public final class DBSCANClusteringS implements ClusteringAlgorithm {

    private static final int NOISE = -2;

    @Override
    public ClusteringResult run(List<DataPoint> dataset,
                                AlgorithmParams params,
                                ExecutorService executor,
                                int numThreads) throws Exception {
        // Começamos lendo os dois parâmetros do DBSCAN. O raio eps define
        // a vizinhança; minPts indica quantos pontos, incluindo o próprio,
        // são necessários para que um ponto seja considerado de núcleo.
        double eps = params.getDouble("eps", -1);
        int minPts = params.getInt("minPts", -1);
        if (!Double.isFinite(eps) || eps <= 0 || minPts <= 0) {
            throw new IllegalArgumentException("eps deve ser positivo e finito; minPts deve ser positivo");
        }
        // Trabalhamos com distâncias ao quadrado para evitar calcular
        // raiz quadrada em cada comparação entre dois pontos.
        double eps2 = eps * eps;
        if (!Double.isFinite(eps2)) {
            throw new IllegalArgumentException("eps e muito grande para a distancia ao quadrado");
        }

        int n = dataset.size();
        if (n == 0) return new ClusteringResult(new int[0], 0, 0);

        // Não faz sentido criar mais partições do que pontos. Também
        // garantimos pelo menos uma thread para o caso sequencial.
        final int threads = Math.max(1, Math.min(numThreads, n));
        if (threads > 1 && executor == null) {
            throw new IllegalArgumentException("ExecutorService e obrigatorio para execucao paralela");
        }
        // Antes de paralelizar, reunimos as referências aos vetores de
        // coordenadas em um array de acesso direto. Isso evita depender
        // de chamadas concorrentes aos métodos da lista e dos objetos.
        // ATENÇÃO: não copiamos os vetores internos. Por isso, ninguém
        // deve modificar suas coordenadas enquanto o algoritmo executa.
        // Também conferimos as dimensões e rejeitamos valores inválidos.
        double[][] points = new double[n][];
        int dimensions = -1;
        for (int i = 0; i < n; i++) {
            double[] features = dataset.get(i).getFeatures();
            if (features == null) throw new IllegalArgumentException("Features nulas no ponto " + i);
            if (dimensions < 0) dimensions = features.length;
            if (features.length != dimensions) {
                throw new IllegalArgumentException("Dimensoes diferentes no ponto " + i);
            }
            for (double value : features) {
                if (!Double.isFinite(value)) {
                    throw new IllegalArgumentException("Coordenada nao finita no ponto " + i);
                }
            }
            points[i] = features;
        }

        // Dividimos os índices em faixas contíguas e equilibradas.
        // Cada thread fica responsável pelos pontos de [starts[t], ends[t]),
        // isto é, inclui o início e exclui o fim da sua faixa.
        final int[] starts = new int[threads];
        final int[] ends = new int[threads];
        for (int t = 0; t < threads; t++) {
            starts[t] = (int) ((long) t * n / threads);
            ends[t] = (int) ((long) (t + 1) * n / threads);
        }

        // FASE 1 — Identificação dos pontos de núcleo.
        // Cada thread percorre apenas sua faixa de pontos, mas procura
        // vizinhos no dataset inteiro. Assim, um ponto perto do limite
        // entre duas partições não perde vizinhos da outra partição.
        // Como cada posição de core[] tem um único responsável, não
        // precisamos de locks para preencher esse vetor.
        boolean[] core = new boolean[n];
        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            final int start = starts[t], end = ends[t];
            submit(executor, threads, futures, () -> {
                for (int i = start; i < end; i++) {
                    int neighbors = 0;
                    for (int j = 0; j < n; j++) {
                        // Quando encontramos vizinhos suficientes, já sabemos
                        // que i é core: não há necessidade de continuar.
                        if (withinEps(points[i], points[j], eps2) && ++neighbors >= minPts) {
                            core[i] = true;
                            break;
                        }
                    }
                }
            });
        }
        await(futures);

        // FASE 2 — Construção dos agrupamentos locais.
        // Começamos com cada ponto representando seu próprio conjunto.
        // Cada thread pode unir núcleos da própria partição sem competir
        // com as outras. Quando dois núcleos vizinhos pertencem a
        // partições diferentes, guardamos o par para a fase de merge.
        // A união local só altera pais que pertencem à partição atual.
        int[] parentLocal = new int[n];
        for (int i = 0; i < n; i++) parentLocal[i] = i;
        EdgeBuffer[] crossEdges = new EdgeBuffer[threads];
        futures.clear();
        for (int t = 0; t < threads; t++) {
            final int owner = t, start = starts[t], end = ends[t];
            submit(executor, threads, futures, () -> {
                EdgeBuffer edges = new EdgeBuffer();
                for (int i = start; i < end; i++) {
                    if (!core[i]) continue;
                    // j começa em i + 1 para não examinar o mesmo par
                    // de pontos duas vezes.
                    for (int j = i + 1; j < n; j++) {
                        if (!core[j] || !withinEps(points[i], points[j], eps2)) continue;
                        // Como j > i, basta comparar com o fim da faixa
                        // para saber se os dois pontos têm o mesmo dono.
                        if (j < end) {
                            unionLocal(parentLocal, i, j);
                        } else {
                            edges.add(i, j);
                        }
                    }
                }
                crossEdges[owner] = edges;
            });
        }
        await(futures);

        // FASE 3 — União dos agrupamentos entre partições.
        // Aproveitamos as uniões locais feitas na fase anterior e
        // processamos os pares que ficaram pendentes. Agora várias
        // threads podem modificar a mesma estrutura Union-Find.
        // Usamos CAS (compare-and-set): cada mudança de pai só acontece
        // se o valor observado ainda for o esperado.
        // Para evitar ciclos, sempre ligamos a raiz de índice menor
        // à raiz de índice maior. Não há synchronized global.
        AtomicIntegerArray parent = new AtomicIntegerArray(parentLocal);
        futures.clear();
        for (int t = 0; t < threads; t++) {
            final EdgeBuffer edges = crossEdges[t];
            submit(executor, threads, futures, () -> {
                for (int k = 0; k < edges.size; k++) {
                    long edge = edges.values[k];
                    int i = (int) (edge >>> 32);
                    int j = (int) edge;
                    unionConcurrent(parent, i, j);
                }
            });
        }
        await(futures);

        // FASE 4a — Identificação dos clusters.
        // Cada componente conexa de pontos core recebe um número de
        // cluster. Fazemos essa numeração em uma única thread para
        // produzir identificadores estáveis na ordem do dataset.
        // Por enquanto, todos os pontos começam marcados como ruído.
        int[] labels = new int[n];
        Arrays.fill(labels, NOISE);
        Map<Integer, Integer> clusterByRoot = new HashMap<>();
        int clusterCount = 0;
        for (int i = 0; i < n; i++) {
            if (!core[i]) continue;
            int root = findConcurrent(parent, i);
            Integer id = clusterByRoot.get(root);
            if (id == null) {
                id = clusterCount++;
                clusterByRoot.put(root, id);
            }
            labels[i] = id;
        }

        // FASE 4b — Classificação de bordas e ruído.
        // Um ponto que não é core passa a ser borda se estiver dentro
        // de eps de algum core. Se houver mais de uma possibilidade,
        // escolhemos o primeiro core na ordem do dataset.
        // Os pontos sem qualquer core vizinho continuam marcados como
        // ruído. Cada thread escreve apenas nos rótulos de sua faixa;
        // os rótulos dos cores já estão prontos e não são modificados.
        futures.clear();
        for (int t = 0; t < threads; t++) {
            final int start = starts[t], end = ends[t];
            submit(executor, threads, futures, () -> {
                for (int i = start; i < end; i++) {
                    if (core[i]) continue;
                    for (int j = 0; j < n; j++) {
                        if (core[j] && withinEps(points[i], points[j], eps2)) {
                            labels[i] = labels[j];
                            break;
                        }
                    }
                }
            });
        }
        await(futures);

        // Por fim, contamos os pontos que permaneceram sem cluster.
        int noiseCount = 0;
        for (int label : labels) if (label == NOISE) noiseCount++;
        return new ClusteringResult(labels, clusterCount, noiseCount);
    }

    /**
     * Encaminha uma tarefa para o executor quando há paralelismo.
     * Com uma única thread, executa a tarefa diretamente; assim,
     * podemos usar a mesma classe também como referência sequencial.
     */
    private static void submit(ExecutorService executor, int threads,
                               List<Future<?>> futures, Runnable task) {
        if (threads == 1) {
            task.run();
        } else {
            futures.add(executor.submit(task));
        }
    }

    /**
     * Espera todas as tarefas da etapa terminarem antes de passar
     * para a próxima. Se alguma falhar, a exceção é propagada.
     */
    private static void await(List<Future<?>> futures)
            throws InterruptedException, ExecutionException {
        for (Future<?> future : futures) future.get();
    }

    /**
     * Verifica se dois pontos estão a uma distância de até eps.
     * Acumulamos a distância euclidiana ao quadrado e interrompemos
     * o cálculo assim que ela ultrapassar o limite permitido.
     */
    private static boolean withinEps(double[] a, double[] b, double eps2) {
        double sum = 0.0;
        for (int d = 0; d < a.length; d++) {
            double diff = a[d] - b[d];
            sum += diff * diff;
            if (sum > eps2) return false;
        }
        return sum <= eps2;
    }

    /**
     * Encontra o representante de um conjunto local.
     * Durante o percurso, encurtamos os caminhos até a raiz para
     * acelerar as próximas consultas. Como os conjuntos desta etapa
     * pertencem a partições independentes, não precisamos de locks.
     */
    private static int findLocal(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    /**
     * Junta dois conjuntos da mesma partição, caso ainda estejam
     * separados. A raiz menor passa a apontar para a maior, mantendo
     * uma regra de direção que também será usada no merge paralelo.
     */
    private static void unionLocal(int[] parent, int a, int b) {
        int ra = findLocal(parent, a);
        int rb = findLocal(parent, b);
        if (ra == rb) return;
        if (ra < rb) parent[ra] = rb;
        else parent[rb] = ra;
    }

    /**
     * Procura a raiz de um conjunto compartilhado entre threads.
     * As leituras atômicas permitem acompanhar os pais mesmo enquanto
     * outras threads fazem uniões. Aqui não comprimimos os caminhos.
     */
    private static int findConcurrent(AtomicIntegerArray parent, int x) {
        int p;
        while ((p = parent.get(x)) != x) x = p;
        return x;
    }

    /**
     * Une dois conjuntos de maneira segura para acesso concorrente.
     * Se outra thread modificar a raiz antes da nossa tentativa,
     * o CAS falha e repetimos a busca com os valores atualizados.
     * A orientação crescente dos índices impede formar ciclos.
     */
    private static void unionConcurrent(AtomicIntegerArray parent, int a, int b) {
        while (true) {
            int ra = findConcurrent(parent, a);
            int rb = findConcurrent(parent, b);
            if (ra == rb) return;
            int lower = Math.min(ra, rb);
            int higher = Math.max(ra, rb);
            if (parent.compareAndSet(lower, lower, higher)) return;
            // Outra thread mudou a raiz primeiro. Tentamos novamente
            // com os representantes mais recentes.
        }
    }

    /**
     * Guarda os pares de pontos core ligados entre partições.
     * Para economizar memória e evitar milhares de objetos pequenos,
     * empacotamos os dois índices int em um único valor long.
     * O array cresce conforme a necessidade.
     */
    private static final class EdgeBuffer {
        private long[] values = new long[16];
        private int size;

        void add(int a, int b) {
            if (size == values.length) {
                int next = values.length + (values.length >>> 1) + 1;
                if (next <= values.length) {
                    throw new IllegalStateException("Quantidade excessiva de arestas externas");
                }
                values = Arrays.copyOf(values, next);
            }
            // Os 32 bits superiores guardam a; os inferiores, b.
            values[size++] = ((long) a << 32) | (b & 0xffffffffL);
        }
    }

    @Override
    public String getName() {
        return "dbscan-paralelo-s";
    }
}
