package br.uel.ce.clustering.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Leitura do dataset de entrada (CSV).
 *
 * Função auxiliar de leitura de arquivo — uso de IA permitido pelas regras da
 * disciplina (ver README.md, seção "Declaração de uso de IA").
 *
 * Formato esperado: "id,x1,x2,...,xn" (uma linha de cabeçalho opcional é
 * detectada e ignorada automaticamente).
 */
public final class DatasetReader {

    private DatasetReader() {
    }

    public static List<DataPoint> read(Path csvPath) throws IOException {
        List<DataPoint> points = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (firstLine) {
                    firstLine = false;
                    if (!isNumeric(parts[0])) {
                        continue; // linha de cabeçalho, pula
                    }
                }
                int id = (int) Double.parseDouble(parts[0]);
                double[] features = new double[parts.length - 1];
                for (int i = 1; i < parts.length; i++) {
                    features[i - 1] = Double.parseDouble(parts[i]);
                }
                points.add(new DataPoint(id, features));
            }
        }
        return points;
    }

    /**
     * Lê o dataset limitando aos primeiros {@code limit} pontos. Usado nos testes
     * de escalabilidade para extrair subconjuntos crescentes do mesmo dataset base.
     */
    public static List<DataPoint> read(Path csvPath, int limit) throws IOException {
        List<DataPoint> all = read(csvPath);
        if (limit < 0 || limit >= all.size()) {
            return all;
        }
        return all.subList(0, limit);
    }

    private static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
