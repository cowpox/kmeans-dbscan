#!/usr/bin/env bash
# Experimento 05: replica a metodologia de speedup do experimento_01
# (run_speedup_tests.sh), mas variando a dimensionalidade do dataset
# (d=2, 50; n=150.000 fixo) em vez do dataset. Objetivo: verificar se o
# ganho de paralelismo muda conforme cada ponto fica mais caro de processar
# (mais dimensões = mais trabalho por thread em relação ao overhead fixo de
# sincronização).
#
# d=500 foi removido do escopo: o custo do DBSCAN é ~O(n^2 * d), então em
# n=150.000 uma única execução sequencial em d=500 chegaria a horas (vs.
# ~130-320s em d=2) — inviável para 5 threads x 5 repetições x 2 variantes
# de DBSCAN. d=50 já aumenta bastante o custo por ponto (confirma se a
# tendência existe) sem tornar o experimento inviável.
#
# k-means e DBSCAN usam tamanhos de dataset DIFERENTES neste experimento:
# k-means fica em n=150.000 (rápido, igual ao experimento_01). DBSCAN usa
# n=25.000 (informativa_d{d}_dbscan.csv) porque uma única execução
# sequencial em n=150.000/d=50 foi medida em ~22min — inviável para 5
# threads x 5 repetições x 2 variantes. n=25.000 está na mesma escala já
# usada no experimento_03/04 e mantém o objetivo do experimento (ver se d
# muda a forma da curva de speedup), só não permite comparar diretamente
# o tempo absoluto de k-means com o de DBSCAN nesta rodada.
#
# Script de automação de testes: uso de IA permitido pelas regras da disciplina.
#
# Pré-requisitos:
#   mvn package
#   python scripts/generate_speedup_dim_datasets.py
#   python scripts/calibrate_eps_speedup.py
#
# Variáveis de ambiente opcionais:
#   REPS=5 ./scripts/run_speedup_dim_tests.sh   (repetições por configuração; default 5)

set -euo pipefail

cd "$(dirname "$0")/.."

JAR="target/kmeans-dbscan.jar"
SIZE_KMEANS=150000
SIZE_DBSCAN=25000
THREADS_LIST=(1 2 4 8 16)
REPS="${REPS:-5}"
RESULTS_CSV="results/experimento_05/speedup_dim_results.csv"
EPS_CSV="results/experimento_05/eps_calibrado.csv"

if [ ! -f "$JAR" ]; then
    echo "Jar não encontrado em $JAR — rode 'mvn package' antes." >&2
    exit 1
fi
if [ ! -f "$EPS_CSV" ]; then
    echo "eps não calibrado — rode 'python scripts/calibrate_eps_speedup.py' antes." >&2
    exit 1
fi

eps_de() {
    local d=$1
    awk -F, -v d="$d" 'NR==1{for(i=1;i<=NF;i++){if($i=="dimensao")di=i; if($i=="eps")ei=i}} NR>1 && $di==d {print $ei}' "$EPS_CSV"
}

run() {
    local algoritmo=$1
    local dataset_nome=$2
    local dataset_csv=$3
    local params=$4
    local threads=$5
    local tamanho=$6

    echo ">> $algoritmo | $dataset_nome | n=$tamanho | threads=$threads"
    java -jar "$JAR" \
        --algoritmo "$algoritmo" \
        --entrada "$dataset_csv" \
        --tamanho "$tamanho" \
        --threads "$threads" \
        --params "$params" \
        --dataset-nome "$dataset_nome" \
        --resultados-csv "$RESULTS_CSV"
}

mkdir -p "$(dirname "$RESULTS_CSV")"

for d in 2 50; do
    dataset_nome="d${d}"
    dataset_csv_kmeans="data/speedup_dim_experimento/informativa_d${d}.csv"
    dataset_csv_dbscan="data/speedup_dim_experimento/informativa_d${d}_dbscan.csv"
    if [ ! -f "$dataset_csv_kmeans" ] || [ ! -f "$dataset_csv_dbscan" ]; then
        echo "Dataset não encontrado — rode 'python scripts/generate_speedup_dim_datasets.py' antes." >&2
        exit 1
    fi

    eps=$(eps_de "$d")
    kmeans_params="k=8"
    dbscan_params="eps=${eps};minPts=10"

    for rep in $(seq 1 "$REPS"); do
        echo "== d=$d, repetição $rep/$REPS =="

        # Baseline sequencial: --threads é ignorado pelas versões clássicas.
        run "kmeans-classico" "$dataset_nome" "$dataset_csv_kmeans" "$kmeans_params" 1 "$SIZE_KMEANS"
        run "dbscan-classico" "$dataset_nome" "$dataset_csv_dbscan" "$dbscan_params" 1 "$SIZE_DBSCAN"

        for threads in "${THREADS_LIST[@]}"; do
            run "kmeans" "$dataset_nome" "$dataset_csv_kmeans" "$kmeans_params" "$threads" "$SIZE_KMEANS"
            run "kmeans-paralelo-s" "$dataset_nome" "$dataset_csv_kmeans" "$kmeans_params" "$threads" "$SIZE_KMEANS"
            run "dbscan" "$dataset_nome" "$dataset_csv_dbscan" "$dbscan_params" "$threads" "$SIZE_DBSCAN"
            run "dbscan-paralelo-s" "$dataset_nome" "$dataset_csv_dbscan" "$dbscan_params" "$threads" "$SIZE_DBSCAN"
        done
    done
done

echo "Concluído. Resultados em $RESULTS_CSV"
