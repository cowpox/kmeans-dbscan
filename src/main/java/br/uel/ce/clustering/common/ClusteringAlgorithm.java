package br.uel.ce.clustering.common;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Contrato comum que k-means e DBSCAN devem implementar, garantindo a mesma
 * interface de execução e o mesmo formato de saída (exigência da disciplina).
 *
 * IMPORTANTE: a implementação dos métodos desta interface — o algoritmo em si,
 * incluindo qualquer função auxiliar que faça parte do método (distância,
 * atribuição a clusters, fusão de partições etc.) — NÃO pode usar IA generativa.
 */
public interface ClusteringAlgorithm {

    /**
     * Executa o algoritmo de clusterização sobre o dataset fornecido.
     *
     * @param dataset    pontos de entrada
     * @param params     parâmetros do algoritmo (ex.: k, eps, minPts)
     * @param executor   pool de threads a ser usado na paralelização
     * @param numThreads número de threads/cores disponíveis (para a estratégia de particionamento)
     * @return resultado padronizado (rótulo de cluster por ponto)
     */
    ClusteringResult run(List<DataPoint> dataset, AlgorithmParams params,
                          ExecutorService executor, int numThreads) throws Exception;

    /**
     * Nome curto do algoritmo, usado nos arquivos de resultado ("kmeans" ou "dbscan").
     */
    String getName();
}
