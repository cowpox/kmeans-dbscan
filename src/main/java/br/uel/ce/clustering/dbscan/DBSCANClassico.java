
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
 * DBSCAN clássico — implementação sequencial de referência.
 *
 * Baseado em Ester et al. (1996).
 *
 * Responsáveis: Adriano e Sofia.
 *
 * Complexidade temporal: O(n² * d)
 * Memória auxiliar: O(n), além dos dados de entrada.
 *
 * Não utiliza paralelismo ou índices espaciais.
 *
 * Convenção de rótulos:
 *   -1 = UNVISITED (não visitado, apenas estado interno)
 *   -2 = NOISE (ruído)
 *    0, 1, 2, ... = clusters
 */
public final class DBSCANClassico implements ClusteringAlgorithm {

    private static final int UNVISITED = -1;
    private static final int NOISE = -2;

    @Override
    public ClusteringResult run(
            List<DataPoint> dataset,
            AlgorithmParams params,
            ExecutorService executor,
            int numThreads) {

        double eps = params.getDouble("eps", -1);
        int minPts = params.getInt("minPts", -1);

        if (!Double.isFinite(eps) || eps <= 0 || minPts <= 0) {
            throw new IllegalArgumentException(
                "Parâmetros inválidos: eps deve ser positivo " +
                "e finito; minPts deve ser positivo."
            );
        }

        int n = dataset.size();

        int[] labels = new int[n];
        Arrays.fill(labels, UNVISITED);

        int clusterId = 0;
        double epsSquared = eps * eps;

        // Percorre todos os pontos na ordem do dataset.
        for (int i = 0; i < n; i++) {

            // Ignora pontos já classificados.
            if (labels[i] != UNVISITED) {
                continue;
            }

            // Busca a vizinhança do ponto atual.
            List<Integer> neighbors =
                regionQuery(dataset, i, epsSquared);

            // Não possui vizinhos suficientes para ser núcleo.
            if (neighbors.size() < minPts) {
                labels[i] = NOISE;
                continue;
            }

            // Encontrado um ponto de núcleo:
            // inicia um novo cluster.
            labels[i] = clusterId;

            // Fila de expansão do cluster.
            List<Integer> seeds = new ArrayList<>(neighbors);

            // Evita inserir o mesmo ponto repetidamente.
            boolean[] inSeeds = new boolean[n];

            for (int neighbor : seeds) {
                inSeeds[neighbor] = true;
            }

            int k = 0;

            // Expansão iterativa do cluster.
            while (k < seeds.size()) {

                int q = seeds.get(k);

                // Um ponto anteriormente considerado ruído
                // pode ser incorporado como ponto de borda.
                if (labels[q] == NOISE) {
                    labels[q] = clusterId;
                }

                // Processa apenas pontos não visitados.
                if (labels[q] == UNVISITED) {

                    labels[q] = clusterId;

                    // Busca a vizinhança do ponto q.
                    List<Integer> qNeighbors =
                        regionQuery(dataset, q, epsSquared);

                    // Apenas pontos de núcleo expandem o cluster.
                    if (qNeighbors.size() >= minPts) {

                        for (int neighbor : qNeighbors) {

                            if (!inSeeds[neighbor]) {
                                seeds.add(neighbor);
                                inSeeds[neighbor] = true;
                            }
                        }
                    }
                }

                k++;
            }

            // Próximo identificador de cluster.
            clusterId++;
        }

        // Conta os pontos classificados como ruído.
        // O rótulo NOISE (-2) é preservado no resultado.
        int numNoisePoints = 0;

        for (int label : labels) {
            if (label == NOISE) {
                numNoisePoints++;
            }
        }

        return new ClusteringResult(
            labels,
            clusterId,
            numNoisePoints
        );
    }

    /**
     * Retorna os índices de todos os pontos cuja distância
     * ao ponto i seja menor ou igual a eps.
     *
     * Inclui o próprio ponto i na vizinhança.
     */
    private static List<Integer> regionQuery(
            List<DataPoint> dataset,
            int i,
            double epsSquared) {

        List<Integer> neighbors = new ArrayList<>();

        DataPoint p = dataset.get(i);

        for (int j = 0; j < dataset.size(); j++) {

            DataPoint q = dataset.get(j);

            if (squaredDistance(p, q) <= epsSquared) {
                neighbors.add(j);
            }
        }

        return neighbors;
    }

    /**
     * Calcula a distância euclidiana ao quadrado.
     *
     * Evita calcular a raiz quadrada para cada comparação.
     */
    private static double squaredDistance(
            DataPoint a,
            DataPoint b) {

        double[] x = a.getFeatures();
        double[] y = b.getFeatures();

        if (x.length != y.length) {
            throw new IllegalArgumentException(
                "Pontos com dimensionalidades diferentes."
            );
        }

        double sum = 0.0;

        for (int d = 0; d < x.length; d++) {
            double diff = x[d] - y[d];
            sum += diff * diff;
        }

        return sum;
    }

    @Override
    public String getName() {
        return "dbscan-classico";
    }
}
