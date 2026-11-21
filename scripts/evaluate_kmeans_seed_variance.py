#!/usr/bin/env python3
"""
Experimentos 03/04 — agrega o ARI das 5 seeds do kmeans-paralelo-s
(results/experimento_03/assignments_seeds/, gerado por
run_dim_kmeans_seeds.sh) em mediana/min/max por (variante, dimensao), para
medir se a instabilidade de inicialização de centróides explica os outliers
isolados vistos com seed=123 fixa (d=9 no experimento_04, d=50 no
experimento_03).

Script de avaliação de métricas sobre resultados já produzidos — uso de IA
permitido pelas regras da disciplina (não é lógica de clustering).
"""
import argparse
import os

import pandas as pd
from sklearn.metrics import adjusted_rand_score

SEEDS = [1, 2, 3, 4, 5]
DIMENSOES_POR_VARIANTE = {
    "informativa": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 50, 100, 200, 500, 1000],
    "ruido": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 50],
}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--assignments-dir", default="results/experimento_03/assignments_seeds")
    parser.add_argument("--labels-dir", default="data/dim_experimento")
    parser.add_argument("--out", default="results/experimento_03/qualidade_kmeans_seeds.csv")
    args = parser.parse_args()

    linhas = []
    for variante, dimensoes in DIMENSOES_POR_VARIANTE.items():
        for d in dimensoes:
            dataset_nome = f"{variante}_d{d}"
            verdadeiro = pd.read_csv(f"{args.labels_dir}/{dataset_nome}_labels.csv").sort_values("id")

            aris = []
            for seed in SEEDS:
                path = f"{args.assignments_dir}/{dataset_nome}_kmeans-paralelo-s_seed{seed}.csv"
                if not os.path.exists(path):
                    print(f"Aviso: {path} não encontrado, pulando seed.")
                    continue
                previsto = pd.read_csv(path).sort_values("id").rename(columns={"cluster": "label_previsto"})
                merged = verdadeiro.merge(previsto, on="id")
                aris.append(adjusted_rand_score(merged["label_verdadeiro"], merged["label_previsto"]))

            if not aris:
                print(f"Aviso: nenhuma seed encontrada para {dataset_nome}, pulando configuração.")
                continue

            serie = pd.Series(aris)
            linhas.append({
                "variante": variante,
                "dimensao": d,
                "algoritmo": "kmeans-paralelo-s",
                "ari_mediana": serie.median(),
                "ari_min": serie.min(),
                "ari_max": serie.max(),
                "ari_desvio": serie.std() if len(serie) > 1 else 0.0,
                "n_seeds": len(serie),
            })

    resultado = pd.DataFrame(linhas).sort_values(["variante", "dimensao"])
    os.makedirs(os.path.dirname(args.out), exist_ok=True)
    resultado.to_csv(args.out, index=False)
    print(f"Salvo: {args.out}")
    print(resultado.to_string(index=False))

    instaveis = resultado[(resultado["ari_max"] - resultado["ari_min"]) > 0.05]
    if not instaveis.empty:
        print(f"\n{len(instaveis)} configuração(ões) com variação de ARI entre seeds > 0.05:")
        print(instaveis[["variante", "dimensao", "ari_min", "ari_mediana", "ari_max"]].to_string(index=False))
    else:
        print("\nNenhuma configuração com variação de ARI entre seeds > 0.05.")


if __name__ == "__main__":
    main()
