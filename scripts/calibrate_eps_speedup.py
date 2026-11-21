#!/usr/bin/env python3
"""
Experimento 05: calibra o eps do DBSCAN para os datasets
"informativa_d{d}_dbscan.csv" gerados por generate_speedup_dim_datasets.py
(n=25.000, d=2/50 — tamanho reduzido usado só nos testes de DBSCAN, porque
em n=150.000 uma única execução sequencial em d=50 já leva ~22min, inviável
para a bateria completa de threads x repetições; k-means continua em
n=150.000 e não precisa de eps). Mesma metodologia de
calibrate_eps_by_dimension.py (experimento_03). minPts fica fixo,
consistente com os outros experimentos.

Script de automação de calibração de parâmetros de teste — uso de IA
permitido pelas regras da disciplina (não é lógica de clustering do
DBSCAN em si, que está implementado em Java).
"""
import argparse
import os

import numpy as np
import pandas as pd
from sklearn.cluster import DBSCAN
from sklearn.neighbors import NearestNeighbors

MIN_PTS = 10
DIMENSOES = [2, 50]
PERCENTIS_CANDIDATOS = [40, 50, 55, 60, 65, 70, 75, 80, 85, 88, 90, 92, 94, 96, 98]
AMOSTRA_CALIBRACAO = 15_000


def calibrar(x, true_k, min_pts=MIN_PTS, seed=0):
    if len(x) > AMOSTRA_CALIBRACAO:
        rng = np.random.RandomState(seed)
        idx = rng.choice(len(x), size=AMOSTRA_CALIBRACAO, replace=False)
        x = x[idx]
    nn = NearestNeighbors(n_neighbors=min_pts, n_jobs=-1).fit(x)
    distancias, _ = nn.kneighbors(x)
    k_dist = np.sort(distancias[:, -1])
    candidatos = sorted(set(np.percentile(k_dist, PERCENTIS_CANDIDATOS)))

    melhor = None
    for eps in candidatos:
        labels = DBSCAN(eps=eps, min_samples=min_pts, n_jobs=-1).fit_predict(x)
        n_clusters = len(set(labels)) - (1 if -1 in labels else 0)
        frac_ruido = float(np.mean(labels == -1))
        diff_k = abs(n_clusters - true_k)
        chave = (diff_k, frac_ruido)
        if melhor is None or chave < melhor[0]:
            melhor = (chave, eps, n_clusters, frac_ruido)

    _, eps_escolhido, n_clusters, frac_ruido = melhor
    return eps_escolhido, n_clusters, frac_ruido


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--data-dir", default="data/speedup_dim_experimento")
    parser.add_argument("--out", default="results/experimento_05/eps_calibrado.csv")
    parser.add_argument("--refazer-tudo", action="store_true",
                         help="Recalibra mesmo as configurações já presentes em --out")
    args = parser.parse_args()

    ja_calibradas = set()
    linhas_existentes = []
    if os.path.exists(args.out) and not args.refazer_tudo:
        existente = pd.read_csv(args.out)
        linhas_existentes = existente.to_dict("records")
        ja_calibradas = set(existente["dimensao"])

    linhas = list(linhas_existentes)
    for d in DIMENSOES:
        if d in ja_calibradas:
            print(f">> d={d} já calibrado em {args.out}, pulando.")
            continue

        dataset_path = f"{args.data_dir}/informativa_d{d}_dbscan.csv"
        labels_path = f"{args.data_dir}/informativa_d{d}_dbscan_labels.csv"

        df = pd.read_csv(dataset_path)
        x = df.drop(columns=["id"]).to_numpy()
        true_k = pd.read_csv(labels_path)["label_verdadeiro"].nunique()

        print(f">> calibrando d={d} (n={len(x)}, true_k={true_k})...")
        eps, n_clusters, frac_ruido = calibrar(x, true_k)
        print(f"   eps={eps:.4f} -> clusters_encontrados={n_clusters}, ruido={frac_ruido:.4f}")

        linhas.append({
            "dimensao": d,
            "eps": eps,
            "min_pts": MIN_PTS,
            "clusters_verdadeiro": true_k,
            "clusters_na_calibracao": n_clusters,
            "fracao_ruido_na_calibracao": frac_ruido,
        })

    resultado = pd.DataFrame(linhas).sort_values("dimensao")
    os.makedirs(os.path.dirname(args.out), exist_ok=True)
    resultado.to_csv(args.out, index=False)
    print(f"\nSalvo: {args.out}")
    print(resultado.to_string(index=False))


if __name__ == "__main__":
    main()
