package br.uel.ce.clustering.kmeans;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

/**
 * k-means clássico — implementação sequencial de referência.
 *
 * Baseado em Arthur & Vassilvitskii (2007), com os centróides iniciais escolhidos pelo k-means++
 * 
 * Responsáveis: Herik e Julia.
 * 
 * Complexidade temporal: O(I . n . k . d)
 * Memória auxiliar: O(n + k . d)
 *
 * Não utiliza paralelismo: ignora executor e numThreads.
 *
 * Parâmetros: k = num de clusters, maxIter = max de iterações
 * seed = semente (123), tol = tolerância de convergência
 * 
 * Convenção de rótulos: 0, 1, ..., k-1 = clusters.
 */

public final class KMeansClassico implements ClusteringAlgorithm {
    @Override
    public final ClusteringResult run(List<DataPoint> dataset, AlgorithmParams parametros, ExecutorService executor, int numThreads) {
        int maxIter = parametros.getInt("maxIter", 100);
        int seed = parametros.getInt("seed", 123);
        double tolerancia = parametros.getDouble("tol", 1e-4);
        int n = dataset.size();
        int k = parametros.getInt("k", -1);
        if (k <= 0 || k > n) {
            throw new IllegalArgumentException("K deve estar entre 1 e " + n);
        }

        double [][] centroides = escolherCentroidesIniciais(dataset, k, new Random(seed));
        int dimensoes = centroides[0].length;
        int [] rotulos = new int[n];

        for (int iteracao = 0; iteracao < maxIter; iteracao++) {
            double[][] somas = new double[k][dimensoes];
            int[] contagens = new int[k];
            atribuir(dataset, centroides, rotulos, somas, contagens);
            double [][] novosCentroides = new double[k][];

            // quanto foi o deslocamento do centroide que mais se moveu (distancia ao quadrado)
            double maiorDeslocamento = 0;
            for (int c = 0; c < k; c++) {
                if (contagens[c] == 0) {
                    novosCentroides[c] = centroides[c];
                } else {
                    novosCentroides[c] = new double[dimensoes];
                    for (int j = 0; j < dimensoes; j++) {
                        novosCentroides[c][j] = somas[c][j] / contagens[c];
                    }
                 }
                maiorDeslocamento = Math.max(maiorDeslocamento, distanciaAoQuadrado(centroides[c], novosCentroides[c]));
            }
            centroides = novosCentroides;
            if (tolerancia > 0 && maiorDeslocamento <= tolerancia * tolerancia) {
                break; // convergiu
            }
        }
        return new ClusteringResult(rotulos, k, 0); // k-means nao tem pontos como ruido
    }

    private static void atribuir(List<DataPoint> pontos, double[][] centroides, int[] rotulos, double[][] somas, int[] contagens) {
        for (int i = 0; i < pontos.size(); i++) {
            double[] ponto = pontos.get(i).getFeatures();
            int maisProximo = 0;
            double menorDistancia = distanciaAoQuadrado(ponto, centroides[0]);
            for (int c = 1; c < centroides.length; c++) {
                double distancia = distanciaAoQuadrado(ponto, centroides[c]);
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    maisProximo = c;
                }
            }
            rotulos[i] = maisProximo;
            contagens[maisProximo]++;
            for (int j = 0; j < ponto.length; j++) {
                somas[maisProximo][j] += ponto[j];
            }
        }
    }

    private static double[][] escolherCentroidesIniciais(List<DataPoint> pontos, int k, Random sorteio) {
        int n = pontos.size();
        double[][] centroides = new double[k][];
        centroides[0] = pontos.get(sorteio.nextInt(n)).getFeatures().clone();

        double[] menorDistancia = new double[n];
        Arrays.fill(menorDistancia, Double.MAX_VALUE);

        for (int c = 1; c < k; c++) {
            // so o ultimo centroide escolhido pode diminuir a distancia de algum ponto
            double total = 0;
            for (int i = 0; i < n; i++) {
                double d = distanciaAoQuadrado(pontos.get(i).getFeatures(), centroides[c - 1]);
                menorDistancia[i] = Math.min(menorDistancia[i], d);
                total += menorDistancia[i];
            }
            // sorteia proporcional a distancia (pontos mais longe tem mais chance)
            double alvo = sorteio.nextDouble() * total;
            int escolhido = 0;
            while (escolhido < n - 1) {
                alvo -= menorDistancia[escolhido];
                if (alvo < 0) break;
                escolhido++;
            }
            centroides[c] = pontos.get(escolhido).getFeatures().clone();
        }
        return centroides;
    }

    // calcula a distancia ao quadrado entre os pontos (nao foi usado raiz porque nao muda o centroide escolhido)
    private static double distanciaAoQuadrado (double [] a, double [] b) {
        double soma = 0;
        for (int i = 0; i < a.length; i++) {
            double diferenca = a[i] - b[i];
            soma += diferenca * diferenca;
        }
        return soma;
    }

    @Override
    public String getName() {
        return "kmeans-classico";
    }
}