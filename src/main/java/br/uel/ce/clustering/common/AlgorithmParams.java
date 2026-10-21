package br.uel.ce.clustering.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Parâmetros de um algoritmo, passados pela linha de comando.
 *
 * Formato esperado em --params: "chave1=valor1;chave2=valor2"
 * Ex.: "k=8" para k-means, "eps=0.5;minPts=10" para DBSCAN.
 */
public final class AlgorithmParams {

    private final Map<String, String> values = new HashMap<>();

    public void put(String key, String value) {
        values.put(key, value);
    }

    public String getString(String key, String defaultValue) {
        return values.getOrDefault(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        String v = values.get(key);
        return v == null ? defaultValue : Integer.parseInt(v);
    }

    public double getDouble(String key, double defaultValue) {
        String v = values.get(key);
        return v == null ? defaultValue : Double.parseDouble(v);
    }

    public static AlgorithmParams parse(String raw) {
        AlgorithmParams params = new AlgorithmParams();
        if (raw == null || raw.isBlank()) {
            return params;
        }
        for (String pair : raw.split(";")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                params.put(kv[0].trim(), kv[1].trim());
            }
        }
        return params;
    }
}
