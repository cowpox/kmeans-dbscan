package br.uel.ce.clustering.common;

import java.util.Arrays;

/**
 * Representa um ponto do dataset. Classe de dados pura (sem lógica de algoritmo).
 */
public final class DataPoint {

    private final int id;
    private final double[] features;

    public DataPoint(int id, double[] features) {
        this.id = id;
        this.features = features;
    }

    public int getId() {
        return id;
    }

    public double[] getFeatures() {
        return features;
    }

    public int getDimensions() {
        return features.length;
    }

    @Override
    public String toString() {
        return "DataPoint{id=" + id + ", features=" + Arrays.toString(features) + "}";
    }
}
