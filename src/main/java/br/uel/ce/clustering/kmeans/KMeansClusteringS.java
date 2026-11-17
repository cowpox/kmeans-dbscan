package br.uel.ce.clustering.kmeans;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * k-means paralelo com redução paralela das somas parciais.
 *
 * Variante do KMeansClustering. No artigo de Dhillon & Modha (2000), a redução das
 * somas parciais também é paralela (MPI_Allreduce, O(log P)). Em memória compartilhada
 * o análogo é repartir o trabalho de redução entre as threads, em vez de deixar uma só
 * juntar as P somas parciais.
 *
 * Responsáveis: Herik e Julia.
 *
 * Estratégia (por iteração, depois da inicialização k-means++ idêntica à do KMeansClustering):
 *   1. Atribuição (paralela): igual ao KMeansClustering. Uma tarefa por faixa de pontos,
 *      cada uma com somas e contagens só dela, sem lock.
 *   2. Contagens (sequencial): soma das contagens das faixas, O(P . k). É pequena perto
 *      das somas, que têm d vezes mais elementos.
 *   3. Redução e atualização (paralela): o vetor de somas é visto como k . d posições
 *      seguidas e dividido em fatias entre as threads. Cada tarefa, para as posições da
 *      sua fatia, soma o valor de todas as faixas, sempre na ordem 0..P-1, divide pela
 *      contagem e escreve o novo centróide. Fatias não se sobrepõem, então não há lock.
 *      Dividir em k . d, e não em k, mantém todas as threads ocupadas mesmo com k pequeno.
 *   4. Convergência (sequencial): maior deslocamento entre os centróides, O(k . d).
 *   A soma de cada posição segue a mesma ordem de faixas do KMeansClustering, então o
 *   resultado é idêntico ao dele para o mesmo numThreads.
 *
 * Complexidade temporal por iteração:
 *   O(n . k . d / P + P . k . d / P + P . k + k . d) = O(n . k . d / P + P . k + k . d),
 *   com P = numThreads. O termo P . k . d do KMeansClustering vira P . k . d / P.
 *
 * Parâmetros: idem do KMeansClassico, k, maxIter, seed e tol.
 *
 * Convenção de rótulos: 0, 1, ..., k-1 = clusters.
 */
public final class KMeansClusteringS implements ClusteringAlgorithm {

    @Override
    public ClusteringResult run(
            List<DataPoint> dataset,
            AlgorithmParams params,
            ExecutorService executor,
            int numThreads) throws InterruptedException, ExecutionException {

        int n = dataset.size();

        int k = params.getInt("k", -1);
        if (k <= 0 || k > n) {
            throw new IllegalArgumentException("K deve estar entre 1 e " + n);
        }
        if (numThreads < 1) {
            throw new IllegalArgumentException("numThreads deve ser >= 1");
        }

        int maxIter = params.getInt("maxIter", 100);
        int seed = params.getInt("seed", 123);
        double tolerance = params.getDouble("tol", 1e-4);

        double[][] centroids = KMeansClustering.initCentroids(dataset, k, new Random(seed));
        int dimensions = centroids[0].length;
        int[] labels = new int[n];

        // faixas de pontos para a atribuição
        int chunkSize = (n + numThreads - 1) / numThreads;
        int numChunks = (n + chunkSize - 1) / chunkSize;

        // faixas de posições do vetor de somas (k . d) para a redução
        int cells = k * dimensions;
        int reduceSize = (cells + numThreads - 1) / numThreads;
        int numReduce = (cells + reduceSize - 1) / reduceSize;

        for (int iteration = 0; iteration < maxIter; iteration++) {
            double[][] currentCentroids = centroids;
            double[][][] partialSums = new double[numChunks][k][dimensions];
            int[][] partialCounts = new int[numChunks][k];

            // 1. atribuição paralela
            List<Future<?>> tasks = new ArrayList<>();
            for (int chunk = 0; chunk < numChunks; chunk++) {
                int from = chunk * chunkSize;
                int to = Math.min(n, from + chunkSize);
                double[][] sums = partialSums[chunk];
                int[] counts = partialCounts[chunk];
                tasks.add(executor.submit(
                        () -> KMeansClustering.assignRange(dataset, currentCentroids, labels, from, to, sums, counts)));
            }
            for (Future<?> task : tasks) {
                task.get();
            }

            // 2. contagens totais por cluster
            int[] counts = new int[k];
            for (int chunk = 0; chunk < numChunks; chunk++) {
                for (int c = 0; c < k; c++) {
                    counts[c] += partialCounts[chunk][c];
                }
            }

            // 3. redução paralela + atualização: cada tarefa cuida de uma fatia de k . d posições
            double[][] newCentroids = new double[k][dimensions];
            tasks.clear();
            for (int r = 0; r < numReduce; r++) {
                int from = r * reduceSize;
                int to = Math.min(cells, from + reduceSize);
                tasks.add(executor.submit(() -> {
                    for (int cell = from; cell < to; cell++) {
                        int c = cell / dimensions;
                        int j = cell % dimensions;
                        if (counts[c] == 0) {
                            // cluster vazio mantém o centróide anterior
                            newCentroids[c][j] = currentCentroids[c][j];
                        } else {
                            // soma sempre na ordem das faixas, para o resultado ser determinístico
                            double sum = 0;
                            for (int chunk = 0; chunk < numChunks; chunk++) {
                                sum += partialSums[chunk][c][j];
                            }
                            newCentroids[c][j] = sum / counts[c];
                        }
                    }
                }));
            }
            for (Future<?> task : tasks) {
                task.get();
            }

            // 4. convergência
            double maxShift = 0;
            for (int c = 0; c < k; c++) {
                maxShift = Math.max(maxShift, KMeansClustering.squaredDistance(centroids[c], newCentroids[c]));
            }
            centroids = newCentroids;

            if (tolerance > 0 && maxShift <= tolerance * tolerance) {
                break;
            }
        }
        return new ClusteringResult(labels, k, 0); // sem ruído
    }

    @Override
    public String getName() {
        return "kmeans-paralelo-s";
    }
}
