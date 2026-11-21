#!/usr/bin/env python3
"""
Experimento 02: avalia a QUALIDADE dos agrupamentos (não o desempenho/tempo,
ver experimento_01) comparando as atribuições de cluster de cada algoritmo
(results/experimento_02/assignments/<dataset>_<algoritmo>.csv, gerado por
run_quality_tests.sh) com o rótulo verdadeiro do make_blobs
(data/<dataset>_labels.csv).

Duas análises:
  1. Qualidade externa: ARI e NMI de cada algoritmo contra o rótulo
     verdadeiro, mais nº de clusters encontrados e fração de ruído (DBSCAN).
  2. Consistência entre implementações: ARI entre as 3 versões de uma mesma
     tarefa (classico vs. paralelo vs. paralelo-s) — deve ficar em ~1.0, já
     que as 3 implementam o mesmo algoritmo e devem produzir o mesmo
     resultado; serve de checagem de corretude, não só de qualidade.

Script de geração/avaliação de métricas sobre resultados já produzidos pelos
algoritmos: uso de IA permitido pelas regras da disciplina (não é lógica de
clustering, é leitura e comparação do resultado final).
"""
import argparse
import itertools
import os

import pandas as pd
from sklearn.metrics import adjusted_rand_score, normalized_mutual_info_score

TAREFAS = {
    "kmeans": ["kmeans-classico", "kmeans", "kmeans-paralelo-s"],
    "dbscan": ["dbscan-classico", "dbscan", "dbscan-paralelo-s"],
}


def carregar_labels(path):
    return pd.read_csv(path).sort_values("id").reset_index(drop=True)


def avaliar_qualidade(assignments_dir, labels_dir, datasets):
    linhas = []
    for dataset_nome in datasets:
        verdadeiro = carregar_labels(f"{labels_dir}/{dataset_nome}_labels.csv")
        num_clusters_verdadeiro = verdadeiro["label_verdadeiro"].nunique()

        for tarefa, algoritmos in TAREFAS.items():
            for algoritmo in algoritmos:
                path = f"{assignments_dir}/{dataset_nome}_{algoritmo}.csv"
                if not os.path.exists(path):
                    print(f"Aviso: {path} não encontrado, pulando.")
                    continue
                previsto = carregar_labels(path).rename(columns={"cluster": "label_previsto"})
                merged = verdadeiro.merge(previsto, on="id")

                ari = adjusted_rand_score(merged["label_verdadeiro"], merged["label_previsto"])
                nmi = normalized_mutual_info_score(merged["label_verdadeiro"], merged["label_previsto"])
                e_ruido = merged["label_previsto"] < 0
                num_clusters_encontrados = merged.loc[~e_ruido, "label_previsto"].nunique()

                linhas.append({
                    "dataset": dataset_nome,
                    "tarefa": tarefa,
                    "algoritmo": algoritmo,
                    "ari": ari,
                    "nmi": nmi,
                    "num_clusters_verdadeiro": num_clusters_verdadeiro,
                    "num_clusters_encontrados": num_clusters_encontrados,
                    "fracao_ruido": e_ruido.mean(),
                })
    return pd.DataFrame(linhas)


def avaliar_consistencia(assignments_dir, datasets):
    linhas = []
    for dataset_nome in datasets:
        for tarefa, algoritmos in TAREFAS.items():
            dados = {}
            for algoritmo in algoritmos:
                path = f"{assignments_dir}/{dataset_nome}_{algoritmo}.csv"
                if os.path.exists(path):
                    dados[algoritmo] = carregar_labels(path)
            for a, b in itertools.combinations(dados.keys(), 2):
                merged = dados[a].merge(dados[b], on="id", suffixes=("_a", "_b"))
                ari = adjusted_rand_score(merged["cluster_a"], merged["cluster_b"])
                linhas.append({
                    "dataset": dataset_nome,
                    "tarefa": tarefa,
                    "algoritmo_a": a,
                    "algoritmo_b": b,
                    "ari_entre_implementacoes": ari,
                })
    return pd.DataFrame(linhas)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--assignments-dir", default="results/experimento_02/assignments")
    parser.add_argument("--labels-dir", default="data")
    parser.add_argument("--out-dir", default="results/experimento_02")
    parser.add_argument("--datasets", nargs="+", default=["dataset1", "dataset2"])
    args = parser.parse_args()

    os.makedirs(args.out_dir, exist_ok=True)

    qualidade = avaliar_qualidade(args.assignments_dir, args.labels_dir, args.datasets)
    qualidade_path = f"{args.out_dir}/qualidade_resultados.csv"
    qualidade.to_csv(qualidade_path, index=False)
    print(f"Salvo: {qualidade_path}")
    print(qualidade.to_string(index=False))

    consistencia = avaliar_consistencia(args.assignments_dir, args.datasets)
    consistencia_path = f"{args.out_dir}/consistencia_implementacoes.csv"
    consistencia.to_csv(consistencia_path, index=False)
    print(f"\nSalvo: {consistencia_path}")
    print(consistencia.to_string(index=False))


if __name__ == "__main__":
    main()
