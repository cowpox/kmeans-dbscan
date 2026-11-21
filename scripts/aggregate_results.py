#!/usr/bin/env python3
"""
Agrega repetições de uma mesma configuração de teste (mesmo algoritmo, dataset,
tamanho e número de threads) em estatísticas: mediana, desvio-padrão, mínimo e
máximo do tempo de execução. Usado quando run_scalability_tests.sh ou
run_speedup_tests.sh são rodados várias vezes seguidas (sem apagar o CSV entre
execuções), para reduzir ruído de medição (JIT, GC, agendamento do SO).

Mantém o nome de coluna "tempo_execucao_ms" com o valor da mediana, para que
plot_results.py e plot_speedup.py funcionem sem alteração; adiciona colunas
extras (tempo_min_ms, tempo_max_ms, tempo_desvio_ms, n_amostras) que os
scripts de plot usam para desenhar barras de erro quando presentes.

Script de agregação de métricas: uso de IA permitido pelas regras da
disciplina (estatística sobre resultados já medidos, não lógica de
algoritmo).
"""
import argparse

import pandas as pd

GROUP_COLS = ["algoritmo", "dataset", "tamanho_dataset", "num_threads"]


def aggregate(df):
    grupo = df.groupby(GROUP_COLS)["tempo_execucao_ms"]
    agregado = grupo.agg(
        tempo_execucao_ms="median",
        tempo_desvio_ms="std",
        tempo_min_ms="min",
        tempo_max_ms="max",
        n_amostras="count",
    ).reset_index()

    extras = df.groupby(GROUP_COLS)[["num_clusters", "num_ruido"]].first().reset_index()
    agregado = agregado.merge(extras, on=GROUP_COLS)
    agregado["tempo_desvio_ms"] = agregado["tempo_desvio_ms"].fillna(0.0)
    return agregado.sort_values(GROUP_COLS)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--csv", required=True, help="CSV bruto (com repetições) a agregar")
    parser.add_argument("--out", required=True, help="Caminho do CSV agregado de saída")
    args = parser.parse_args()

    df = pd.read_csv(args.csv)
    agregado = aggregate(df)

    n_configs = len(agregado)
    n_amostras_media = agregado["n_amostras"].mean()
    print(f"{len(df)} linhas brutas -> {n_configs} configurações agregadas "
          f"(média de {n_amostras_media:.1f} amostras/configuração)")

    poucas = agregado[agregado["n_amostras"] < 2]
    if not poucas.empty:
        print(f"Aviso: {len(poucas)} configuração(ões) com apenas 1 amostra "
              f"(sem repetição, desvio-padrão = 0):")
        print(poucas[GROUP_COLS].to_string(index=False))

    agregado.to_csv(args.out, index=False)
    print(f"Salvo: {args.out}")


if __name__ == "__main__":
    main()
