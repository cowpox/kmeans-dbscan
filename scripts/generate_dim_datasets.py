#!/usr/bin/env python3
"""
Experimento 03: gera datasets sintéticos em múltiplas dimensionalidades, em
duas variantes, para medir como o aumento de dimensões afeta a qualidade dos
agrupamentos (ver docs/analise_experimento_03.md quando existir).

Variantes:
  - informativa (DIMENSOES_INFORMATIVA, 2 a 1000): todas as d dimensões
    carregam estrutura de cluster (make_blobs(n_features=d) direto).
    Faixa estendida para além de 50 para verificar se a concentração de
    distâncias característica da maldição da dimensionalidade eventualmente
    reverte a tendência observada até d=50 (ver docs/analise_experimento_03.md).
  - ruido (DIMENSOES_RUIDO, 2 a 50): as mesmas 2 dimensões informativas do
    dataset-base (fixas, independentes de d) + (d - 2) dimensões de ruído
    puro (uniforme, sem relação com o cluster) anexadas. Em d=2 não há
    colunas de ruído, então a variante "ruido" em d=2 é idêntica à
    "informativa" em d=2 — serve de âncora comum entre as duas curvas. Não
    estendida além de d=50: o colapso de qualidade já fica evidente nessa
    faixa (ARI ≈ 0 a partir de d=10) e o custo computacional de dimensões
    maiores não se justifica para confirmar um resultado já bem demonstrado.

Mesmo número de clusters verdadeiros (8) e cluster_std (1.0) em todas as
configurações — dimensionalidade é a única variável controlada.

Script de geração de dados: uso de IA permitido pelas regras da disciplina
(scripts de automação de testes, não lógica de clustering).
"""
import argparse
import os

import numpy as np
from sklearn.datasets import make_blobs

N_SAMPLES = 30_000
CENTERS = 8
CLUSTER_STD = 1.0
DIMENSOES_RUIDO = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 50]
DIMENSOES_INFORMATIVA = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 50, 100, 200, 500, 1000]
SEED = 42


def gerar_informativa(d, seed=SEED):
    x, y = make_blobs(n_samples=N_SAMPLES, n_features=d, centers=CENTERS,
                       cluster_std=CLUSTER_STD, random_state=seed)
    return x, y


def gerar_ruido(d, base_x, base_y, seed=SEED):
    """base_x/base_y: dataset-base de 2 dimensões informativas, fixo para
    todos os d (gerado uma única vez). Para d > 2, anexa (d - 2) colunas de
    ruído uniforme, na mesma faixa de valores da parte informativa (para que
    o ruído não seja trivialmente distinguível por escala)."""
    if d <= 2:
        return base_x[:, :d].copy(), base_y.copy()
    rng = np.random.RandomState(seed + d)
    low, high = base_x.min(), base_x.max()
    ruido = rng.uniform(low, high, size=(N_SAMPLES, d - 2))
    x = np.hstack([base_x, ruido])
    return x, base_y.copy()


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
    parser.add_argument("--out-dir", default="data/dim_experimento")
    args = parser.parse_args()

    os.makedirs(args.out_dir, exist_ok=True)

    base_x, base_y = gerar_informativa(2, seed=SEED)

    for d in DIMENSOES_INFORMATIVA:
        x_inf, y_inf = gerar_informativa(d)
        salvar_csv(f"{args.out_dir}/informativa_d{d}.csv", x_inf)
        salvar_labels(f"{args.out_dir}/informativa_d{d}_labels.csv", y_inf)
        print(f"informativa_d{d}: {x_inf.shape[0]} pontos, {x_inf.shape[1]} dimensões")

    for d in DIMENSOES_RUIDO:
        x_ruido, y_ruido = gerar_ruido(d, base_x, base_y)
        salvar_csv(f"{args.out_dir}/ruido_d{d}.csv", x_ruido)
        salvar_labels(f"{args.out_dir}/ruido_d{d}_labels.csv", y_ruido)
        n_informativas = min(d, 2)
        n_ruido = max(d - 2, 0)
        print(f"ruido_d{d}: {x_ruido.shape[0]} pontos, {x_ruido.shape[1]} dimensões "
              f"({n_informativas} informativas + {n_ruido} de ruído)")


if __name__ == "__main__":
    main()
