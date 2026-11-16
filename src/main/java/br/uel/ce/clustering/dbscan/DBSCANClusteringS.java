package br.uel.ce.clustering.dbscan;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * DBSCAN paralelo em memoria compartilhada, inspirado no PDSDBSCAN-S
 * de Patwary et al. (2012), com separacao entre componentes core e bordas.
 *
 * Fases:
 * 1. Identificacao paralela dos pontos core.
 * 2. Union-Find local por particao e registro de arestas entre particoes.
 * 3. Merge paralelo das arestas externas, usando CAS por raiz (sem lock global).
 * 4. Rotulagem dos cores e atribuicao paralela de bordas/ruido.
 *
 * Busca por vizinhos: forca bruta (O(n^2 * d) no pior caso).
 * Convencao: -2 = ruido; 0, 1, ... = clusters.
 */
public final class DBSCANClusteringS implements ClusteringAlgorithm {

    private static final int NOISE = -2;

    @Override
    public ClusteringResult run(List<DataPoint> dataset,
                                AlgorithmParams params,
                                ExecutorService executor,
                                int numThreads) throws Exception {
        double eps = params.getDouble("eps", -1);
        int minPts = params.getInt("minPts", -1);
        if (!Double.isFinite(eps) || eps <= 0 || minPts <= 0) {
            throw new IllegalArgumentException("eps deve ser positivo e finito; minPts deve ser positivo");
        }
        double eps2 = eps * eps;
        if (!Double.isFinite(eps2)) {
            throw new IllegalArgumentException("eps e muito grande para a distancia ao quadrado");
        }

        int n = dataset.size();
        if (n == 0) return new ClusteringResult(new int[0], 0, 0);

        final int threads = Math.max(1, Math.min(numThreads, n));
        if (threads > 1 && executor == null) {
            throw new IllegalArgumentException("ExecutorService e obrigatorio para execucao paralela");
        }
        // O snapshot evita chamadas concorrentes a List.get() e getFeatures().
        // As coordenadas devem permanecer imutaveis durante run().
        double[][] points = new double[n][];
        int dimensions = -1;
        for (int i = 0; i < n; i++) {
            double[] features = dataset.get(i).getFeatures();
            if (features == null) throw new IllegalArgumentException("Features nulas no ponto " + i);
            if (dimensions < 0) dimensions = features.length;
            if (features.length != dimensions) {
                throw new IllegalArgumentException("Dimensoes diferentes no ponto " + i);
            }
            for (double value : features) {
                if (!Double.isFinite(value)) {
                    throw new IllegalArgumentException("Coordenada nao finita no ponto " + i);
                }
            }
            points[i] = features;
        }

        // Intervalos contiguos [starts[t], ends[t]) por thread.
        final int[] starts = new int[threads];
        final int[] ends = new int[threads];
        for (int t = 0; t < threads; t++) {
            starts[t] = (int) ((long) t * n / threads);
            ends[t] = (int) ((long) (t + 1) * n / threads);
        }

        // FASE 1: core[i] so e escrito pelo trabalhador proprietario de i.
        boolean[] core = new boolean[n];
        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            final int start = starts[t], end = ends[t];
            submit(executor, threads, futures, () -> {
                for (int i = start; i < end; i++) {
                    int neighbors = 0;
                    for (int j = 0; j < n; j++) {
                        if (withinEps(points[i], points[j], eps2) && ++neighbors >= minPts) {
                            core[i] = true;
                            break;
                        }
                    }
                }
            });
        }
        await(futures);

        // FASE 2: estruturas locais independentes. Um trabalhador altera
        // parentLocal[i] somente para i pertencente a sua particao.
        int[] parentLocal = new int[n];
        for (int i = 0; i < n; i++) parentLocal[i] = i;
        EdgeBuffer[] crossEdges = new EdgeBuffer[threads];
        futures.clear();
        for (int t = 0; t < threads; t++) {
            final int owner = t, start = starts[t], end = ends[t];
            submit(executor, threads, futures, () -> {
                EdgeBuffer edges = new EdgeBuffer();
                for (int i = start; i < end; i++) {
                    if (!core[i]) continue;
                    for (int j = i + 1; j < n; j++) {
                        if (!core[j] || !withinEps(points[i], points[j], eps2)) continue;
                        if (j < end) {
                            unionLocal(parentLocal, i, j);
                        } else {
                            edges.add(i, j);
                        }
                    }
                }
                crossEdges[owner] = edges;
            });
        }
        await(futures);

