#!/usr/bin/env python3
"""
Gera os 2 gráficos de escalabilidade exigidos pela disciplina (um por dataset),
cada um com as 2 séries (k-means vs. DBSCAN), a partir de
results/scalability_results.csv.

Script de geração de gráficos: uso de IA permitido pelas regras da disciplina.
"""
import argparse
import os

import pandas as pd
import matplotlib.pyplot as plt


def plot_dataset(df, dataset_nome, out_path):
    subset = df[df["dataset"] == dataset_nome].sort_values("tamanho_dataset")
    fig, ax = plt.subplots(figsize=(8, 5))
    for algoritmo, grupo in subset.groupby("algoritmo"):
        ax.plot(grupo["tamanho_dataset"], grupo["tempo_execucao_ms"], marker="o", label=algoritmo)
    ax.set_xlabel("Tamanho do dataset (número de pontos)")
    ax.set_ylabel("Tempo de execução (ms)")
    ax.set_title(f"Análise de Escalabilidade — {dataset_nome}")
    ax.legend()
    ax.grid(True, alpha=0.3)
    fig.tight_layout()
    fig.savefig(out_path, dpi=150)
    print(f"Salvo: {out_path}")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--csv", default="results/scalability_results.csv")
    parser.add_argument("--out-dir", default="results/plots")
    args = parser.parse_args()

    os.makedirs(args.out_dir, exist_ok=True)
    df = pd.read_csv(args.csv)
    for dataset_nome in sorted(df["dataset"].unique()):
        plot_dataset(df, dataset_nome, f"{args.out_dir}/{dataset_nome}_escalabilidade.png")


if __name__ == "__main__":
    main()
