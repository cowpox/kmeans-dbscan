package br.uel.ce.clustering.dbscan;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.List;
import java.util.concurrent.ExecutorService;
/**
 * DBSCAN paralelo desenvolvido por Sofia.
 *
 * Esta versão procura encontrar os mesmos agrupamentos do DBSCAN clássico,
 * mas divide parte do trabalho entre várias threads. Em vez de expandir
 * cada cluster por uma fila de pontos, identifica primeiro os pontos de núcleo
 * e depois usa Union-Find para descobrir quais deles estão conectados.
 *
 * O processamento acontece em três momentos: identificar os pontos de núcleo,
 * unir os núcleos vizinhos e, por fim, atribuir rótulos aos pontos de borda
 * e aos pontos de ruído.
 *
 * A busca de vizinhos ainda é feita por força bruta. As uniões usam um
 * método synchronized, ou seja, apenas uma thread pode alterar os grupos
 * por vez. Isso facilita a sincronização, mas pode limitar o desempenho.
 *
 * Convenção de rótulos: -2 representa ruído; 0, 1, 2, ... representam
 * os clusters encontrados.
 */
public final class DBSCANClustering implements ClusteringAlgorithm {

    /**
     * Executa o agrupamento com o número de threads solicitado.
     * O executor é fornecido pela aplicação e distribui as tarefas paralelas.
     *
     * @param dataset pontos que serão agrupados
     * @param params parâmetros eps e minPts do DBSCAN
     * @param executor executor responsável pelas tarefas
     * @param numThreads quantidade de threads solicitada
     * @return rótulos de cada ponto, quantidade de clusters e de pontos de ruído
     */
    @Override
    public ClusteringResult run(List<DataPoint> dataset, AlgorithmParams params,
                                ExecutorService executor, int numThreads) throws Exception {

        // Lemos os dois parâmetros do DBSCAN e verificamos se são válidos.
        // eps define o raio da vizinhança; minPts define a densidade mínima.
        double eps = params.getDouble("eps", -1);
        int minPts = params.getInt("minPts", -1);
        if (!Double.isFinite(eps) || eps <= 0 || minPts <= 0) {
            throw new IllegalArgumentException("eps e minPts devem ser positivos");
        }

        // Um conjunto vazio não possui clusters nem pontos de ruído.
        int n = dataset.size();
        if (n == 0) {
            return new ClusteringResult(new int[0], 0, 0);
        }

        // Dividimos os pontos em blocos para distribuir o trabalho.
        // Não faz sentido criar mais tarefas do que a quantidade de pontos.
        int threads = Math.max(1, Math.min(numThreads, n));
        int bloco = (n + threads - 1) / threads;
        // Trabalhar com a distância ao quadrado evita calcular raízes
        // quadradas em cada comparação de pontos.
        double eps2 = eps * eps;
        // Guardamos quais pontos são de núcleo e as tarefas em execução.
        // Cada thread irá escrever apenas nas posições de sua própria faixa.
        boolean[] core = new boolean[n];
        List<Future<?>> tarefas = new ArrayList<>();

        // FASE 1 — Identificação dos pontos de núcleo.
        // Cada tarefa examina um bloco, mas procura vizinhos no dataset inteiro.
        for (int t = 0; t < threads; t++) {
            int inicio = t * bloco;
            int fim = Math.min(inicio + bloco, n);
            if (inicio >= fim) break;

            tarefas.add(executor.submit(() -> {
                for (int i = inicio; i < fim; i++) {
                    // O próprio ponto participa da contagem de vizinhos,
                    // conforme a definição de vizinhança adotada pelo DBSCAN.
                    int vizinhos = 0;

                    for (int j = 0; j < n; j++) {
                        if (distancia2(dataset.get(i), dataset.get(j)) <= eps2) {
                            vizinhos++;

                            // Assim que atingimos minPts, já sabemos que o
                            // ponto é núcleo e podemos encerrar esta busca.
                            if (vizinhos >= minPts) break;
                        }
                    }
                    core[i] = vizinhos >= minPts;
                }
            }));
        }

        // Só passamos à próxima fase quando todas as threads terminam.
        // get() também propaga eventuais erros das tarefas.
        for (Future<?> tarefa : tarefas) {
            tarefa.get();
        }

        // FASE 2 — União dos pontos de núcleo que são vizinhos.
        // Inicialmente cada ponto forma seu próprio conjunto no Union-Find.
        UnionFind grupos = new UnionFind(n);
        tarefas.clear();

        for (int t = 0; t < threads; t++) {
            int inicio = t * bloco;
            int fim = Math.min(inicio + bloco, n);

            if (inicio >= fim) break;

            tarefas.add(executor.submit(() -> {
                for (int i = inicio; i < fim; i++) {
                    // Apenas pontos de núcleo podem conectar regiões densas.
                    if (!core[i]) continue;

                    // Verificamos apenas j > i para não comparar duas vezes
                    // o mesmo par de pontos de núcleo.
                    for (int j = i + 1; j < n; j++) {
                        if (core[j] &&
                                distancia2(dataset.get(i), dataset.get(j)) <= eps2) {
                            // Os dois núcleos estão próximos: passam a fazer
                            // parte do mesmo componente, isto é, do mesmo cluster.
                            grupos.unir(i, j);
                        }
                    }
                }
            }));
        }
        for (Future<?> tarefa : tarefas) {
            tarefa.get();
        }

        // FASE 3 — Transformação dos componentes em rótulos de clusters.
        // Começamos marcando todos os pontos como ruído (-2); depois
        // substituímos esse valor nos pontos que pertencem a clusters.
        int[] labels = new int[n];
        Arrays.fill(labels, -2);

        // A raiz do Union-Find identifica um componente, mas pode ser
        // qualquer índice. Este mapa atribui IDs consecutivos aos clusters.
        Map<Integer, Integer> ids = new HashMap<>();
        int numClusters = 0;

        for (int i = 0; i < n; i++) {
            if (!core[i]) continue;

            // Pontos de núcleo com a mesma raiz pertencem ao mesmo grupo.
            int raiz = grupos.achar(i);

            if (!ids.containsKey(raiz)) {
                ids.put(raiz, numClusters++);
            }

            labels[i] = ids.get(raiz);
        }

        // Agora tratamos os pontos que não são de núcleo.
        // Um ponto próximo de algum núcleo é de borda; caso contrário,
        // permanece marcado como ruído.
        int ruido = 0;

        for (int i = 0; i < n; i++) {
            if (core[i]) continue;

            for (int j = 0; j < n; j++) {
                if (core[j] &&
                        distancia2(dataset.get(i), dataset.get(j)) <= eps2) {
                    // Encontramos um núcleo vizinho e usamos o rótulo dele.
                    // Se houver vários clusters possíveis, vence o primeiro
                    // núcleo encontrado na ordem do dataset.
                    labels[i] = labels[j];
                    break;
                }
            }

            if (labels[i] == -2) {
                ruido++;
            }
        }

        // Devolvemos a classificação final e os totais encontrados.
        return new ClusteringResult(labels, numClusters, ruido);
    }

