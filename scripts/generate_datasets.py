#!/usr/bin/env python3
"""
Geração dos datasets sintéticos usados nos testes de escalabilidade.

Uso de IA permitido para este script, conforme as regras da disciplina
(scripts de automação de testes e geração de dados/gráficos).

Gera dois datasets com distribuições diferentes, no tamanho máximo (150.000
pontos). Os testes de escalabilidade (run_scalability_tests.sh) usam
subconjuntos (primeiros N pontos) desses arquivos para cada tamanho avaliado,
garantindo que o mesmo dado "cresça" de forma consistente entre os pontos
medidos.
"""
import argparse

import numpy as np
from sklearn.datasets import make_blobs

MAX_SIZE = 150_000


def gerar_dataset1(n, seed=42):
    """Blobs gaussianos bem separados, mesmo desvio padrão em todos os clusters."""
    x, y = make_blobs(n_samples=n, n_features=2, centers=8, cluster_std=1.0, random_state=seed)
    return x, y


def gerar_dataset2(n, seed=7):
    """Blobs com densidades e formatos diferentes (anisotrópicos) — mais desafiador."""
    x, y = make_blobs(n_samples=n, n_features=2, centers=5,
                       cluster_std=[0.4, 1.2, 0.6, 2.0, 0.8], random_state=seed)
    transformacao = np.array([[0.6, -0.6], [-0.4, 0.8]])
    return x @ transformacao, y


def salvar_csv(path, x):
    with open(path, "w") as f:
        f.write("id,x1,x2\n")
        for i, row in enumerate(x):
            f.write(f"{i},{row[0]:.6f},{row[1]:.6f}\n")


def salvar_labels(path, y):
    """Rótulo verdadeiro (gerado pelo make_blobs) de cada ponto, por id — usado
    como ground truth no experimento_02 (qualidade de clustering: ARI/NMI),
    nunca repassado aos algoritmos de clustering em si."""
    with open(path, "w") as f:
        f.write("id,label_verdadeiro\n")
        for i, label in enumerate(y):
            f.write(f"{i},{label}\n")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--n", type=int, default=MAX_SIZE, help="Tamanho máximo de cada dataset")
    parser.add_argument("--out-dir", default="data", help="Diretório de saída")
    args = parser.parse_args()

    x1, y1 = gerar_dataset1(args.n)
    x2, y2 = gerar_dataset2(args.n)

    salvar_csv(f"{args.out_dir}/dataset1_full.csv", x1)
    salvar_csv(f"{args.out_dir}/dataset2_full.csv", x2)
    salvar_labels(f"{args.out_dir}/dataset1_labels.csv", y1)
    salvar_labels(f"{args.out_dir}/dataset2_labels.csv", y2)

    print(f"dataset1_full.csv: {len(x1)} pontos (blobs gaussianos bem separados)")
    print(f"dataset2_full.csv: {len(x2)} pontos (blobs anisotrópicos, densidades diferentes)")


if __name__ == "__main__":
    main()
