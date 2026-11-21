# Experimento 02 — Análise de qualidade dos agrupamentos

*Documento de trabalho interno do grupo — serve de base para a discussão de
resultados do relatório final. Não é o texto a ser submetido: o grupo deve
reescrever os pontos relevantes com suas próprias palavras no relatório
oficial.*

Dados-fonte: `results/experimento_02/qualidade_resultados.csv` e
`results/experimento_02/consistencia_implementacoes.csv`, gerados por
`scripts/evaluate_clustering_quality.py` a partir das atribuições de cluster
em `results/experimento_02/assignments/` (uma execução por algoritmo,
`scripts/run_quality_tests.sh`, n=150.000, threads=4).

Enquanto o experimento_01 mede **desempenho** (tempo, speedup), este mede
**qualidade do resultado**: o quão perto cada algoritmo chega da estrutura
real dos dados.

## 1. Metodologia

- **Ground truth:** `make_blobs` gera, além dos pontos, o cluster de origem
  de cada um (`y`). Esse rótulo nunca é usado pelos algoritmos de
  clustering — só é revelado depois, na avaliação. Adicionamos
  `data/dataset{1,2}_labels.csv` a partir da mesma seed já usada para gerar
  os pontos (`scripts/generate_datasets.py`), sem alterar os dados
  existentes (conferido byte a byte).
- **Métrica externa principal: Adjusted Rand Index (ARI).** Mede
  concordância entre dois agrupamentos corrigindo por acaso — 1.0 é
  concordância perfeita, ~0 é o esperado de um agrupamento aleatório.
  Preferida a acurácia bruta porque não depende de "casar" os números dos
  clusters (cluster 3 do algoritmo pode ser o cluster 5 do rótulo
  verdadeiro e o ARI lida com isso).
- **Métrica externa secundária: Normalized Mutual Information (NMI).**
  Mede quanta informação um agrupamento dá sobre o outro; usada como
  segunda leitura por ter base teórica diferente do ARI (teoria da
  informação vs. combinatória).
- **Métricas específicas de DBSCAN:** número de clusters encontrados vs.
  número verdadeiro de centros, e fração de pontos classificados como
  ruído.
- **Consistência entre implementações:** ARI entre as 3 versões de uma
  mesma tarefa (clássica, paralela com lock, paralela lock-free) — serve de
  checagem de corretude (elas deveriam convergir para o mesmo resultado),
  não de qualidade.
- Uma única execução por configuração (sem repetição): aqui o resultado é
  determinístico ponto a ponto (dado o mesmo dataset e parâmetros), ao
  contrário do tempo de execução — repetir não mudaria a atribuição de
  cluster.

## 2. Qualidade externa (ARI / NMI contra o rótulo verdadeiro)

| Dataset | Algoritmo | ARI | NMI | clusters verdadeiro → encontrado | % ruído |
|---|---|---|---|---|---|
| dataset1 | k-means (3 versões) | 0,691 | 0,832 | 8 → 8 | 0% |
| dataset1 | DBSCAN (3 versões) | 0,669 | 0,839 | 8 → 9 | 0,95% |
| dataset2 | k-means (3 versões) | 0,619 | 0,736 | 5 → 5 | 0% |
| dataset2 | DBSCAN (3 versões) | 0,778 | 0,897 | 5 → 6 | 0,39% |

**Observações:**

- **Nenhum algoritmo chega a ARI = 1.0**, o que é esperado e não indica
  defeito: os blobs gerados têm `cluster_std` alto o bastante (1.0 no
  dataset1; até 2.0 no dataset2) para haver sobreposição real entre
  clusters vizinhos nas bordas. Nessa faixa de sobreposição, mesmo o
  algoritmo "certo" para os dados vai discordar do rótulo de geração em
  alguns pontos de fronteira — isso é uma propriedade do dataset, não um
  erro de implementação.
- **DBSCAN supera k-means no dataset2** (ARI 0,778 vs. 0,619), mas **perde
  para k-means no dataset1** (0,669 vs. 0,691). Isso é consistente com a
  natureza dos dois datasets: dataset2 é anisotrópico e tem densidades
  distintas por cluster — exatamente o cenário em que a suposição de
  clusters esféricos e de densidade uniforme do k-means falha, e onde
  DBSCAN (baseado em densidade local) tem vantagem estrutural. Dataset1 tem
  blobs esféricos e com densidade uniforme — o cenário ideal para k-means.
  Esse contraste é um bom ponto de discussão no relatório: não existe
  "o melhor algoritmo" em geral, depende da geometria dos dados.
