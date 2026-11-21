#!/usr/bin/env python3
"""
Experimentos 03/04 — mescla o CSV principal de qualidade (uma execução por
configuração, seed=123 fixa) com o CSV de variância de seeds do k-means
(5 seeds, mediana/min/max), substituindo o ARI de ponto único do k-means
pela mediana das 5 seeds e anexando ari_min/ari_max (usados pelas barras de
erro em plot_dim_quality.py). Linhas de DBSCAN não mudam — não têm seed,
então não têm barra de erro (ari_min/ari_max ficam vazios).

Script de agregação sobre resultados já produzidos — uso de IA permitido
pelas regras da disciplina (não é lógica de clustering).
"""
import argparse

import pandas as pd


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--qualidade-csv", required=True)
    parser.add_argument("--seeds-csv", required=True)
    parser.add_argument("--out", required=True)
    args = parser.parse_args()

    qualidade = pd.read_csv(args.qualidade_csv)
    seeds = pd.read_csv(args.seeds_csv)

    qualidade["ari_min"] = pd.NA
    qualidade["ari_max"] = pd.NA

    for _, linha in seeds.iterrows():
        mascara = (
            (qualidade["variante"] == linha["variante"])
            & (qualidade["dimensao"] == linha["dimensao"])
            & (qualidade["algoritmo"] == "kmeans-paralelo-s")
        )
        qualidade.loc[mascara, "ari"] = linha["ari_mediana"]
        qualidade.loc[mascara, "ari_min"] = linha["ari_min"]
        qualidade.loc[mascara, "ari_max"] = linha["ari_max"]

    qualidade.to_csv(args.out, index=False)
    print(f"Salvo: {args.out}")


if __name__ == "__main__":
    main()
