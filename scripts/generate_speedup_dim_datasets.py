#!/usr/bin/env python3
"""
Experimento 05: gera datasets sintéticos (variante "informativa", mesma
metodologia do experimento_03/04) em n=150.000 pontos, para 3 dimensões
(d=2, 50, 500), usados para medir se a dimensionalidade muda a curva de
speedup/escalabilidade por número de threads (ver docs/analise_experimento_05.md
quando existir).

n=150.000 replica o tamanho fixo usado no experimento_01 (run_speedup_tests.sh),
para manter o baseline (d=2) comparável. d=50 e d=500 reaproveitam os pontos de
dimensionalidade já estudados no experimento_03 (onde a qualidade satura e o
custo por ponto cresce), mas aqui com n maior (os datasets do experimento_03
são n=30.000, pequenos demais para saturar 16 threads).

Script de geração de dados: uso de IA permitido pelas regras da disciplina
(scripts de automação de testes, não lógica de clustering).
"""
import argparse
import os

from sklearn.datasets import make_blobs

N_SAMPLES = 150_000
N_SAMPLES_DBSCAN = 25_000
CENTERS = 8
CLUSTER_STD = 1.0
DIMENSOES = [2, 50]
SEED = 42


def salvar_csv(path, x):
    colunas = ",".join(f"x{i+1}" for i in range(x.shape[1]))
    with open(path, "w") as f:
        f.write(f"id,{colunas}\n")
        for i, row in enumerate(x):
            valores = ",".join(f"{v:.6f}" for v in row)
            f.write(f"{i},{valores}\n")


def salvar_labels(path, y):
    with open(path, "w") as f:
        f.write("id,label_verdadeiro\n")
        for i, label in enumerate(y):
            f.write(f"{i},{label}\n")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--out-dir", default="data/speedup_dim_experimento")
    args = parser.parse_args()

    os.makedirs(args.out_dir, exist_ok=True)

    for d in DIMENSOES:
        x, y = make_blobs(n_samples=N_SAMPLES, n_features=d, centers=CENTERS,
                           cluster_std=CLUSTER_STD, random_state=SEED)
        salvar_csv(f"{args.out_dir}/informativa_d{d}.csv", x)
        salvar_labels(f"{args.out_dir}/informativa_d{d}_labels.csv", y)
        print(f"informativa_d{d}: {x.shape[0]} pontos, {x.shape[1]} dimensões")

    # DBSCAN tem custo ~O(n^2 * d): em n=150.000 e d=50, uma única execução
    # sequencial leva ~22min (medido), inviável para a bateria completa de
    # threads x repetições. Gera uma versão menor (n=25.000, mesma escala já
    # usada no experimento_03/04) só para os testes de DBSCAN; k-means
    # continua usando a versão de 150.000 acima.
    for d in DIMENSOES:
        x, y = make_blobs(n_samples=N_SAMPLES_DBSCAN, n_features=d, centers=CENTERS,
                           cluster_std=CLUSTER_STD, random_state=SEED)
        salvar_csv(f"{args.out_dir}/informativa_d{d}_dbscan.csv", x)
        salvar_labels(f"{args.out_dir}/informativa_d{d}_dbscan_labels.csv", y)
        print(f"informativa_d{d}_dbscan: {x.shape[0]} pontos, {x.shape[1]} dimensões")


if __name__ == "__main__":
    main()
