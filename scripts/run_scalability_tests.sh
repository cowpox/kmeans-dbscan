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

# Parâmetros calibrados por dataset em 2026-11-20 (ver docs/logs.md, A-010):
#   - k = número de centros verdadeiros gerados em generate_datasets.py
#     (dataset1: 8 centros; dataset2: 5 centros).
#   - eps calibrado via grade de valores com sklearn.cluster.DBSCAN em n=150000
#     (tamanho máximo), escolhendo o eps que mais se aproxima do número de
#     clusters verdadeiros com baixo ruído. eps=0.5 (valor anterior) colapsava
#     os clusters verdadeiros em 2-3 clusters nos dois datasets.
declare -A KMEANS_PARAMS=( [dataset1]="k=8" [dataset2]="k=5" )
declare -A DBSCAN_PARAMS=( [dataset1]="eps=0.2;minPts=10" [dataset2]="eps=0.15;minPts=10" )

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

# As 3 versões de cada algoritmo entram no mesmo gráfico (plot_results.py agrupa
# por "algoritmo" automaticamente) — fase exploratória com os 6 algoritmos juntos,
# a entrega final decide quais séries ficam no gráfico oficial (2 por disciplina).
ALGOS_KMEANS=(kmeans-classico kmeans kmeans-paralelo-s)
ALGOS_DBSCAN=(dbscan-classico dbscan dbscan-paralelo-s)

for dataset_nome in dataset1 dataset2; do
    dataset_csv="data/${dataset_nome}_full.csv"
    if [ ! -f "$dataset_csv" ]; then
        echo "Dataset não encontrado: $dataset_csv — rode 'python scripts/generate_datasets.py' antes." >&2
        exit 1
    fi
    for tamanho in "${SIZES[@]}"; do
        for algoritmo in "${ALGOS_KMEANS[@]}"; do
            run "$algoritmo" "$dataset_nome" "$dataset_csv" "${KMEANS_PARAMS[$dataset_nome]}" "$tamanho"
        done
        for algoritmo in "${ALGOS_DBSCAN[@]}"; do
            run "$algoritmo" "$dataset_nome" "$dataset_csv" "${DBSCAN_PARAMS[$dataset_nome]}" "$tamanho"
        done
    done
done

echo "Concluído. Resultados em $RESULTS_CSV"
