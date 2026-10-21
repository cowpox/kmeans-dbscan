package br.uel.ce.clustering.common;

/**
 * Resultado padronizado de uma execução de clusterização, comum aos dois algoritmos.
 */
public final class ClusteringResult {

    private final int[] labels;       // rótulo de cluster por ponto (mesmo índice do dataset de entrada)
    private final int numClusters;    // número de clusters encontrados (excluindo ruído)
    private final int numNoisePoints; // pontos de ruído (0 para k-means; rótulo -1 para DBSCAN)

    public ClusteringResult(int[] labels, int numClusters, int numNoisePoints) {
        this.labels = labels;
        this.numClusters = numClusters;
        this.numNoisePoints = numNoisePoints;
    }

    public int[] getLabels() {
        return labels;
    }

    public int getNumClusters() {
        return numClusters;
    }

    public int getNumNoisePoints() {
        return numNoisePoints;
    }
}
