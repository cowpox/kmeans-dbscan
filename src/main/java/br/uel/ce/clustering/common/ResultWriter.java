package br.uel.ce.clustering.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;

/**
 * Escrita dos resultados finais: atribuições de cluster por ponto e registro
 * (append) do tempo de execução de cada corrida no CSV de escalabilidade.
 *
 * Função auxiliar de escrita de resultados — uso de IA permitido pelas regras
 * da disciplina (ver README.md, seção "Declaração de uso de IA").
 */
public final class ResultWriter {

    private static final String TIMING_HEADER =
            "algoritmo,dataset,tamanho_dataset,num_threads,tempo_execucao_ms,num_clusters,num_ruido,timestamp";

    private ResultWriter() {
    }

    public static void writeAssignments(Path outputPath, List<DataPoint> dataset, int[] labels) throws IOException {
        StringBuilder sb = new StringBuilder("id,cluster\n");
        for (int i = 0; i < dataset.size(); i++) {
            sb.append(dataset.get(i).getId()).append(',').append(labels[i]).append('\n');
        }
        if (outputPath.getParent() != null) {
            Files.createDirectories(outputPath.getParent());
        }
        Files.writeString(outputPath, sb.toString());
    }

    /**
     * Acrescenta uma linha ao CSV de resultados de escalabilidade, criando o
     * arquivo com cabeçalho se ainda não existir.
     */
    public static synchronized void appendTimingRecord(Path csvPath, String algoritmo, String datasetName,
                                                         int datasetSize, int numThreads, long execucaoMs,
                                                         int numClusters, int numRuido) throws IOException {
        if (csvPath.getParent() != null) {
            Files.createDirectories(csvPath.getParent());
        }
        boolean exists = Files.exists(csvPath);
        if (!exists) {
            Files.writeString(csvPath, TIMING_HEADER + "\n", StandardOpenOption.CREATE);
        }
        String line = String.join(",",
                algoritmo, datasetName, String.valueOf(datasetSize), String.valueOf(numThreads),
                String.valueOf(execucaoMs), String.valueOf(numClusters), String.valueOf(numRuido),
                Instant.now().toString()) + "\n";
        Files.writeString(csvPath, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
