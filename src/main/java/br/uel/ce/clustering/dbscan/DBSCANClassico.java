
package br.uel.ce.clustering.dbscan;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Implementação clássica e sequencial do DBSCAN, baseada em Ester et al. (1996).
 *
 * Responsáveis: Adriano e Sofia.
 *
 * Esta é nossa versão de referência para comparar os resultados e o desempenho
 * das implementações paralelas. O algoritmo examina cada ponto e, quando encontra
 * uma região suficientemente densa, expande o grupo até não haver mais pontos
 * alcançáveis a partir dos seus núcleos.
 *
 * A busca de vizinhos percorre diretamente o dataset, sem índices espaciais.
 * Assim, o tempo no pior caso é O(n² * d), sendo n o número de pontos e d o
 * número de características. A memória auxiliar é O(n), desconsiderando a entrada.
 *
 * Rótulos utilizados:
 *  -1: ponto ainda não visitado (apenas durante a execução);
 *  -2: ruído, isto é, ponto não associado a nenhum cluster;
 *   0, 1, 2...: identificadores dos clusters encontrados.
 */
public final class DBSCANClassico implements ClusteringAlgorithm {

    // Esses valores distinguem os pontos ainda não examinados daqueles
    // que já foram considerados ruído.
    private static final int UNVISITED = -1;
    private static final int NOISE = -2;

    /**
     * Executa o DBSCAN do começo ao fim em uma única thread.
     *
     * Os argumentos executor e numThreads fazem parte da interface comum do
     * projeto, mas não são usados aqui: esta classe representa o baseline
     * sequencial dos nossos experimentos.
     *
     * @param dataset pontos que serão agrupados
     * @param params parâmetros eps (raio) e minPts (densidade mínima)
     * @param executor não utilizado nesta implementação
     * @param numThreads não utilizado nesta implementação
     * @return rótulos, quantidade de clusters e quantidade de pontos de ruído
     */
    @Override
    public ClusteringResult run(
            List<DataPoint> dataset,
            AlgorithmParams params,
            ExecutorService executor,
            int numThreads) {

        // Lemos os dois parâmetros que definem a densidade mínima de um grupo.
        // eps é o raio da vizinhança; minPts é a quantidade mínima de pontos nela.
        double eps = params.getDouble("eps", -1);
        int minPts = params.getInt("minPts", -1);

        // Não faz sentido procurar vizinhanças com raio inválido ou exigir
        // uma quantidade não positiva de vizinhos.
        if (!Double.isFinite(eps) || eps <= 0 || minPts <= 0) {
            throw new IllegalArgumentException(
                "Parâmetros inválidos: eps deve ser positivo " +
                "e finito; minPts deve ser positivo."
            );
        }

        int n = dataset.size();

        // Inicialmente ninguém foi visitado e ainda não conhecemos nenhum cluster.
        // Cada posição de labels corresponde à mesma posição no dataset.
        int[] labels = new int[n];
        Arrays.fill(labels, UNVISITED);

        int clusterId = 0;
        // Trabalhamos com distâncias ao quadrado para evitar calcular raízes
        // quadradas em cada comparação entre dois pontos.
        double epsSquared = eps * eps;

        // Procuramos um ponto ainda não visitado para iniciar uma nova região.
        // Como esta é a versão clássica, a exploração acontece sequencialmente.
        for (int i = 0; i < n; i++) {

            // Um ponto que já pertence a um cluster não precisa iniciar outra
            // exploração. Pontos marcados como ruído também serão reconsiderados
            // somente se aparecerem depois na vizinhança de um núcleo.
            if (labels[i] != UNVISITED) {
                continue;
            }

            // Procuramos todos os pontos que estão a uma distância de até eps
            // do ponto atual, incluindo o próprio ponto.
            List<Integer> neighbors =
                regionQuery(dataset, i, epsSquared);

            // Se a vizinhança não atingir minPts, este ponto ainda não pode
            // iniciar um grupo. Ele fica marcado como ruído por enquanto,
            // mas poderá ser incorporado como ponto de borda mais adiante.
            if (neighbors.size() < minPts) {
                labels[i] = NOISE;
                continue;
            }

            // Encontramos um ponto de núcleo: há densidade suficiente para
            // começar um cluster e explorar a região em torno dele.
            labels[i] = clusterId;

            // A lista seeds funciona como uma fila de pontos a examinar.
            // Começamos com os vizinhos do núcleo que iniciou o cluster.
            List<Integer> seeds = new ArrayList<>(neighbors);

            // Um ponto pode aparecer na vizinhança de vários núcleos.
            // Este controle impede que ele entre repetidas vezes na fila.
            boolean[] inSeeds = new boolean[n];

            for (int neighbor : seeds) {
                inSeeds[neighbor] = true;
            }

            // Em vez de remover itens da lista, avançamos um índice sobre ela.
            // Novos vizinhos podem ser acrescentados enquanto exploramos o grupo.
            int k = 0;

            // Seguimos explorando até não restar nenhum ponto na fila.
            // É essa expansão que permite identificar clusters de formas variadas.
            while (k < seeds.size()) {

                int q = seeds.get(k);

                // Um ponto considerado ruído em uma visita anterior pode estar
                // dentro do raio de um núcleo. Nesse caso, ele deixa de ser
                // ruído e passa a fazer parte deste cluster como ponto de borda.
                if (labels[q] == NOISE) {
                    labels[q] = clusterId;
                }

                // Consultamos a vizinhança apenas de pontos ainda não visitados.
                // Isso evita expandir várias vezes a partir do mesmo ponto.
                if (labels[q] == UNVISITED) {

                    labels[q] = clusterId;

                    // Agora verificamos se o ponto q também possui densidade
                    // suficiente para continuar a expansão do cluster.
                    List<Integer> qNeighbors =
                        regionQuery(dataset, q, epsSquared);

                    // Só pontos de núcleo podem acrescentar novos candidatos
                    // à fila. Pontos de borda pertencem ao grupo, mas não o expandem.
                    if (qNeighbors.size() >= minPts) {

                        for (int neighbor : qNeighbors) {

                            // Acrescentamos apenas os vizinhos que ainda não
                            // estavam na fila de exploração deste cluster.
                            if (!inSeeds[neighbor]) {
                                seeds.add(neighbor);
                                inSeeds[neighbor] = true;
                            }
                        }
                    }
                }

                // Passamos ao próximo candidato. A fila pode ter crescido
                // durante a iteração, portanto o laço continua até seu fim.
                k++;
            }

            // Encerramos a expansão deste cluster. O próximo núcleo ainda
            // não visitado, se existir, dará origem a outro identificador.
            clusterId++;
        }

        // Depois de explorar todos os grupos, contamos os pontos que
        // permaneceram sem cluster. Mantemos o rótulo -2 no resultado.
        int numNoisePoints = 0;

        for (int label : labels) {
            if (label == NOISE) {
                numNoisePoints++;
            }
        }

        // Devolvemos os rótulos na ordem original do dataset, além das
        // quantidades necessárias para avaliar o agrupamento.
        return new ClusteringResult(
            labels,
            clusterId,
            numNoisePoints
        );
    }

