package br.uel.ce.clustering.kmeans;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Implementação paralela do k-means.
 *
 * Responsáveis: Herik e Julia.
 *
 * REGRA DA DISCIPLINA: a implementação deste algoritmo — incluindo qualquer
 * função auxiliar que faça parte do método (cálculo de distância, atribuição
 * de pontos a centróides, recálculo de centróides, critério de parada etc.) —
 * NÃO pode usar IA generativa. Ver ../../../../../../docs/referencial_teorico.md
 * para as referências de k-means paralelo (Dhillon & Modha 2000; Zhao, Ma & He
 * 2009; Bahmani et al. 2012 — k-means||).
 *
 * Parâmetros esperados em AlgorithmParams (via --params na CLI):
 *   k        — número de clusters (obrigatório)
 *   maxIter  — número máximo de iterações (opcional)
 *   seed     — semente para inicialização dos centróides (opcional)
 */
public final class KMeansClustering implements ClusteringAlgorithm {

    @Override
    public ClusteringResult run(List<DataPoint> dataset, AlgorithmParams params,
                                 ExecutorService executor, int numThreads) {
        int k = params.getInt("k", -1);
        if (k <= 0) {
            throw new IllegalArgumentException("Parâmetro obrigatório ausente: k");
        }

        // TODO (Herik, Julia): implementar o k-means paralelo aqui.
        //  - Particionar o dataset entre as threads do ExecutorService.
        //  - Cada partição calcula, em paralelo, a atribuição de pontos ao
        //    centróide mais próximo e as somas parciais para o recálculo.
        //  - Reduzir (merge) os resultados parciais para obter os novos centróides.
        //  - Repetir até convergência ou atingir maxIter.
        throw new UnsupportedOperationException("k-means ainda não implementado");
    }

    @Override
    public String getName() {
        return "kmeans";
    }
}
