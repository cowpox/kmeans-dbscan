#!/usr/bin/env bash
# Experimento 03: roda kmeans-paralelo-s e dbscan-paralelo-s em cada
# combinação de (variante, dimensão) gerada por generate_dim_datasets.py,
# usando o eps calibrado por calibrate_eps_by_dimension.py. Salva
# atribuições de cluster (para ARI/NMI) e tempo de execução.
#
# Script de automação de testes: uso de IA permitido pelas regras da disciplina.
#
# Pré-requisitos:
#   mvn package
#   python scripts/generate_dim_datasets.py
#   python scripts/calibrate_eps_by_dimension.py

set -euo pipefail

cd "$(dirname "$0")/.."

JAR="target/kmeans-dbscan.jar"
THREADS="${THREADS:-4}"
DATA_DIR="data/dim_experimento"
OUT_DIR="results/experimento_03"
ASSIGNMENTS_DIR="$OUT_DIR/assignments"
TIMING_CSV="$OUT_DIR/tempo_execucao.csv"
EPS_CSV="$OUT_DIR/eps_calibrado.csv"
K_MEANS=8

DIMENSOES_INFORMATIVA=(1 2 3 4 5 6 7 8 9 10 20 50 100 200 500 1000)
DIMENSOES_RUIDO=(1 2 3 4 5 6 7 8 9 10 20 50)

if [ ! -f "$JAR" ]; then
    echo "Jar não encontrado em $JAR — rode 'mvn package' antes." >&2
    exit 1
fi
if [ ! -f "$EPS_CSV" ]; then
    echo "$EPS_CSV não encontrado — rode 'python scripts/calibrate_eps_by_dimension.py' antes." >&2
    exit 1
fi

mkdir -p "$ASSIGNMENTS_DIR"

# Carrega eps calibrado num array associativo chaveado por "variante_d".
declare -A EPS_POR_CONFIG
while IFS=, read -r variante dimensao eps resto; do
    [ "$variante" = "variante" ] && continue  # pula cabeçalho
    EPS_POR_CONFIG["${variante}_${dimensao}"]="$eps"
done < "$EPS_CSV"

run() {
    local algoritmo=$1
    local params=$2
    local dataset_nome=$3
    local dataset_csv=$4

    local saida="$ASSIGNMENTS_DIR/${dataset_nome}_${algoritmo}.csv"
    if [ -f "$saida" ] && [ "${FORCE:-0}" != "1" ]; then
        echo ">> $algoritmo | $dataset_nome | já existe ($saida), pulando (use FORCE=1 para refazer)"
        return
    fi

    echo ">> $algoritmo | $dataset_nome | params=$params"
    java -jar "$JAR" \
        --algoritmo "$algoritmo" \
        --entrada "$dataset_csv" \
        --saida "$saida" \
        --threads "$THREADS" \
        --params "$params" \
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

    eps="${EPS_POR_CONFIG[${variante}_${d}]:-}"
    if [ -z "$eps" ]; then
        echo "Eps não calibrado para $dataset_nome — verifique $EPS_CSV." >&2
        exit 1
    fi

    run "kmeans-paralelo-s" "k=$K_MEANS" "$dataset_nome" "$dataset_csv"
    run "dbscan-paralelo-s" "eps=$eps;minPts=10" "$dataset_nome" "$dataset_csv"
done

echo "Concluído. Atribuições em $ASSIGNMENTS_DIR, tempos em $TIMING_CSV"
