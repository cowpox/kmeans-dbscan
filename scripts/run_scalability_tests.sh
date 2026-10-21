#!/usr/bin/env bash
# Roda os testes de escalabilidade de k-means e DBSCAN nos dois datasets,
# incrementando o tamanho de 20k em 20k (10k -> 150k, 8 pontos medidos).
#
# Script de automação de testes: uso de IA permitido pelas regras da disciplina.
#
# Pré-requisitos:
#   mvn package                       (gera target/kmeans-dbscan.jar)
#   python scripts/generate_datasets.py  (gera data/dataset1_full.csv e dataset2_full.csv)
#
# Variáveis de ambiente opcionais:
#   THREADS=4 ./scripts/run_scalability_tests.sh

set -euo pipefail

cd "$(dirname "$0")/.."

JAR="target/kmeans-dbscan.jar"
THREADS="${THREADS:-4}"
SIZES=(10000 30000 50000 70000 90000 110000 130000 150000)
RESULTS_CSV="results/scalability_results.csv"

# Ajustar os parâmetros dos algoritmos conforme a calibração feita pelo grupo
# nos datasets escolhidos (ex.: k = número de clusters gerados em generate_datasets.py).
KMEANS_PARAMS="k=8"
DBSCAN_PARAMS="eps=0.5;minPts=10"

if [ ! -f "$JAR" ]; then
    echo "Jar não encontrado em $JAR — rode 'mvn package' antes." >&2
    exit 1
fi

run() {
    local algoritmo=$1
    local dataset_nome=$2
    local dataset_csv=$3
    local params=$4
    local tamanho=$5

    echo ">> $algoritmo | $dataset_nome | n=$tamanho | threads=$THREADS"
    java -jar "$JAR" \
        --algoritmo "$algoritmo" \
        --entrada "$dataset_csv" \
        --tamanho "$tamanho" \
        --threads "$THREADS" \
        --params "$params" \
        --dataset-nome "$dataset_nome" \
        --resultados-csv "$RESULTS_CSV"
}

for dataset_nome in dataset1 dataset2; do
    dataset_csv="data/${dataset_nome}_full.csv"
    if [ ! -f "$dataset_csv" ]; then
        echo "Dataset não encontrado: $dataset_csv — rode 'python scripts/generate_datasets.py' antes." >&2
        exit 1
    fi
    for tamanho in "${SIZES[@]}"; do
        run "kmeans" "$dataset_nome" "$dataset_csv" "$KMEANS_PARAMS" "$tamanho"
        run "dbscan" "$dataset_nome" "$dataset_csv" "$DBSCAN_PARAMS" "$tamanho"
    done
done

echo "Concluído. Resultados em $RESULTS_CSV"
