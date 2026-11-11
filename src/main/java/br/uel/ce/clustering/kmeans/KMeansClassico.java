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

// continuar implementação a partir daqui