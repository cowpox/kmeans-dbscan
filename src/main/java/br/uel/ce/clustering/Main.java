package br.uel.ce.clustering;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;
import br.uel.ce.clustering.common.DatasetReader;
import br.uel.ce.clustering.common.ResultWriter;
import br.uel.ce.clustering.dbscan.DBSCANClustering;
import br.uel.ce.clustering.kmeans.KMeansClustering;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ponto de entrada único e padronizado para rodar k-means ou DBSCAN — é a
 * "mesma interface de execução" exigida pela disciplina para os dois algoritmos.
 *
 * Uso:
 *   java -jar target/kmeans-dbscan.jar \
 *        --algoritmo kmeans|dbscan \
 *        --entrada caminho/para/dataset.csv \
 *        [--saida caminho/para/resultado.csv] \
 *        [--tamanho N] \                 (usa só os N primeiros pontos do dataset)
 *        [--threads N] \                 (default: núcleos disponíveis na máquina)
 *        --params "k=8" ou "eps=0.5;minPts=10" \
 *        [--dataset-nome dataset1] \     (rótulo livre, só para o CSV de resultados)
 *        [--resultados-csv results/scalability_results.csv]
 *
 * Mede apenas o tempo de execução do algoritmo (chamada a run(...)), excluindo
 * a leitura do dataset e a inicialização da JVM/thread pool, conforme exigido
 * pelas instruções da disciplina.
 */
public final class Main {

    public static void main(String[] args) throws Exception {
        CliArgs cli = CliArgs.parse(args);

        List<DataPoint> dataset = cli.tamanho > 0
                ? DatasetReader.read(Path.of(cli.entrada), cli.tamanho)
                : DatasetReader.read(Path.of(cli.entrada));

        ClusteringAlgorithm algorithm = switch (cli.algoritmo) {
            case "kmeans" -> new KMeansClustering();
            case "dbscan" -> new DBSCANClustering();
            default -> throw new IllegalArgumentException("Algoritmo desconhecido: " + cli.algoritmo);
        };

        AlgorithmParams params = AlgorithmParams.parse(cli.params);
        ExecutorService executor = Executors.newFixedThreadPool(cli.threads);

        long inicio = System.nanoTime();
        ClusteringResult result = algorithm.run(dataset, params, executor, cli.threads);
        long fim = System.nanoTime();
        long execucaoMs = (fim - inicio) / 1_000_000;

        executor.shutdown();

        if (cli.saida != null) {
            ResultWriter.writeAssignments(Path.of(cli.saida), dataset, result.getLabels());
        }
        if (cli.resultadosCsv != null) {
            ResultWriter.appendTimingRecord(Path.of(cli.resultadosCsv), algorithm.getName(),
                    cli.datasetNome, dataset.size(), cli.threads, execucaoMs,
                    result.getNumClusters(), result.getNumNoisePoints());
        }

        System.out.printf("%s | dataset=%s | n=%d | threads=%d | tempo=%dms | clusters=%d | ruido=%d%n",
                algorithm.getName(), cli.datasetNome, dataset.size(), cli.threads, execucaoMs,
                result.getNumClusters(), result.getNumNoisePoints());
    }

    private static final class CliArgs {
        String algoritmo;
        String entrada;
        String saida;
        int tamanho = -1;
        int threads = Runtime.getRuntime().availableProcessors();
        String params = "";
        String datasetNome = "dataset";
        String resultadosCsv;

        static CliArgs parse(String[] args) {
            CliArgs c = new CliArgs();
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--algoritmo" -> c.algoritmo = args[++i];
                    case "--entrada" -> c.entrada = args[++i];
                    case "--saida" -> c.saida = args[++i];
                    case "--tamanho" -> c.tamanho = Integer.parseInt(args[++i]);
                    case "--threads" -> c.threads = Integer.parseInt(args[++i]);
                    case "--params" -> c.params = args[++i];
                    case "--dataset-nome" -> c.datasetNome = args[++i];
                    case "--resultados-csv" -> c.resultadosCsv = args[++i];
                    default -> throw new IllegalArgumentException("Argumento desconhecido: " + args[i]);
                }
            }
            if (c.algoritmo == null || c.entrada == null) {
                throw new IllegalArgumentException("--algoritmo e --entrada são obrigatórios");
            }
            return c;
        }
    }
}
