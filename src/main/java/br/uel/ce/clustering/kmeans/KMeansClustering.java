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


        }
    







        
    @Override
    public String getName(){
        return "kmeans";
    }

}
