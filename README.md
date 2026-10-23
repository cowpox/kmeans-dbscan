# kmeans-dbscan

Trabalho do Semestre da disciplina Computação Escalável (UEL, prof. Larissa C.
Shimomura): implementação paralela de k-means e DBSCAN em Java, com análise
de escalabilidade (tempo de execução vs. tamanho do dataset).

## Integrantes e divisão de responsabilidades

| Dupla | Algoritmo |
|---|---|
| Herik Daurizio Ricardo, Julia Yokoyama Massaki | k-means |
| Adriano Lúcio Uchôa Brandão, Sofia Gutschow Casal | DBSCAN |

Tarefa: **Clustering**. Linguagem/tecnologia: **Java** (`ExecutorService`/`ForkJoin`, sem frameworks externos de distribuição).

## Requisitos

- JDK 17+
- Maven 3.8+
- Python 3.10+ (apenas para os scripts de geração de dataset e gráficos, em `scripts/`)

## Como compilar

```bash
mvn clean package
```

Gera `target/kmeans-dbscan.jar`, já executável (classe principal configurada no `pom.xml`).

## Interface de execução (comum aos dois algoritmos)

```bash
java -jar target/kmeans-dbscan.jar \
     --algoritmo kmeans|dbscan \
     --entrada caminho/para/dataset.csv \
     [--saida caminho/para/resultado.csv] \
     [--tamanho N] \
     [--threads N] \
     --params "k=8"                  # (kmeans) ou "eps=0.5;minPts=10"  (dbscan)
     [--dataset-nome dataset1] \
     [--resultados-csv results/scalability_results.csv]
```

- `--entrada`: CSV no formato `id,x1,x2,...,xn` (sem cabeçalho, ou com cabeçalho — é detectado automaticamente).
- `--tamanho`: limita a execução aos N primeiros pontos do dataset (usado nos testes de escalabilidade).
- `--threads`: número de threads/cores usados pelo algoritmo (default: núcleos disponíveis na máquina).
- `--saida`: se informado, grava `id,cluster` para cada ponto.
- `--resultados-csv`: se informado, acrescenta uma linha com o tempo de execução ao CSV de escalabilidade.

Exemplo de uma execução única:

```bash
java -jar target/kmeans-dbscan.jar \
     --algoritmo kmeans --entrada data/dataset1_full.csv --tamanho 50000 \
     --threads 4 --params "k=8" --dataset-nome dataset1 \
     --resultados-csv results/scalability_results.csv
```

**Medição de tempo:** apenas o tempo de execução do algoritmo em si (`algorithm.run(...)`) é cronometrado — a leitura do dataset e a inicialização da JVM/thread pool ficam de fora, conforme exigido pelas instruções da disciplina.

## Como gerar os datasets

```bash
pip install -r scripts/requirements.txt
python scripts/generate_datasets.py
```

Gera `data/dataset1_full.csv` (blobs gaussianos bem separados) e `data/dataset2_full.csv`
(blobs anisotrópicos com densidades diferentes), ambos com 150.000 pontos. Detalhes em `data/README.md`.

## Como rodar a suíte completa de testes de escalabilidade

```bash
mvn clean package
python scripts/generate_datasets.py
./scripts/run_scalability_tests.sh
python scripts/plot_results.py
```

O script de testes roda os dois algoritmos nos dois datasets, em 8 tamanhos
(10k, 30k, ..., 150k), e acumula os tempos em `results/scalability_results.csv`.
O script de plots gera `results/plots/dataset1_escalabilidade.png` e
`dataset2_escalabilidade.png` (cada um com as 2 séries: k-means vs. DBSCAN) — os
gráficos exigidos pelo relatório final.

Para ajustar o número de threads usado nos testes:

```bash
THREADS=8 ./scripts/run_scalability_tests.sh
```

## Estrutura do repositório

```
kmeans-dbscan/
├── pom.xml
├── src/main/java/br/uel/ce/clustering/
│   ├── Main.java                  # interface de execução comum (CLI)
│   ├── common/                    # DataPoint, leitura/escrita de arquivos, contrato ClusteringAlgorithm
│   ├── kmeans/KMeansClustering.java   # Herik, Julia
│   └── dbscan/DBSCANClustering.java   # Adriano, Sofia
├── scripts/
│   ├── generate_datasets.py       # gera os 2 datasets sintéticos
│   ├── run_scalability_tests.sh   # roda os testes de escalabilidade
│   └── plot_results.py            # gera os gráficos finais
├── data/                          # datasets gerados (não versionados)
├── results/                       # CSV de tempos + gráficos (não versionados)
├── docs/
│   ├── logs.md                    # log do projeto: estado, decisões, pendências, achados, linha do tempo
│   └── revisao-literatura/        # revisão de literatura: template, slots, fichas, .bib
│       ├── artigos/               # 22 PDFs + catálogo (README.md)
│       ├── fichamento_literatura_template.md
│       ├── fichamentos.md
│       ├── revisao_literatura.md
│       └── referencias.bib
└── relatorio/                     # relatório final (LaTeX), editado por todo o grupo
    ├── template_trabalho_semestre.tex
    └── logo_uel.png
```

## Declaração de uso de IA

- **Não utilizada** para a implementação dos algoritmos (`KMeansClustering.java`, `DBSCANClustering.java`) — feita manualmente pelas duplas responsáveis.
- **Utilizada** para: leitura/escrita de arquivos (`DatasetReader.java`, `ResultWriter.java`), a interface de execução/harness de linha de comando (`Main.java`), e os scripts de automação de testes e geração de gráficos (`scripts/generate_datasets.py`, `scripts/run_scalability_tests.sh`, `scripts/plot_results.py`), além do esqueleto inicial do repositório (estrutura de pastas, `pom.xml`, `.gitignore`, este README).
- Atualizar esta seção caso o uso de IA mude ao longo do desenvolvimento.

## Fundamentação teórica

Ver `docs/revisao-literatura/` (síntese em `revisao_literatura.md`, fichas em
`fichamentos.md`, bibliografia em `referencias.bib`) para a
leitura integral dos 22 artigos originais de k-means e DBSCAN e dos principais trabalhos sobre suas
versões paralelas/distribuídas. O catálogo dos PDFs está em `docs/revisao-literatura/artigos/README.md`.
O panorama inicial do grupo (`referencial_teorico.md`), escrito sem leitura integral, foi absorvido por essa
síntese e removido; as correções que a leitura trouxe a ele estão em `revisao_literatura.md` §8.

## Relatório final

O relatório é escrito em LaTeX em `relatorio/template_trabalho_semestre.tex`
(template oficial da disciplina, com `logo_uel.png`). Todo o grupo edita
esse arquivo diretamente no repositório — commitar incrementalmente ao
longo do desenvolvimento, não tudo de uma vez perto do prazo (12/11).
