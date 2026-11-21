#!/usr/bin/env python3
"""
Experimento 03: gera gráficos de ARI vs. dimensionalidade (uma linha por
variante) para k-means e DBSCAN, a partir de
results/experimento_03/qualidade_por_dimensao.csv.

Script de geração de gráficos: uso de IA permitido pelas regras da disciplina.
"""
import argparse
import os

import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.ticker import ScalarFormatter


def plot_algoritmo(df, algoritmo, out_path, escala):
    subset = df[df["algoritmo"] == algoritmo]
    tem_min_max = "ari_min" in subset.columns and "ari_max" in subset.columns
    fig, ax = plt.subplots(figsize=(8, 5))
    for variante, grupo in subset.groupby("variante"):
        grupo = grupo.sort_values("dimensao")
        if tem_min_max and grupo["ari_min"].notna().any():
            erro = [grupo["ari"] - grupo["ari_min"].fillna(grupo["ari"]),
                    grupo["ari_max"].fillna(grupo["ari"]) - grupo["ari"]]
            ax.errorbar(grupo["dimensao"], grupo["ari"], yerr=erro, marker="o", label=variante, capsize=3)
        else:
            ax.plot(grupo["dimensao"], grupo["ari"], marker="o", label=variante)
    rotulo_escala = "escala log" if escala == "log" else "escala linear"
    ax.set_xlabel(f"Número de dimensões ({rotulo_escala})")
    ax.set_ylabel("ARI (contra rótulo verdadeiro)")
    ax.set_title(f"Qualidade vs. dimensionalidade — {algoritmo}")
    if escala == "log":
        ax.set_xscale("log")
        ax.xaxis.set_major_formatter(ScalarFormatter())
    ax.set_xticks(sorted(subset["dimensao"].unique()))
    ax.set_ylim(0, 1.05)
    ax.legend()
    ax.grid(True, alpha=0.3)
    fig.tight_layout()
    fig.savefig(out_path, dpi=150)
    print(f"Salvo: {out_path}")
    plt.close(fig)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--csv", default="results/experimento_03/qualidade_por_dimensao.csv")
    parser.add_argument("--out-dir", default="results/experimento_03/plots")
    parser.add_argument("--escala", choices=["log", "linear"], default="log")
    parser.add_argument("--sufixo", default="", help="Sufixo opcional no nome do arquivo de saída")
    args = parser.parse_args()

    os.makedirs(args.out_dir, exist_ok=True)
    df = pd.read_csv(args.csv)
    for algoritmo in sorted(df["algoritmo"].unique()):
        out_path = f"{args.out_dir}/ari_vs_dimensao_{algoritmo}{args.sufixo}.png"
        plot_algoritmo(df, algoritmo, out_path, args.escala)


if __name__ == "__main__":
    main()