        // FASE 3: parent com leituras atomicas; CAS muda apenas uma raiz.
        // Uma raiz menor sempre aponta para uma maior, impedindo ciclos.
        AtomicIntegerArray parent = new AtomicIntegerArray(parentLocal);
        futures.clear();
        for (int t = 0; t < threads; t++) {
            final EdgeBuffer edges = crossEdges[t];
            submit(executor, threads, futures, () -> {
                for (int k = 0; k < edges.size; k++) {
                    long edge = edges.values[k];
                    int i = (int) (edge >>> 32);
                    int j = (int) edge;
                    unionConcurrent(parent, i, j);
                }
            });
        }
        await(futures);

        // FASE 4a: apenas pontos core definem as componentes.
        int[] labels = new int[n];
        Arrays.fill(labels, NOISE);
        Map<Integer, Integer> clusterByRoot = new HashMap<>();
        int clusterCount = 0;
        for (int i = 0; i < n; i++) {
            if (!core[i]) continue;
            int root = findConcurrent(parent, i);
            Integer id = clusterByRoot.get(root);
            if (id == null) {
                id = clusterCount++;
                clusterByRoot.put(root, id);
            }
            labels[i] = id;
        }

        // FASE 4b: cada borda escolhe o primeiro core vizinho na ordem do dataset.
        // Escritas em indices diferentes; labels dos cores nao sao alterados.
        futures.clear();
        for (int t = 0; t < threads; t++) {
            final int start = starts[t], end = ends[t];
            submit(executor, threads, futures, () -> {
                for (int i = start; i < end; i++) {
                    if (core[i]) continue;
                    for (int j = 0; j < n; j++) {
                        if (core[j] && withinEps(points[i], points[j], eps2)) {
                            labels[i] = labels[j];
                            break;
                        }
                    }
                }
            });
        }
        await(futures);

        int noiseCount = 0;
        for (int label : labels) if (label == NOISE) noiseCount++;
        return new ClusteringResult(labels, clusterCount, noiseCount);
    }

    // Executa diretamente no caso de uma thread, permitindo executor == null.
    private static void submit(ExecutorService executor, int threads,
                               List<Future<?>> futures, Runnable task) {
        if (threads == 1) {
            task.run();
        } else {
            futures.add(executor.submit(task));
        }
    }

    private static void await(List<Future<?>> futures)
            throws InterruptedException, ExecutionException {
        for (Future<?> future : futures) future.get();
    }

    private static boolean withinEps(double[] a, double[] b, double eps2) {
        double sum = 0.0;
        for (int d = 0; d < a.length; d++) {
            double diff = a[d] - b[d];
            sum += diff * diff;
            if (sum > eps2) return false;
        }
        return sum <= eps2;
    }

    private static int findLocal(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    private static void unionLocal(int[] parent, int a, int b) {
        int ra = findLocal(parent, a);
        int rb = findLocal(parent, b);
        if (ra == rb) return;
        if (ra < rb) parent[ra] = rb;
        else parent[rb] = ra;
    }

    private static int findConcurrent(AtomicIntegerArray parent, int x) {
        int p;
        while ((p = parent.get(x)) != x) x = p;
        return x;
    }

    /** Union-Find concorrente: monotonia dos indices + CAS evita ciclos e perdas de uniao. */
    private static void unionConcurrent(AtomicIntegerArray parent, int a, int b) {
        while (true) {
            int ra = findConcurrent(parent, a);
            int rb = findConcurrent(parent, b);
            if (ra == rb) return;
            int lower = Math.min(ra, rb);
            int higher = Math.max(ra, rb);
            if (parent.compareAndSet(lower, lower, higher)) return;
            // Outro trabalhador alterou a raiz; recalcular representantes.
        }
    }

    /** Armazena pares de indices sem criar um objeto para cada aresta. */
    private static final class EdgeBuffer {
        private long[] values = new long[16];
        private int size;

        void add(int a, int b) {
            if (size == values.length) {
                int next = values.length + (values.length >>> 1) + 1;
                if (next <= values.length) {
                    throw new IllegalStateException("Quantidade excessiva de arestas externas");
                }
                values = Arrays.copyOf(values, next);
            }
            values[size++] = ((long) a << 32) | (b & 0xffffffffL);
        }
    }

    @Override
    public String getName() {
        return "dbscan-paralelo-s";
    }
}
