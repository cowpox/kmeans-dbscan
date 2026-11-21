#!/usr/bin/env bash
# Mede o ganho de speedup das versões paralelas de k-means e DBSCAN em relação
# às versões clássicas (sequenciais): tamanho do dataset fixo, variando o
# número de threads das versões paralelas. Avaliação adicional opcional
# descrita nas instruções da disciplina (seção 3, "Avaliações adicionais
# opcionais"), complementar aos 2 gráficos obrigatórios de escalabilidade
# (ver run_scalability_tests.sh).
#
# Script de automação de testes: uso de IA permitido pelas regras da disciplina.
#
# Pré-requisitos:
#   mvn package                          (gera target/kmeans-dbscan.jar)
#   python scripts/generate_datasets.py  (gera data/dataset1_full.csv e dataset2_full.csv)
#
# Variáveis de ambiente opcionais:
#   SIZE=100000 ./scripts/run_speedup_tests.sh

set -euo pipefail

cd "$(dirname "$0")/.."

JAR="target/kmeans-dbscan.jar"
SIZE="${SIZE:-150000}"
THREADS_LIST=(1 2 4 8 16)
RESULTS_CSV="results/speedup_results.csv"

# Mesmos parâmetros calibrados por dataset usados em run_scalability_tests.sh.
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
    local threads=$5

    echo ">> $algoritmo | $dataset_nome | n=$SIZE | threads=$threads"
    java -jar "$JAR" \
        --algoritmo "$algoritmo" \
        --entrada "$dataset_csv" \
        --tamanho "$SIZE" \
        --threads "$threads" \
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

    # Baseline sequencial: roda uma vez (--threads é ignorado pelas versões clássicas).
    run "kmeans-classico" "$dataset_nome" "$dataset_csv" "${KMEANS_PARAMS[$dataset_nome]}" 1
    run "dbscan-classico" "$dataset_nome" "$dataset_csv" "${DBSCAN_PARAMS[$dataset_nome]}" 1

    for threads in "${THREADS_LIST[@]}"; do
        run "kmeans" "$dataset_nome" "$dataset_csv" "${KMEANS_PARAMS[$dataset_nome]}" "$threads"
        run "kmeans-paralelo-s" "$dataset_nome" "$dataset_csv" "${KMEANS_PARAMS[$dataset_nome]}" "$threads"
        run "dbscan" "$dataset_nome" "$dataset_csv" "${DBSCAN_PARAMS[$dataset_nome]}" "$threads"
        run "dbscan-paralelo-s" "$dataset_nome" "$dataset_csv" "${DBSCAN_PARAMS[$dataset_nome]}" "$threads"
    done
done

echo "Concluído. Resultados em $RESULTS_CSV"
