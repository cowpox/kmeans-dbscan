package br.uel.ce.clustering.kmeans;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * k-means paralelo por divisão dos dados.
 *
 * Baseado em Dhillon & Modha (2000) para a decomposição paralela, e em Arthur &
 * Vassilvitskii (2007) para o laço de Lloyd e a inicialização k-means++
 *
 * Responsáveis: Herik e Julia.
 *
 * Estratégia: os pontos são divididos em numThreads faixas seguidas. Em cada iteração,
 * cada faixa vira uma tarefa no ExecutorService, que atribui os seus pontos e acumula
 * as suas próprias somas. Depois as somas de todas as faixas são juntadas, sempre na
 * mesma ordem, e os centróides são recalculados.

 * Complexidade temporal por iteração: O(n . k . d / P + P . k · d), com P = numThreads
 *
 * Parâmetros: idem do KMeansClassico, k, maxIter, seed e tol.
 * 
 * Convenção de rótulos: 0, 1, ..., k-1 = clusters.
 */

public final class KMeansClustering implements ClusteringAlgorithm {

    @Override
    public ClusteringResult run(
            List<DataPoint> dataset,
            AlgorithmParams params,
            ExecutorService executor,
            int numThreads) throws InterruptedException, ExecutionException {

        int n = dataset.size();

        // k = número de clusters; om-1 dispara exception se o k faltar
        int k = params.getInt("k", -1);
        if (k <= 0 || k > n) {
            throw new IllegalArgumentException("K deve estar entre 1 e " + n);
        }
        if (numThreads < 1) {
            throw new IllegalArgumentException("numThreads deve ser >= 1");
        }

        // lim de iteracoes, seed e tol
        int maxIter = params.getInt("maxIter", 100);
        int seed = params.getInt("seed", 123);
        double tolerance = params.getDouble("tol", 1e-4);

        double[][] centroids = initCentroids(dataset, k, new Random(seed));
        int dimensions = centroids[0].length;

        // labels = idx do cluster do ponto i
        int[] labels = new int[n];

        // divisão dos dados em faixas, uma tarefa por faixa
        // divisão inteira com arredondamento para cima
        int chunkSize = (n + numThreads - 1) / numThreads;
        // pode haver menos faixas que threads; dado n pequeno
        int numChunks = (n + chunkSize - 1) / chunkSize;

        for (int iteration = 0; iteration < maxIter; iteration++){
            // cada faixa tem seus próprios acumuladores (soma e contagem por cluster), sem lock
            double[][] currentCentroids = centroids;
            double[][][] partialSums = new double[numChunks][k][dimensions];
            int[][] partialCounts = new int[numChunks][k];

            List<Future<?>> tasks = new ArrayList<>();
            for (int chunk = 0; chunk < numChunks; chunk++){
                // submete uma task por faixa ao executor
                int from = chunk * chunkSize;
                int to = Math.min(n, from + chunkSize);
                double[][] sums = partialSums[chunk];
                int[] counts = partialCounts[chunk];
                // cada task atribui os pontos ao centróide mais próximo
                tasks.add(executor.submit(
                        () -> assignRange(dataset, currentCentroids, labels, from, to, sums, counts)));
            }
            // espera todas as faixas terminarem antes de recalcular os centróides
            for (Future<?> task : tasks){
                task.get();
            }

            // Junta as somas das faixas na ordem
            double[][] sums = partialSums[0];
            int[] counts = partialCounts[0];
            for (int chunk = 1; chunk < numChunks; chunk++){
                for (int c = 0; c < k; c++){
                    counts[c] += partialCounts[chunk][c];
                    // soma por dimensão
                    for (int j = 0; j < dimensions; j++){
                        sums[c][j] += partialSums[chunk][c][j];
                    }
                }
            }

            // atualização: o novo centróide é a média dos pontos do cluster
            // cluster vazio mantém o centróide anterior
            double[][] newCentroids = new double[k][];
            double maxShift = 0;
                for (int c = 0; c < k; c++){
                    if (counts[c] == 0){
                        newCentroids[c] = centroids[c];
                    } else{
                        newCentroids[c] = new double[dimensions];
                        for (int j = 0; j < dimensions; j++){
                            newCentroids[c][j] = sums[c][j] / counts[c];
                        }
                    }
                    maxShift = Math.max(maxShift, squaredDistance(centroids[c], newCentroids[c]));        
                }
                centroids = newCentroids;

                // Convergiu
                if (tolerance > 0 && maxShift <= tolerance * tolerance){
                    break;
                }
            }
            return new ClusteringResult(labels, k, 0); // sem ruído

        }
        /**
        * Inicialização k-means++:
        * O primeiro centróide é um ponto escolhido ao acaso. Os demais também são
        * escolhidos ao acaso, mas quanto mais longe um ponto está dos centróides
        * já escolhidos, maior a chance de ele ser o próximo.
        */
        
        private static double[][] initCentroids(List<DataPoint> dataset, int k, Random random){
            int n = dataset.size();
            double[][] centroids = new double[k][];
            centroids[0] = dataset.get(random.nextInt(n)).getFeatures().clone();
            
            double[] minDistance = new double[n];
            Arrays.fill(minDistance, Double.MAX_VALUE);

            for (int c = 1; c < k; c++){
                // apenas o último centróide pode diminuir alguma distância
                double total = 0;
                for (int i = 0; i < n; i++){
                    double distance = squaredDistance(dataset.get(i).getFeatures(), centroids[c - 1]);
                    minDistance[i] = Math.min(minDistance[i], distance);
                    total += minDistance[i];
                }

                // sorteia um valor entre 0 e a soma total das distâncias
                double target = random.nextDouble() * total;
                int chosen = 0;
                // percorre os pontos descontando suas distâncias até o valor zerar;
                // pontos com distância maior ocupam uma fatia maior e são mais prováveis
                while (chosen < n - 1){
                    target -= minDistance[chosen];
                    if (target < 0){
                        break;
                    }
                    chosen++;
                }
                // copia as coordenadas do ponto escolhido como novo centróide
                centroids[c] = dataset.get(chosen).getFeatures().clone();
            }
            return centroids;
        }

        // Atribui cada ponto de [from, to) ao centróide mais próximo e acumula soma e contagem
        private static void assignRange(List<DataPoint> dataset, double[][] centroids, int[] labels,
                                        int from, int to, double[][] sums, int[] counts){
            for (int i = from; i < to; i++){
                double[] x = dataset.get(i).getFeatures();
                int best = 0;
                double bestDistance = Double.MAX_VALUE;
                for (int c = 0; c < centroids.length; c++){
                    double distance = squaredDistance(x, centroids[c]);
                    if (distance < bestDistance){
                        bestDistance = distance;
                        best = c;
                    }
                }
                labels[i] = best;
                counts[best]++;
                for (int j = 0; j < x.length; j++){
                    sums[best][j] += x[j];
                }
            }
        }

        // Calcula a distância euclidiana ao quadrado
        private static double squaredDistance(double[] a, double[] b){
            double sum = 0.0;
            for (int j = 0; j < a.length; j++){
                double diff = a[j] - b[j];
                sum += diff * diff;
            }
            return sum;
        }

    @Override
    public String getName(){
        return "kmeans";
    }

}