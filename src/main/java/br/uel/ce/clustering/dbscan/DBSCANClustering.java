package br.uel.ce.clustering.dbscan;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.List;
import java.util.concurrent.ExecutorService;
public final class DBSCANClustering implements ClusteringAlgorithm {

    @Override
    public ClusteringResult run(List<DataPoint> dataset, AlgorithmParams params,
                                ExecutorService executor, int numThreads) throws Exception {

        double eps = params.getDouble("eps", -1);
        int minPts = params.getInt("minPts", -1);
        if (!Double.isFinite(eps) || eps <= 0 || minPts <= 0) {
            throw new IllegalArgumentException("eps e minPts devem ser positivos");
        }

        int n = dataset.size();
        if (n == 0) {
            return new ClusteringResult(new int[0], 0, 0);
        }

        int threads = Math.max(1, Math.min(numThreads, n));
        int bloco = (n + threads - 1) / threads;
        double eps2 = eps * eps;
        boolean[] core = new boolean[n];
        List<Future<?>> tarefas = new ArrayList<>();

        for (int t = 0; t < threads; t++) {
            int inicio = t * bloco;
            int fim = Math.min(inicio + bloco, n);
            if (inicio >= fim) break;

            tarefas.add(executor.submit(() -> {
                for (int i = inicio; i < fim; i++) {
                    int vizinhos = 0;

                    for (int j = 0; j < n; j++) {
                        if (distancia2(dataset.get(i), dataset.get(j)) <= eps2) {
                            vizinhos++;

                            if (vizinhos >= minPts) break;
                        }
                    }
                    core[i] = vizinhos >= minPts;
                }
            }));
        }

        for (Future<?> tarefa : tarefas) {
            tarefa.get();
        }

        UnionFind grupos = new UnionFind(n);
        tarefas.clear();

        for (int t = 0; t < threads; t++) {
            int inicio = t * bloco;
            int fim = Math.min(inicio + bloco, n);

            if (inicio >= fim) break;

            tarefas.add(executor.submit(() -> {
                for (int i = inicio; i < fim; i++) {
                    if (!core[i]) continue;

                    for (int j = i + 1; j < n; j++) {
                        if (core[j] &&
                                distancia2(dataset.get(i), dataset.get(j)) <= eps2) {
                            grupos.unir(i, j);
                        }
                    }
                }
            }));
        }
        for (Future<?> tarefa : tarefas) {
            tarefa.get();
        }

        int[] labels = new int[n];
        Arrays.fill(labels, -2);

        Map<Integer, Integer> ids = new HashMap<>();
        int numClusters = 0;

        for (int i = 0; i < n; i++) {
            if (!core[i]) continue;

            int raiz = grupos.achar(i);

            if (!ids.containsKey(raiz)) {
                ids.put(raiz, numClusters++);
            }

            labels[i] = ids.get(raiz);
        }

        int ruido = 0;

        for (int i = 0; i < n; i++) {
            if (core[i]) continue;

            for (int j = 0; j < n; j++) {
                if (core[j] &&
                        distancia2(dataset.get(i), dataset.get(j)) <= eps2) {
                    labels[i] = labels[j];
                    break;
                }
            }

            if (labels[i] == -2) {
                ruido++;
            }
        }

        return new ClusteringResult(labels, numClusters, ruido);
    }

    private static double distancia2(DataPoint a, DataPoint b) {
        double[] x = a.getFeatures();
        double[] y = b.getFeatures();

        if (x.length != y.length) {
            throw new IllegalArgumentException("Dimensoes diferentes");
        }
        double soma = 0;

        for (int d = 0; d < x.length; d++) {
            double diferenca = x[d] - y[d];
            soma += diferenca * diferenca;
        }

        return soma;
    }

    private static class UnionFind {
        private final int[] pai;

        UnionFind(int n) {
            pai = new int[n];

            for (int i = 0; i < n; i++) {
                pai[i] = i;
            }
        }

        int achar(int x) {
            if (pai[x] != x) {
                pai[x] = achar(pai[x]);
            }
            return pai[x];
        }
        synchronized void unir(int a, int b) {
            int raizA = achar(a);
            int raizB = achar(b);
            if (raizA != raizB) {
                pai[raizB] = raizA;
            }
        }
    }

    @Override
    public String getName() {
        return "dbscan";
    }
}
