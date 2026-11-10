#!/usr/bin/env python3
"""
Visualização de um resultado de clusterização: lê o dataset de entrada
(id,x1,x2,...) e o CSV de atribuições gerado pelo Main.java via --saida
(id,cluster), junta os dois por "id" e plota um scatter colorido por cluster.

Rótulos negativos (ruído, convenção -2 para DBSCAN) aparecem em cinza com
marcador "x", distintos dos clusters normais.

Script de geração de gráfico: uso de IA permitido pelas regras da disciplina.
"""
import argparse

import matplotlib.pyplot as plt
import pandas as pd


def plot_clusters(dataset_csv, labels_csv, out_path, title):
    dataset = pd.read_csv(dataset_csv)
    labels = pd.read_csv(labels_csv)
    df = dataset.merge(labels, on="id")

    fig, ax = plt.subplots(figsize=(7, 6))

    noise = df[df["cluster"] < 0]
    clustered = df[df["cluster"] >= 0]

    for cluster_id, grupo in clustered.groupby("cluster"):
        ax.scatter(grupo["x1"], grupo["x2"], s=20, label=f"cluster {cluster_id}")

    if not noise.empty:
        ax.scatter(noise["x1"], noise["x2"], s=25, c="gray", marker="x", label="ruído")

    ax.set_title(title)
    ax.set_xlabel("x1")
    ax.set_ylabel("x2")
    ax.legend(loc="best", fontsize=8)
    ax.set_aspect("equal", adjustable="datalim")
    fig.tight_layout()
    fig.savefig(out_path, dpi=150)
    print(f"Salvo: {out_path}")
    print(f"{len(clustered)} pontos em {clustered['cluster'].nunique()} clusters, {len(noise)} pontos de ruído")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--dataset", required=True, help="CSV de entrada (id,x1,x2,...)")
    parser.add_argument("--labels", required=True, help="CSV de saída do Main.java (id,cluster)")
    parser.add_argument("--out", required=True, help="Caminho do PNG a gerar")
    parser.add_argument("--title", default="Resultado da clusterização")
    args = parser.parse_args()

    plot_clusters(args.dataset, args.labels, args.out, args.title)


if __name__ == "__main__":
    main()
