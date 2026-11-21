#!/usr/bin/env python3
"""
Experimento 03: avalia qualidade (ARI/NMI contra o rótulo verdadeiro) e
tempo de execução de kmeans-paralelo-s e dbscan-paralelo-s em função da
dimensionalidade, nas duas variantes (informativa / ruido), a partir de
results/experimento_03/assignments/ e results/experimento_03/tempo_execucao.csv.

Script de avaliação de métricas sobre resultados já produzidos — uso de IA
permitido pelas regras da disciplina (não é lógica de clustering).
"""
import argparse
import os

import pandas as pd
from sklearn.metrics import adjusted_rand_score, normalized_mutual_info_score

ALGORITMOS = ["kmeans-paralelo-s", "dbscan-paralelo-s"]
DIMENSOES_POR_VARIANTE = {
    "informativa": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 50, 100, 200, 500, 1000],
    "ruido": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 50],
}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--assignments-dir", default="results/experimento_03/assignments")
    parser.add_argument("--labels-dir", default="data/dim_experimento")
    parser.add_argument("--timing-csv", default="results/experimento_03/tempo_execucao.csv")
    parser.add_argument("--out", default="results/experimento_03/qualidade_por_dimensao.csv")
    parser.add_argument("--dimensao-max", type=int, default=None,
                         help="Se definido, restringe a avaliação a dimensao <= esse valor")
    args = parser.parse_args()

    if args.dimensao_max is not None:
        DIMENSOES_POR_VARIANTE["informativa"] = [d for d in DIMENSOES_POR_VARIANTE["informativa"] if d <= args.dimensao_max]
        DIMENSOES_POR_VARIANTE["ruido"] = [d for d in DIMENSOES_POR_VARIANTE["ruido"] if d <= args.dimensao_max]

    tempos = pd.read_csv(args.timing_csv) if os.path.exists(args.timing_csv) else None

    linhas = []
    for variante, dimensoes in DIMENSOES_POR_VARIANTE.items():
        for d in dimensoes:
            dataset_nome = f"{variante}_d{d}"
            verdadeiro = pd.read_csv(f"{args.labels_dir}/{dataset_nome}_labels.csv").sort_values("id")

            for algoritmo in ALGORITMOS:
                path = f"{args.assignments_dir}/{dataset_nome}_{algoritmo}.csv"
                if not os.path.exists(path):
                    print(f"Aviso: {path} não encontrado, pulando.")
                    continue
                previsto = pd.read_csv(path).sort_values("id").rename(columns={"cluster": "label_previsto"})
                merged = verdadeiro.merge(previsto, on="id")

                ari = adjusted_rand_score(merged["label_verdadeiro"], merged["label_previsto"])
                nmi = normalized_mutual_info_score(merged["label_verdadeiro"], merged["label_previsto"])
                e_ruido = merged["label_previsto"] < 0
                n_clusters = merged.loc[~e_ruido, "label_previsto"].nunique()

                tempo_ms = None
                if tempos is not None:
                    linha_tempo = tempos[(tempos["dataset"] == dataset_nome) & (tempos["algoritmo"] == algoritmo)]
                    if not linha_tempo.empty:
                        tempo_ms = linha_tempo["tempo_execucao_ms"].iloc[0]

                linhas.append({
                    "variante": variante,
                    "dimensao": d,
                    "algoritmo": algoritmo,
                    "ari": ari,
                    "nmi": nmi,
                    "num_clusters_encontrados": n_clusters,
                    "fracao_ruido": e_ruido.mean(),
                    "tempo_execucao_ms": tempo_ms,
                })

    resultado = pd.DataFrame(linhas)
    os.makedirs(os.path.dirname(args.out), exist_ok=True)
    resultado.to_csv(args.out, index=False)
    print(f"Salvo: {args.out}")
    print(resultado.to_string(index=False))


if __name__ == "__main__":
    main()