    /**
     * Calcula a distância euclidiana ao quadrado entre dois pontos.
     * A distância só pode ser calculada se ambos tiverem a mesma dimensão.
     *
     * @param a primeiro ponto
     * @param b segundo ponto
     * @return distância euclidiana ao quadrado
     */
    private static double distancia2(DataPoint a, DataPoint b) {
        double[] x = a.getFeatures();
        double[] y = b.getFeatures();

        if (x.length != y.length) {
            throw new IllegalArgumentException("Dimensoes diferentes");
        }
        double soma = 0;

        for (int d = 0; d < x.length; d++) {
            double diferenca = x[d] - y[d];
            soma += diferenca * diferenca;
        }

        return soma;
    }

    /**
     * Estrutura que mantém conjuntos de pontos conectados.
     *
     * Cada posição aponta para um pai. Ao seguir esses pais até a raiz,
     * descobrimos a qual conjunto um ponto pertence. Unir dois conjuntos
     * significa fazer uma das raízes apontar para a outra.
     *
     * Nesta versão, as uniões são sincronizadas no objeto inteiro.
     * Isso evita que duas uniões alterem os pais ao mesmo tempo, porém
     * serializa essa parte do processamento.
     */
    private static class UnionFind {
        private final int[] pai;

        /** Cria um conjunto independente para cada ponto. */
        UnionFind(int n) {
            pai = new int[n];

            for (int i = 0; i < n; i++) {
                pai[i] = i;
            }
        }

        /**
         * Encontra a raiz do conjunto de um ponto.
         * Durante a busca, encurta o caminho até a raiz para acelerar
         * consultas posteriores (compressão de caminhos).
         */
        int achar(int x) {
            if (pai[x] != x) {
                pai[x] = achar(pai[x]);
            }
            return pai[x];
        }
        /**
         * Junta dois conjuntos quando ainda possuem raízes diferentes.
         * O synchronized permite que apenas uma thread execute esta
         * operação de cada vez nesta instância de Union-Find.
         */
        synchronized void unir(int a, int b) {
            int raizA = achar(a);
            int raizB = achar(b);
            if (raizA != raizB) {
                pai[raizB] = raizA;
            }
        }
    }

    /** Identificador utilizado pela aplicação para selecionar o algoritmo. */
    @Override
    public String getName() {
        return "dbscan";
    }
}