    /**
     * Encontra a vizinhança de um ponto por busca direta.
     *
     * Percorremos o dataset inteiro e guardamos os índices dos pontos que
     * estão dentro do raio eps. O próprio ponto também entra nessa contagem,
     * como prevê a definição de vizinhança adotada no DBSCAN.
     *
     * @param dataset conjunto completo de pontos
     * @param i índice do ponto cuja vizinhança queremos conhecer
     * @param epsSquared raio eps elevado ao quadrado
     * @return índices dos pontos encontrados na vizinhança
     */
    private static List<Integer> regionQuery(
            List<DataPoint> dataset,
            int i,
            double epsSquared) {

        // Guardamos índices, e não cópias dos pontos, para reutilizar o dataset.
        List<Integer> neighbors = new ArrayList<>();

        DataPoint p = dataset.get(i);

        for (int j = 0; j < dataset.size(); j++) {

            DataPoint q = dataset.get(j);

            // A comparação inclui os pontos exatamente na fronteira do raio.
            if (squaredDistance(p, q) <= epsSquared) {
                neighbors.add(j);
            }
        }

        return neighbors;
    }

    /**
     * Calcula a distância euclidiana ao quadrado entre dois pontos.
     *
     * Em cada dimensão, calculamos a diferença entre as características e
     * somamos seu quadrado. Como comparamos o resultado com eps², não é
     * necessário calcular a raiz quadrada.
     *
     * @param a primeiro ponto
     * @param b segundo ponto
     * @return soma dos quadrados das diferenças entre as características
     */
    private static double squaredDistance(
            DataPoint a,
            DataPoint b) {

        double[] x = a.getFeatures();
        double[] y = b.getFeatures();

        // A distância só pode ser calculada entre vetores de mesma dimensão.
        if (x.length != y.length) {
            throw new IllegalArgumentException(
                "Pontos com dimensionalidades diferentes."
            );
        }

        double sum = 0.0;

        // Somamos a contribuição de cada característica para a distância.
        for (int d = 0; d < x.length; d++) {
            double diff = x[d] - y[d];
            sum += diff * diff;
        }

        return sum;
    }

    // Nome usado pelo restante do projeto para identificar esta implementação.
    @Override
    public String getName() {
        return "dbscan-classico";
    }
}
