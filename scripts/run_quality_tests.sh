#!/usr/bin/env bash
# Experimento 02: roda cada um dos 6 algoritmos uma vez, no tamanho máximo do
# dataset (150.000 pontos), salvando as atribuições de cluster por ponto
# (--saida) para depois comparar com o rótulo verdadeiro conhecido do
# make_blobs (scripts/evaluate_clustering_quality.py). Isso mede qualidade do
# agrupamento, não desempenho (ver experimento_01 para tempo/speedup).
#
# Script de automação de testes: uso de IA permitido pelas regras da disciplina.
#
# Pré-requisitos:
#   mvn package                          (gera target/kmeans-dbscan.jar)
#   python scripts/generate_datasets.py  (gera data/datasetN_full.csv e datasetN_labels.csv)

set -euo pipefail

cd "$(dirname "$0")/.."

JAR="target/kmeans-dbscan.jar"
THREADS="${THREADS:-4}"
OUT_DIR="results/experimento_02/assignments"

declare -A KMEANS_PARAMS=( [dataset1]="k=8" [dataset2]="k=5" )
declare -A DBSCAN_PARAMS=( [dataset1]="eps=0.2;minPts=10" [dataset2]="eps=0.15;minPts=10" )

if [ ! -f "$JAR" ]; then
    echo "Jar não encontrado em $JAR — rode 'mvn package' antes." >&2
    exit 1
fi

mkdir -p "$OUT_DIR"

ALGOS_KMEANS=(kmeans-classico kmeans kmeans-paralelo-s)
ALGOS_DBSCAN=(dbscan-classico dbscan dbscan-paralelo-s)

run() {
    local algoritmo=$1
    local dataset_nome=$2
    local dataset_csv=$3
    local params=$4

    local saida="$OUT_DIR/${dataset_nome}_${algoritmo}.csv"
    echo ">> $algoritmo | $dataset_nome -> $saida"
    java -jar "$JAR" \
        --algoritmo "$algoritmo" \
        --entrada "$dataset_csv" \
        --saida "$saida" \
        --threads "$THREADS" \
        --params "$params" \
        --dataset-nome "$dataset_nome"
}

for dataset_nome in dataset1 dataset2; do
    dataset_csv="data/${dataset_nome}_full.csv"
    labels_csv="data/${dataset_nome}_labels.csv"
    if [ ! -f "$dataset_csv" ] || [ ! -f "$labels_csv" ]; then
        echo "Dataset/labels não encontrados para $dataset_nome — rode 'python scripts/generate_datasets.py' antes." >&2
        exit 1
    fi
    for algoritmo in "${ALGOS_KMEANS[@]}"; do
        run "$algoritmo" "$dataset_nome" "$dataset_csv" "${KMEANS_PARAMS[$dataset_nome]}"
    done
    for algoritmo in "${ALGOS_DBSCAN[@]}"; do
        run "$algoritmo" "$dataset_nome" "$dataset_csv" "${DBSCAN_PARAMS[$dataset_nome]}"
    done
done

echo "Concluído. Atribuições em $OUT_DIR"
