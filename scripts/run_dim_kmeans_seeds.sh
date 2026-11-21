#!/usr/bin/env bash
# Experimentos 03/04 — investigação de variância: o kmeans-paralelo-s usa
# seed=123 fixa por padrão (ver KMeansClustering.java), então rodar a mesma
# configuração várias vezes com o comando original não captura variância
# nenhuma (resultado idêntico sempre). Este script roda cada configuração de
# (variante, dimensão) com 5 seeds diferentes (1 a 5), para medir se a
# inicialização aleatória dos centróides produz instabilidade de qualidade
# (ver outliers isolados em d=9 e d=50 em docs/analise_experimento_04.md).
#
# DBSCAN não é afetado: dbscan-paralelo-s não tem parâmetro de seed (é
# determinístico dado eps/minPts), então não precisa ser re-executado aqui.
#
# Script de automação de testes: uso de IA permitido pelas regras da disciplina.
#
# Pré-requisitos:
#   mvn package
#   python scripts/generate_dim_datasets.py
#   python scripts/calibrate_eps_by_dimension.py (não usado aqui, mas gera os
#     mesmos datasets já usados por este script)

set -euo pipefail

cd "$(dirname "$0")/.."

JAR="target/kmeans-dbscan.jar"
THREADS="${THREADS:-4}"
DATA_DIR="data/dim_experimento"
OUT_DIR="results/experimento_03/assignments_seeds"
TIMING_CSV="results/experimento_03/tempo_execucao_seeds.csv"
K_MEANS=8
SEEDS=(1 2 3 4 5)

DIMENSOES_INFORMATIVA=(1 2 3 4 5 6 7 8 9 10 20 50 100 200 500 1000)
DIMENSOES_RUIDO=(1 2 3 4 5 6 7 8 9 10 20 50)

if [ ! -f "$JAR" ]; then
    echo "Jar não encontrado em $JAR — rode 'mvn package' antes." >&2
    exit 1
fi

mkdir -p "$OUT_DIR"

run() {
    local dataset_nome=$1
    local dataset_csv=$2
    local seed=$3

    local saida="$OUT_DIR/${dataset_nome}_kmeans-paralelo-s_seed${seed}.csv"
    if [ -f "$saida" ] && [ "${FORCE:-0}" != "1" ]; then
        echo ">> kmeans-paralelo-s | $dataset_nome | seed=$seed | já existe, pulando"
        return
    fi

    echo ">> kmeans-paralelo-s | $dataset_nome | seed=$seed"
    java -jar "$JAR" \
        --algoritmo "kmeans-paralelo-s" \
        --entrada "$dataset_csv" \
        --saida "$saida" \
        --threads "$THREADS" \
        --params "k=${K_MEANS};seed=${seed}" \
        --dataset-nome "$dataset_nome" \
        --resultados-csv "$TIMING_CSV"
}

CONFIGS=()
for d in "${DIMENSOES_INFORMATIVA[@]}"; do CONFIGS+=("informativa_${d}"); done
for d in "${DIMENSOES_RUIDO[@]}"; do CONFIGS+=("ruido_${d}"); done

for config in "${CONFIGS[@]}"; do
    variante="${config%_*}"
    d="${config##*_}"
    dataset_nome="${variante}_d${d}"
    dataset_csv="$DATA_DIR/${dataset_nome}.csv"
    if [ ! -f "$dataset_csv" ]; then
        echo "Dataset não encontrado: $dataset_csv — rode generate_dim_datasets.py antes." >&2
        exit 1
    fi

    for seed in "${SEEDS[@]}"; do
        run "$dataset_nome" "$dataset_csv" "$seed"
    done
done

echo "Concluído. Atribuições em $OUT_DIR, tempos em $TIMING_CSV"
