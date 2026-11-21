#!/usr/bin/env python3
"""
Gera os gráficos de speedup (avaliação adicional opcional da disciplina):
para cada dataset e cada algoritmo (k-means, DBSCAN), mostra o ganho das
versões paralelas em relação à versão clássica (sequencial), com o tamanho
do dataset fixo e o número de threads variando no eixo X.

speedup(threads) = tempo_execucao(versão clássica) / tempo_execucao(versão
paralela, threads)

A partir de results/speedup_results.csv, gerado por run_speedup_tests.sh.

Script de geração de gráficos: uso de IA permitido pelas regras da disciplina.
"""
import argparse
import os

import pandas as pd
import matplotlib.pyplot as plt

ALGOS_PARALELOS = {
    "kmeans": {"classico": "kmeans-classico", "paralelas": ["kmeans", "kmeans-paralelo-s"]},
    "dbscan": {"classico": "dbscan-classico", "paralelas": ["dbscan", "dbscan-paralelo-s"]},
}


def plot_speedup(df, dataset_nome, algoritmo_base, config, out_path):
    subset = df[df["dataset"] == dataset_nome]
    tem_min_max = "tempo_min_ms" in subset.columns and "tempo_max_ms" in subset.columns

    baseline_rows = subset[subset["algoritmo"] == config["classico"]]
    if baseline_rows.empty:
        print(f"Aviso: sem baseline '{config['classico']}' para {dataset_nome}, pulando.")
        return
    tempo_base = baseline_rows["tempo_execucao_ms"].iloc[0]

    fig, ax = plt.subplots(figsize=(8, 5))
    algum_dado = False
    for algoritmo in config["paralelas"]:
        grupo = subset[subset["algoritmo"] == algoritmo].sort_values("num_threads")
        if grupo.empty:
            continue
        speedup = tempo_base / grupo["tempo_execucao_ms"]
        if tem_min_max:
            # tempo menor -> speedup maior, por isso os limites se invertem.
            speedup_min = tempo_base / grupo["tempo_max_ms"]
            speedup_max = tempo_base / grupo["tempo_min_ms"]
            erro = [speedup - speedup_min, speedup_max - speedup]
            ax.errorbar(grupo["num_threads"], speedup, yerr=erro, marker="o", label=algoritmo, capsize=3)
        else:
            ax.plot(grupo["num_threads"], speedup, marker="o", label=algoritmo)
        algum_dado = True

    if not algum_dado:
        print(f"Aviso: sem dados paralelos para {algoritmo_base}/{dataset_nome}, pulando.")
        plt.close(fig)
        return

    max_threads = subset["num_threads"].max()
    ax.plot([1, max_threads], [1, max_threads], linestyle="--", color="gray", alpha=0.5, label="speedup ideal")

    ax.set_xlabel("Número de threads")
    ax.set_ylabel(f"Speedup (tempo {config['classico']} / tempo paralelo)")
    ax.set_title(f"Speedup — {algoritmo_base} — {dataset_nome} (n={baseline_rows['tamanho_dataset'].iloc[0]})")
    ax.legend()
    ax.grid(True, alpha=0.3)
    fig.tight_layout()
    fig.savefig(out_path, dpi=150)
    print(f"Salvo: {out_path}")
    plt.close(fig)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--csv", default="results/speedup_results.csv")
    parser.add_argument("--out-dir", default="results/plots")
    args = parser.parse_args()

    os.makedirs(args.out_dir, exist_ok=True)
    df = pd.read_csv(args.csv)

    for dataset_nome in sorted(df["dataset"].unique()):
        for algoritmo_base, config in ALGOS_PARALELOS.items():
            out_path = f"{args.out_dir}/{dataset_nome}_speedup_{algoritmo_base}.png"
            plot_speedup(df, dataset_nome, algoritmo_base, config, out_path)


if __name__ == "__main__":
    main()
