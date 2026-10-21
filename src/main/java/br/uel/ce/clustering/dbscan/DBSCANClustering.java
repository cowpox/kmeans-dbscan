package br.uel.ce.clustering.dbscan;

import br.uel.ce.clustering.common.AlgorithmParams;
import br.uel.ce.clustering.common.ClusteringAlgorithm;
import br.uel.ce.clustering.common.ClusteringResult;
import br.uel.ce.clustering.common.DataPoint;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Implementação paralela do DBSCAN.
 *
 * Responsáveis: Adriano e Sofia.
 *
 * REGRA DA DISCIPLINA: a implementação deste algoritmo — incluindo qualquer
 * função auxiliar que faça parte do método (cálculo de distância, busca de
 * vizinhança, expansão de clusters, fusão entre partições etc.) — NÃO pode
 * usar IA generativa. Ver ../../../../../../docs/referencial_teorico.md para
 * as referências de DBSCAN paralelo em memória compartilhada — em especial
 * Patwary et al. 2012 (merge de clusters via disjoint-set/union-find) e
 * Wang, Gu & Shun 2020 (mais aplicável ao nosso cenário com
 * ExecutorService/ForkJoin numa única máquina, memória compartilhada).
 *
 * Parâmetros esperados em AlgorithmParams (via --params na CLI):
 *   eps     — raio da vizinhança (obrigatório)
 *   minPts  — número mínimo de pontos para formar uma região densa (obrigatório)
 */
public final class DBSCANClustering implements ClusteringAlgorithm {

    @Override
    public ClusteringResult run(List<DataPoint> dataset, AlgorithmParams params,
                                 ExecutorService executor, int numThreads) {
        double eps = params.getDouble("eps", -1);
        int minPts = params.getInt("minPts", -1);
        if (eps <= 0 || minPts <= 0) {
            throw new IllegalArgumentException("Parâmetros obrigatórios ausentes: eps, minPts");
        }

        // TODO (Adriano, Sofia): implementar o DBSCAN paralelo aqui.
        //  - Particionar o espaço de dados entre as threads do ExecutorService.
        //  - Cada partição identifica pontos núcleo e expande clusters localmente.
        //  - Tratar os pontos na fronteira entre partições: mesclar clusters que
        //    se espalham por mais de uma partição (ex.: estrutura union-find).
        //  - Rotular pontos de ruído com -1.
        throw new UnsupportedOperationException("DBSCAN ainda não implementado");
    }

    @Override
    public String getName() {
        return "dbscan";
    }
}