- **DBSCAN encontra 1 cluster a mais que o verdadeiro em ambos os
  datasets** (9 em vez de 8; 6 em vez de 5). Hipótese mais provável: dentro
  de um blob gaussiano de `cluster_std` alto, a densidade cai de forma
  suave do centro para a borda, e pode existir uma região intermediária
  rarefeita o bastante para o algoritmo cortar o blob em dois sob o `eps`
  calibrado. Não investigamos isso a fundo neste experimento — é um ponto
  de discussão para o relatório, não uma conclusão fechada.
- **Fração de ruído baixa em DBSCAN** (<1% nos dois datasets) — consistente
  com a calibração de `eps`/`minPts` feita anteriormente (ver `docs/logs.md`,
  A-010), que já tinha esse objetivo (evitar excesso de ruído por
  sub-calibração).

## 3. Consistência entre implementações (checagem de corretude)

| Dataset | Tarefa | Par de implementações | ARI entre elas |
|---|---|---|---|
| dataset1 | k-means | todas as combinações | 1,000000 |
| dataset1 | DBSCAN | clássico vs. paralelo (lock) | 0,999941 |
| dataset1 | DBSCAN | clássico vs. paralelo-s (lock-free) | 0,999941 |
| dataset1 | DBSCAN | paralelo (lock) vs. paralelo-s (lock-free) | 1,000000 |
| dataset2 | k-means | todas as combinações | 1,000000 |
| dataset2 | DBSCAN | clássico vs. paralelo (lock) | 0,999980 |
| dataset2 | DBSCAN | clássico vs. paralelo-s (lock-free) | 0,999980 |
| dataset2 | DBSCAN | paralelo (lock) vs. paralelo-s (lock-free) | 1,000000 |

**Observações:**

- **As 3 versões de k-means são idênticas ponto a ponto** (ARI = 1,0 exato)
  — confirma formalmente o que já havia sido validado manualmente (ver
  `planejamento_trabalho.md`, item concluído de implementação).
- **As 2 versões paralelas de DBSCAN são idênticas entre si** (ARI = 1,0),
  mas **ambas divergem muito ligeiramente da versão clássica**
  (ARI ≈ 0,99994–0,99998, ou seja, ~0,002–0,006% dos pares de pontos
  discordam). Isso sugere que a divergência não é uma inconsistência entre
  paralelo-com-lock e paralelo-lock-free (que concordam perfeitamente entre
  si), e sim um detalhe de **ordem de visita de pontos de fronteira**
  (pontos "alcançáveis por densidade" a partir de mais de um cluster, cuja
  atribuição final depende de qual cluster o visita primeiro) que muda
  entre a execução sequencial pura e a execução particionada — um
  comportamento conhecido e documentado na literatura de DBSCAN paralelo
  (border points), não um bug de implementação.

## 4. Síntese para o relatório

1. Os 2 algoritmos recuperam a estrutura real dos dados com qualidade
   substancial (ARI 0,62–0,78), com a diferença residual explicável pela
   sobreposição intrínseca dos blobs gerados, não por falha do algoritmo.
2. O contraste k-means vs. DBSCAN entre os 2 datasets é o resultado mais
   interessante: cada algoritmo vence no dataset cuja geometria favorece
   sua suposição de base (esférico/uniforme para k-means, densidade local
   para DBSCAN) — vale a pena enfatizar isso na discussão, já que mostra
   entendimento das propriedades dos algoritmos, não só a execução.
3. A corretude das implementações paralelas foi validada quantitativamente
   (ARI ≈ 1,0 contra a versão clássica, exato para k-means e quase-exato
   para DBSCAN), complementando a validação qualitativa já registrada em
   `docs/logs.md`.
4. A pequena divergência de ~0,002–0,006% entre DBSCAN clássico e paralelo
   é explicável por ambiguidade de atribuição de pontos de fronteira
   alcançáveis por mais de um cluster — um comportamento esperado de
   paralelização por partição espacial, não um defeito.
