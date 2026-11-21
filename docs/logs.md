# Log do Projeto — k-means e DBSCAN paralelos (Computação Escalável)

> Documento vivo. Registra, em ordem cronológica, o que foi feito, decidido e descoberto em cada dia do projeto.
> Serve de memória histórica para o grupo e de contexto de entrada para quem retomar o trabalho em outra sessão.

**Projeto:** implementação paralela de k-means e DBSCAN em Java, com análise de escalabilidade
**Instituição:** Departamento de Computação, UEL · **Disciplina:** Computação Escalável (prof.ª Larissa C. Shimomura)
**Mantido por:** Herik Daurizio Ricardo, Julia Yokoyama Massaki, Adriano Lúcio Uchôa Brandão, Sofia Gutschow Casal
**Última atualização:** 2026-11-21

---

## Como ler e manter este documento

**Ordem de leitura recomendada:** §1 Estado atual → §2 Decisões → §3 Pendências → §5 Linha do tempo (só as entradas relevantes à tarefa).

**Convenções:**

| Item | Regra |
|---|---|
| Datas | ISO `AAAA-MM-DD` + dia da semana abreviado. Horários em hora local (UTC−3). |
| IDs | Decisões `D-NNN`, pendências `P-NNN`, achados `A-NNN`. IDs nunca são reutilizados nem renumerados. |
| Tipos de entrada | `reunião` · `comunicação` · `estudo` · `implementação` · `experimento` · `organização` · `ferramenta` |
| Grau de certeza | Fatos relatados por participantes ficam sem marca. Inferências (não relatadas por participantes) são marcadas com **(inferência)**. Datas ou fatos duvidosos com **(a confirmar)**. |
| Entradas passadas | Não reescrever. Correções entram como nota `> Correção (AAAA-MM-DD): …` na própria entrada. |
| Pendências concluídas | Permanecem na tabela, com status `Concluída (AAAA-MM-DD)` e o desfecho. |
| Caminhos de arquivo | Entradas antigas citam os caminhos da época. Se um caminho não existir mais, consultar a §6 (mapa da pasta) ou o histórico do git. |

**Ao final de cada iteração:** (1) acrescentar a entrada do dia em §5; (2) atualizar §1; (3) registrar decisões novas em §2 e pendências em §3; (4) atualizar a data de "Última atualização".

---

## 1. Estado atual (snapshot em 2026-11-21)

| Dimensão | Situação |
|---|---|
| **Fase** | Implementação e validação dos 6 algoritmos concluídas; 5 experimentos rodados e documentados (escalabilidade/speedup, qualidade de agrupamento, dimensionalidade ampla, dimensionalidade fina, speedup × dimensionalidade); próxima frente é escrever o relatório final |
| **Frente ativa** | Experimento_05 (speedup × dimensionalidade) concluído e documentado hoje (ver A-012, `docs/analise_experimento_05.md`); próximo passo é consolidar os 5 experimentos no relatório (P-008) |
| **Próximo marco** | Relatório final — prazo **30/11/2026** (atraso: -10%/dia); apresentação final — **04/12/2026** (datas alteradas em 2026-11-10, ver D-007) |
| **Bloqueio principal** | Nenhum bloqueio técnico; caminho crítico agora é escrever o relatório (P-008) e a apresentação (P-009) a partir do material já pronto em `docs/analise_experimento_0{1..5}.md` |
| **Divisão de responsabilidades** | Herik e Julia → k-means; Adriano e Sofia → DBSCAN |
| **Revisão de literatura** | Concluída — 23 artigos fichados em 8 slots temáticos (KM1–KM4 para k-means, DB1–DB4 para DBSCAN), cada slot com revisão de consistência própria |
| **Implementação (k-means)** | Três implementações: `KMeansClassico.java` (baseline sequencial, Julia), `KMeansClustering.java` (paralelo, redução sequencial das somas parciais, Herik) e `KMeansClusteringS.java` (paralelo, redução paralela das somas parciais, Herik). Saídas idênticas entre as três em `blobs_d10_k5.csv` (k=5, seed=123, 1/4/8 threads, duas máquinas — ver A-009). Todas usam `seed=123` fixa por padrão se `seed` não for passado em `--params` — relevante para qualquer experimento de qualidade (ver A-011) |
| **Implementação (DBSCAN)** | Três implementações, todas validadas ponto a ponto no dataset `demo_moons_circles.csv` (425 pontos), sem divergência de rótulo entre elas: `DBSCANClassico.java` (baseline sequencial, Adriano), `DBSCANClustering.java` (paralelo com lock global, Sofia, ver A-007) e `DBSCANClusteringS.java` (paralelo com merge lock-free via CAS, Adriano, ver A-008). Comentários concluídos nas 3 (P-015). Custo cresce ~O(n²·d) (força bruta na busca de vizinhos) — muito mais sensível a `d` do que o k-means (ver A-012) |
| **Datasets e parâmetros oficiais** | Decididos: os sintéticos de `generate_datasets.py` (dataset1 = 8 blobs bem separados; dataset2 = 5 blobs anisotrópicos), autorizados pela professora. `run_scalability_tests.sh` recalibrado por dataset (`k`, `eps`, `minPts=10`) em 2026-11-20 — ver D-012, A-010 |
| **Experimentos (01-05)** | Todos rodados e documentados em `docs/analise_experimento_0{1..5}.md` — ver linha do tempo (§5) e D-013, D-014, A-011, A-012 |
| **Relatório** | Template LaTeX pronto (`relatorio/template_trabalho_semestre.tex`), conteúdo ainda não escrito — material de análise já pronto nos 5 documentos de experimento |
| **Estrutura de pastas** | Ver §6 (mapa da pasta) |
| **Ferramentas** | Java 17 + Maven (algoritmos); Python (scripts de dataset/teste/gráfico); Claude Code via terminal do IntelliJ para setup, scripts e documentação — **não** para a lógica paralela dos algoritmos (proibido pela disciplina) |

---

## 2. Decisões

| ID | Data | Decisão | Quem decidiu | Status |
|---|---|---|---|---|
| D-001 | 2026-10-21 | Tarefa escolhida: Clustering, com os algoritmos k-means e DBSCAN. Confirmado na planilha do Classroom (Grupo 3, status "Fechado", sem conflito com outros grupos da turma). | Grupo todo | Confirmada |
| D-002 | 2026-10-21 | Duplas definidas: Herik e Julia → k-means; Adriano e Sofia → DBSCAN. | Grupo todo | Confirmada |
| D-003 | 2026-10-21 | Linguagem/tecnologia: Java (Threads/ExecutorService/ForkJoin), descartando Scala/Spark. Motivo: DBSCAN paralelo já é difícil em qualquer linguagem; Java evita overhead de setup de Spark/Docker e dá medição de tempo mais limpa; nenhum trabalho anterior do grupo usou Java/Scala. | Grupo todo | Confirmada |
| D-004 | 2026-10-21 | Repositório renomeado de `clustering-escalabilidade` para `kmeans-dbscan` (artifactId, nome do jar e documentação). | Adriano | Confirmada |
| D-005 | 2026-10-21 | `relatorio/` e `artigos/` (referencial teórico) movidos para dentro do repositório `kmeans-dbscan` (antes viviam soltos em `Trabalho_final/`), para que o grupo edite o relatório colaborativamente via git. | Adriano | Confirmada |
| D-006 | 2026-10-22 | Nas inconsistências internas dos artigos (texto contra tabela ou figura), vale o dado observado, e a divergência fica registrada em `docs/revisao-literatura/revisao_literatura.md` §9. | Herik | Confirmada |
| D-007 | 2026-11-10 | Prazos da disciplina alterados: relatório final adiado de 12/11/2026 para **30/11/2026** (mantém atraso de -10%/dia); apresentação final adiada de 13/11/2026 para **04/12/2026**. | Professora (comunicado à turma) | Confirmada |
| D-008 | 2026-11-10 | Criada `dbscan/DBSCANClassico.java` (stub), versão sequencial/1-núcleo do DBSCAN, usada como baseline de corretude e de speedup — não entra nos 2 gráficos oficiais (k-means vs. DBSCAN paralelo). Ao rodar os testes, usar `--resultados-csv` separado para o baseline (ex.: `results/baseline_results.csv`), diferente do CSV oficial (`results/scalability_results.csv`), para não poluir `plot_results.py` com uma 3ª série. | Adriano (sessão com Claude Code) | Confirmada |
| D-009 | 2026-11-10 | Convenção de rótulos do DBSCAN (`DBSCANClustering` e `DBSCANClassico`, mesma em ambos): `-1` = marcação interna de "não visitado" (estado inicial, não deve sobrar no resultado final); `-2` = rótulo final de ruído; `0, 1, 2, ...` = clusters. Difere do sklearn (que usa `-1` para ruído) — ao validar a corretude do Java contra o notebook de referência, mapear `-1 (sklearn) ↔ -2 (java)` antes de comparar. | Adriano | Confirmada |
| D-012 | 2026-11-20 | Datasets oficiais de escalabilidade: manter os sintéticos já prontos em `scripts/generate_datasets.py` (dataset1 = 8 blobs gaussianos bem separados; dataset2 = 5 blobs anisotrópicos com densidades diferentes), em vez de buscar um dataset real (candidatos em A-003), dado o prazo até 30/11. A professora autorizou o uso de datasets gerados pelo scikit-learn. Parâmetros de `run_scalability_tests.sh` recalibrados por dataset (ver A-010): k-means com `k` = número de centros verdadeiros de cada dataset (8 e 5, antes fixo em 8 para os dois); DBSCAN com `eps` calibrado por dataset (0.2 e 0.15, antes fixo em 0.5 para os dois), `minPts=10` mantido. | Adriano (sessão com Claude Code) | Confirmada |
| D-015 | 2026-11-21 | Dentro de `results/experimento_*/`, só os CSVs de "assignments" (rótulo por ponto — intermediários pesados: ~60MB entre exp_02 e exp_03, só usados pelos scripts de avaliação, sem valor de leitura direta) ficam fora do git (`.gitignore`). Os CSVs de resumo/agregado (ex. `qualidade_por_dimensao.csv`, `eps_calibrado.csv`, `speedup_dim_agregado.csv` — ~137KB no total) e os PNGs de `plots/` são commitados: são a evidência numérica e visual por trás das tabelas/figuras dos `docs/analise_experimento_0X.md`, sem eles os números dos docs não seriam verificáveis nem os links de imagem funcionariam no GitHub. | Adriano (sessão com Claude Code) | Confirmada |
| D-013 | 2026-11-20 (a confirmar) | Metodologia padrão para experimentos de tempo de execução: cada configuração roda 5 vezes, agregada em mediana/min/max/desvio (`scripts/aggregate_results.py`, novo); os gráficos usam a mediana com barra de erro min/max. Adotada a partir do experimento_01 e repetida em todos os experimentos seguintes que medem tempo. | Adriano (sessão com Claude Code) | Confirmada |
| D-014 | 2026-11-20 (a confirmar) | Qualquer experimento que avalie **qualidade** de agrupamento do k-means (ARI/NMI contra rótulo verdadeiro, não tempo de execução) deve variar explicitamente `seed` em `--params` (ex. `seed=1..5`) e reportar mediana/min/max, nunca uma única execução. Motivo: as 3 implementações de k-means usam `seed=123` fixa por padrão quando `seed` não é passado, e a convergência é bimodal (ótimo global vs. mínimo local específico) a partir de certas dimensões — ver A-011. DBSCAN não precisa dessa prática (não tem seed, é determinístico). | Adriano (sessão com Claude Code) | Confirmada |
| D-011 | 2026-11-16 | Manter duas versões do DBSCAN paralelo lado a lado: `DBSCANClustering.java` (Sofia — `synchronized unir()`, lock global por união) e `DBSCANClusteringS.java` (Adriano — Union-Find local por partição + merge entre partições via CAS/`AtomicIntegerArray`, sem lock, inspirado na otimização de merge do PDSDBSCAN-S de Patwary et al. 2012). `getName()` = `"dbscan-paralelo-s"`, ligada ao `Main.java` junto das outras três. Ambas ficam no relatório como comparação de estratégias de sincronização (lock vs. lock-free), não como substituição uma da outra. | Adriano | Confirmada |
| D-010 | 2026-11-10 | Construir uma animação interativa do DBSCAN (passo a passo, HTML autocontido/Canvas 2D/JS puro, publicada como artifact privado), reaproveitando a estrutura de um artifact anterior ("DBSCAN em movimento") já feito para outra disciplina/estudo. Fase 1 (agora): versão só do DBSCAN clássico, usando o dataset real de `data/demo_moons_circles.csv` (não um dataset genérico sorteado) e a convenção de rótulos de D-009. Fase 2 (depois que `DBSCANClustering` — versão paralela — estiver pronta): versão lado a lado clássico vs. paralelo, mostrando particionamento entre threads e fusão de clusters nas fronteiras (union-find) — o ponto central do relatório. | Adriano | Confirmada (fase 1 em andamento; `DBSCANClustering` já está pronta desde 2026-11-15, então a fase 2 pode começar — ver P-012) |

---

## 3. Pendências

| ID | Aberta em | Pendência | Responsável | Status |
|---|---|---|---|---|
| P-001 | 2026-10-21 | Criar repositório remoto no GitHub e dar push | Adriano | Concluída (confirmado em 2026-10-23: `origin` já aponta para `github.com/cowpox/kmeans-dbscan`, branch sincronizado) |
| P-002 | 2026-10-21 | Preencher formulário inicial do Classroom (grupo, tarefa, algoritmos, duplas, link do repositório) — prazo 25/10/2026 | Grupo todo | Concluída (Herik preencheu e entregou dentro do prazo; não estava registrado neste log até 2026-11-20) |
| P-003 | 2026-10-21 | Escolher/gerar os 2 datasets (10.000–150.000 pontos; se sintéticos, com distribuições diferentes) | Grupo todo | Concluída (2026-11-20: mantidos os sintéticos de `generate_datasets.py`, autorizados pela professora — ver D-012) |
| P-004 | 2026-10-21 | Definir formato de saída comum entre os dois algoritmos (além da interface já criada em `common/`) | Grupo todo | Aberta |
| P-005 | 2026-10-21 | Implementar k-means paralelo | Herik e Julia | Aberta (primeira versão escrita e compilando em 2026-11-11; falta executar e validar) |
| P-013 | 2026-11-11 | Implementar `KMeansClassico.java` (baseline sequencial, hoje só esqueleto) e usá-lo para validar a corretude e o speedup do k-means paralelo | Herik e Julia | Em andamento (`KMeansClassico.java` implementado por Julia em 2026-11-14; falta a comparação formal ponto a ponto entre clássico e paralelo e a medição de speedup — ver P-014) |
| P-006 | 2026-10-21 | Implementar DBSCAN paralelo | Adriano e Sofia | Concluída (2026-11-15: Sofia implementou `DBSCANClustering.java`; validado ponto a ponto contra o clássico em 2026-11-16 — ver A-007) |
| P-011 | 2026-11-10 | Implementar e fechar a lógica da `DBSCANClassico.java` (baseline sequencial) antes de iniciar a versão paralela | Adriano | Concluída (2026-11-10) |
| P-014 | 2026-11-16 | Validar corretude ponto a ponto do k-means paralelo contra `KMeansClassico.java` (hoje só confirmado que ambos rodam e retornam o mesmo número de clusters, sem comparar rótulo a rótulo) e medir o speedup | Herik e Julia | Concluída (2026-11-17: corretude ponto a ponto confirmada em `blobs_d10_k5.csv`, seed=123 — ver A-009; speedup medido no experimento_01 e no experimento_05 — ver D-013; variância entre seeds investigada no experimento_03/04 — ver A-011) |
| P-007 | 2026-10-21 | Rodar testes de escalabilidade e gerar os 2 gráficos finais | Grupo todo | Concluída (2026-11-20, a confirmar: experimento_01 rodado com 5 repetições, agregado e documentado em `docs/analise_experimento_01.md` — ver D-013) |
| P-008 | 2026-10-21 | Escrever relatório final (template em `relatorio/template_trabalho_semestre.tex`) — prazo **30/11/2026** (atraso: -10%/dia; data alterada em 2026-11-10, era 12/11/2026 — ver D-007) | Grupo todo | Aberta |
| P-009 | 2026-10-21 | Preparar apresentação final (~20 min, ~5 min por pessoa) — **04/12/2026** (data alterada em 2026-11-10, era 13/11/2026 — ver D-007) | Grupo todo | Aberta |
| P-010 | 2026-10-23 | Decidir o par de datasets (candidatos em A-003) e confirmar com a professora se uma visualização didática do funcionamento paralelo é permitida | Grupo todo | Concluída (2026-11-20: professora autorizou datasets sintéticos do scikit-learn; grupo optou pelos sintéticos já prontos em vez de dataset real, dado o prazo apertado — ver D-012). A pergunta sobre a visualização didática segue sem resposta registrada |
| P-012 | 2026-11-10 | Construir a animação interativa do DBSCAN (ver D-010) — fase 1 (clássico) em andamento; fase 2 (lado a lado com o paralelo) depende de `DBSCANClustering` estar pronta (ver P-006) | Adriano | Aberta (fase 1 em andamento; `DBSCANClustering` ficou pronta em 2026-11-15, fase 2 pode começar) |
| P-015 | 2026-11-16 | Comentar as 3 implementações de DBSCAN (`DBSCANClassico`, `DBSCANClustering`, `DBSCANClusteringS`) — hoje com pouco ou nenhum comentário explicando o porquê das decisões de sincronização/estrutura de dados, importante para o relatório | Adriano e Sofia | Concluída (2026-11-16: Sofia comentou `DBSCANClustering.java`; Adriano comentou `DBSCANClassico.java` e `DBSCANClusteringS.java`) |
| P-016 | 2026-11-21 | Commitar e dar push em todo o trabalho dos experimentos 01-05 (scripts, docs, dados gerados, resultados) — ainda não commitado no repositório no momento desta entrada (`git status` mostra `docs/logs.md`, scripts modificados e todos os `docs/analise_experimento_0{1..5}.md` como untracked/modificados) | Adriano | Concluída (2026-11-21: commit `ff7bec2` com scripts, docs e PNGs; commit seguinte com os CSVs de resumo/agregado — ver D-015. Falta só o `git push`) |

---

## 4. Achados

Descobertas e observações que mudam ou informam o rumo do projeto.

| ID | Data | Achado | Implicação |
|---|---|---|---|
| A-001 | 2026-10-23 | A revisão de literatura foi organizada em 8 slots temáticos (KM1–KM4 para k-means, DB1–DB4 para DBSCAN), cobrindo os 23 artigos do referencial teórico em `docs/revisao-literatura/artigos/`, cada slot com fichamento (`fichamentos.md`) e revisão de consistência própria em `revisao_literatura.md`. | Base teórica para o relatório final já está estruturada antes mesmo de a implementação começar — ao escrever o relatório, usar os slots como esqueleto das seções de trabalhos relacionados. |
| A-002 | 2026-10-22 | Nenhum dos 22 artigos mede *threads* Java em uma só máquina com n ≤ 150 mil pontos. A evidência mais próxima é Patwary et al. (memória compartilhada) e Wang et al. (speedup contra a melhor serial). Vários artigos reportam só speedup self-relative, e os valores de artigos diferentes não são comparáveis. | O relatório deve declarar a base de cada speedup e discutir o custo da fusão e o desbalanceamento de carga. |
| A-003 | 2026-10-23 | O 3D Road Network (UCI) em lat/lon é 2-D e permite uma demonstração visual: os clusters atravessam as partições, o que mostra a fusão do DBSCAN. Seu uso em k-means está na literatura, mas não encontrei uso em DBSCAN. Para o DBSCAN, a literatura lida usa Household, GeoLife e Cosmo50. | Serve para o gráfico e para a demonstração. Se o grupo quiser respaldo da literatura de DBSCAN, o Household é o candidato. |
| A-004 | 2026-11-10 | `DBSCANClassico.regionQuery` usa `List<Integer>` (boxing) para guardar os vizinhos — simples de escrever, mas em O(n²) chamadas (até 150k pontos) o boxing e a falta de localidade de cache geram alocação extra e pressão de GC, o que pode introduzir ruído na medição de tempo usada para os gráficos de escalabilidade. Alternativas avaliadas: (B) array primitivo dinâmico próprio (sem boxing, mais código pra escrever à mão); (C) buffer `int[n]` pré-alocado e reaproveitado entre chamadas (mais rápido ainda, mas introduz estado mutável que exigiria um buffer por thread na versão paralela). | Manter `List<Integer>` por ora (já funciona); migrar para (B) só se o tempo de execução em 150k pontos for inviável ou os gráficos ficarem ruidosos. Qualquer que seja a escolha, usar a MESMA estrutura em `DBSCANClustering` (paralela) para não contaminar a comparação clássico vs. paralelo com overhead de boxing/alocação em vez de paralelismo puro. |
| A-005 | 2026-11-10 | `DBSCANClassico` validado visualmente em `data/demo_moons_circles.csv` (425 pontos: 2 luas entrelaçadas + 2 círculos concêntricos + 25 pontos de ruído, gerado por Adriano em notebook Python próprio). Com `eps=0.2;minPts=5`, encontrou exatamente os 4 clusters esperados (as 2 luas separadas corretamente apesar de não-convexas; os 2 círculos concêntricos separados em interno/externo) e 15 pontos de ruído — ver `results/plots/demo_moons_circles_dbscan.png`. | Confirma que a lógica do DBSCAN clássico está correta também num caso não-trivial (não só no `teste.csv` de 3 pontos), inclusive no cenário que melhor demonstra a vantagem do DBSCAN sobre k-means (clusters não-convexos). Esse dataset é um bom candidato de figura para o relatório/apresentação, mesmo não sendo um dos 2 datasets oficiais de escalabilidade (muito menor que o mínimo de 10k pontos exigido). |
| A-007 | 2026-11-16 | `DBSCANClustering.java` (paralelo, Sofia) foi revisado e testado contra `DBSCANClassico.java` no mesmo dataset (`demo_moons_circles.csv`, `eps=0.2;minPts=5`, 1 thread no clássico vs. 4 threads no paralelo): mesmo total de clusters (4) e de ruído (15); comparação ponto a ponto (425 pontos) não encontrou nenhuma divergência de ruído/não-ruído, e cada cluster do clássico mapeia para exatamente um cluster do paralelo (sem pontos trocando de cluster entre versões). A lógica paralela usa detecção de núcleos particionada por thread, uma fase de Union-Find (sincronizada em `unir`, sem race condition) para unir núcleos transitivamente — o que modela corretamente cadeias de densidade sem exigir que todos os núcleos de um cluster estejam diretamente a `eps` um do outro — e atribuição de pontos de borda ao primeiro núcleo encontrado (ambíguo por natureza do DBSCAN, mas consistente com o clássico). | `DBSCANClustering.java` pode ser considerado correto para os casos testados; falta validar em escala (10k–150k pontos, os datasets oficiais) e medir o speedup real — isso ainda depende de P-003/P-010 (escolha e geração dos datasets oficiais). |
| A-008 | 2026-11-16 | Adriano implementou `DBSCANClusteringS.java`, uma otimização do DBSCAN paralelo baseada na estratégia de merge do PDSDBSCAN-S (Patwary et al. 2012): Union-Find local por partição de thread (sem sincronização, porque cada thread só toca índices da própria partição) e, só para as arestas que cruzam partições, um merge lock-free via CAS sobre `AtomicIntegerArray` (invariante "raiz de índice menor aponta pra raiz de índice maior", que impede ciclos sem precisar de lock). Revisão de código não encontrou problemas de concorrência. Testado contra `DBSCANClassico` no dataset `demo_moons_circles.csv` com 1, 2, 4, 8 e 16 threads, repetindo a execução com 16 threads 5 vezes para forçar contenção no CAS e expor race condition intermitente: em todos os casos, mesmo total de clusters/ruído e zero divergência ponto a ponto. | Terceira implementação de DBSCAN do grupo, correta e validada. Difere de `DBSCANClustering.java` (Sofia) por trocar o lock global (`synchronized unir()`) por uma estrutura sem lock — bom contraste para o relatório (lock vs. lock-free) e para a seção de escalabilidade (o lock global pode virar gargalo com mais threads/pontos, o que só aparece nos testes em escala, ver P-007). Ainda falta comentar o código (ver P-015) e medir desempenho nos datasets oficiais. |
| A-009 | 2026-11-17 | As três implementações de k-means (`kmeans-classico`, `kmeans` e `kmeans-paralelo-s`) geraram arquivos de saída idênticos (mesmo hash MD5, `67EDDADBEFEF5FB66CA4CFF3B9484FCA`) em `data/blobs_d10_k5.csv` (150.000 pontos, 10 dimensões, gerado pelo scikit-learn), com `k=5;seed=123`, e 1, 4 e 8 threads nas versões paralelas. O resultado foi o mesmo nas máquinas de Herik e de Julia. O `KMeansClusteringS` difere do `KMeansClustering` só na redução: as somas parciais são reduzidas em paralelo, com o vetor de k·d posições dividido em fatias entre as threads e a soma feita sempre na ordem das faixas. | Corretude confirmada para um dataset, uma seed e um k, em um caso de clusters bem separados. Ordens de soma diferentes em ponto flutuante podem, em outros casos, mudar o rótulo de um ponto quase equidistante de dois centróides; se ocorrer, comparar SSE e ARI em vez de exigir igualdade exata. Esse dataset não é versionado (`data/*.csv` está no `.gitignore`): cada um precisa ter o mesmo arquivo, e o hash do CSV de entrada deve ser conferido antes de comparar saídas. A medição de speedup continua pendente. |
| A-010 | 2026-11-20 | `eps=0.5` (valor original de `run_scalability_tests.sh`, sem registro de como foi escolhido) estava muito acima da escala real dos dois datasets: em n=150.000, a distância média ao 10º vizinho é ≈0,056 (dataset1) e ≈0,024 (dataset2) — 10 a 20× menor que 0,5 — e a menor distância entre centróides verdadeiros é 0,73 (dataset1) e 1,54 (dataset2). Testado com `sklearn.cluster.DBSCAN` (uso de IA permitido para calibração de script de teste) em grade de valores de `eps` em n=150.000: `eps=0,5` produzia só 3 clusters (dataset1, verdade=8) e 2 clusters (dataset2, verdade=5), além de rodar 10 a 15× mais devagar (151s vs. ~10s em dataset2) porque quase todo ponto vira núcleo. `eps=0,2` (dataset1) deu 9 clusters/0,9% ruído; `eps=0,15` (dataset2) deu 6 clusters/0,4% ruído — ambos próximos da verdade com baixo ruído. Também corrigido o `k` do k-means em `run_scalability_tests.sh`, que usava `k=8` fixo para os dois datasets mesmo o dataset2 tendo só 5 centros verdadeiros. | Os parâmetros antigos (nunca executados nos testes oficiais, que ainda não tinham rodado) teriam produzido resultados sem sentido e mascarado a escalabilidade real do DBSCAN (menos trabalho por `regionQuery` do que o esperado para "quase todo mundo é vizinho de todo mundo"). Os novos valores (ver D-012) ficam documentados em comentário no próprio `run_scalability_tests.sh`. Como os testes amostram os primeiros N pontos do arquivo de 150k (sem regenerar por tamanho), a densidade cai para N menor (10k, 30k) com `eps` fixo calibrado no tamanho máximo — espera-se mais ruído proporcional nesses tamanhos menores; isso é comportamento esperado do DBSCAN, não um bug, mas vale uma nota no relatório. |
| A-011 | 2026-11-20 (a confirmar) | Os experimentos de dimensionalidade (03 e 04) mostraram, numa primeira leitura com seed fixa (`seed=123`, o default de todas as 3 implementações de k-means quando não passada em `--params`), quedas isoladas de ARI em pontos específicos (d=50 no experimento_03, d=9 no experimento_04) entre vizinhos de ARI≈1,0 — inicialmente interpretadas como possível reversão por concentração de distâncias ou execução "azarada" isolada. Repetindo as mesmas configurações com 5 seeds diferentes (`scripts/run_dim_kmeans_seeds.sh`, `evaluate_kmeans_seed_variance.py`), ficou claro que não é nem um efeito de dimensão nem um evento raro: a partir de d≈4-5, o k-means nesses dados converge de forma **bimodal** — ou para o ótimo global (ARI=1,000) ou para um mínimo local específico e quase invariante em d (ARI≈0,8347, variando <0,001 entre d=4 e d=1000) — dependendo só da seed de inicialização dos centróides, em ~1 a cada 3-5 execuções. DBSCAN não tem esse problema (determinístico, sem seed). | Qualquer leitura de qualidade do k-means baseada em uma única execução (seed fixa) não é representativa — ver D-014 (variar seed obrigatoriamente em experimentos de qualidade). `docs/analise_experimento_03.md` (§3.3) e `docs/analise_experimento_04.md` (§3.2-3.3) foram corrigidos para refletir essa explicação, substituindo a hipótese anterior (dimensionalidade/azar). |
| A-012 | 2026-11-21 | No experimento_05 (speedup × dimensionalidade), uma medição isolada do `dbscan-classico` em d=50/n=150.000 (1 thread) levou **22 minutos**, contra ~2 minutos em d=2 — confirma que o custo do DBSCAN (busca de vizinhos força-bruta) cresce ~O(n²·d), muito mais sensível a `d` do que o k-means (~O(n·k·d), linear em d: só ~5,2× mais lento entre d=2 e d=50, contra ~10,2× no DBSCAN). Isso tornou o plano original (d=2/50/500 em n=150.000 para os 6 algoritmos) inviável — d=500 foi removido e o DBSCAN passou a usar n=25.000 (k-means manteve n=150.000) nesta rodada. Com a bateria completa (2 dimensões × 5 repetições × 22 configurações), o speedup a 16 threads aumentou com `d` em todos os algoritmos — pouco no k-means (~4,8×→6,1×) e muito no DBSCAN, especialmente `dbscan-paralelo-s` (~10,2×→**38,0×**, speedup superlinear, acima do número de threads). | Confirma a hipótese de Amdahl (mais trabalho por ponto reduz o peso relativo do overhead de sincronização). O speedup superlinear sugere também um efeito de cache/localidade de memória (o baseline sequencial processando mais dimensões por ponto cabe pior em cache do que a versão particionada entre threads) — hipótese registrada em `docs/analise_experimento_05.md` §4.3, não verificada por profiling nesta rodada. Relevante para o relatório tanto na seção de escalabilidade quanto na de complexidade assintótica dos dois algoritmos. |
| A-006 | 2026-11-10 | A animação interativa (D-010, `https://claude.ai/artifact/PSacE1yM9mSt9oWBtDj3pE`) **não executa** `DBSCANClassico.java` — artifacts rodam isolados no navegador e não conseguem chamar a JVM/o `.jar` nem buscar arquivos externos (`data/*.csv`). É uma reimplementação em JavaScript que espelha manualmente o mesmo fluxo e a mesma convenção de rótulos do arquivo Java, com os 425 pontos do dataset embutidos como array fixo (congelado no momento da publicação). | Duas consequências: (1) a implementação entregue continua 100% do grupo, sem IA — a animação é só recurso visual/didático, não o artefato avaliado; (2) tanto a lógica quanto o dataset da animação são cópias estáticas — se `DBSCANClassico.java` mudar (ex.: a estrutura de dados do `regionQuery`, A-004) ou o dataset for regerado, a página só reflete isso depois de eu replicar a mudança e republicar manualmente (`edit_version`). A animação é pensada para datasets pequenos e demonstrativos (algumas centenas de pontos); os datasets oficiais de escalabilidade (10k–150k pontos) não cabem nesse formato passo a passo — para esses, o que importa é `plot_results.py`, não a animação. |

---

## 5. Linha do tempo

Modelo de entrada (copiar, preencher e colocar abaixo, da mais antiga para a mais recente):

```markdown
### AAAA-MM-DD (dia) — Título curto da entrada

- **Tipo:** reunião · comunicação · estudo · implementação · experimento · organização · ferramenta
- **Participantes:** …
- **O que aconteceu:** …
- **Decisões:** D-NNN
- **Achados:** A-NNN
- **Pendências geradas:** P-NNN
```

### 2026-10-21 (qua) — Definição de grupo, tarefa, duplas e linguagem; estrutura inicial do repositório

- **Tipo:** organização
- **Participantes:** Adriano, Herik, Julia, Sofia
- **O que aconteceu:** confirmada a tarefa (Clustering, k-means + DBSCAN) na planilha do Classroom; definidas as duplas (Herik e Julia → k-means; Adriano e Sofia → DBSCAN); decidida a linguagem Java (Threads/ExecutorService/ForkJoin) em vez de Scala/Spark; estrutura inicial do repositório `kmeans-dbscan` criada (Maven, `Main.java`, `common/`, stubs vazios de `kmeans/` e `dbscan/`, scripts de dataset/teste/gráfico); `relatorio/` e `artigos/` movidos para dentro do repositório; artigos de referência adicionados por todos os membros (k-means, k-means++, MapReduce, MLlib, DBSCAN — 18 dos 23 artigos atuais).
- **Decisões:** D-001, D-002, D-003, D-004, D-005
- **Pendências geradas:** P-002, P-003, P-004, P-005, P-006, P-007, P-008, P-009

### 2026-10-22 (qui) — Leitura e fichamento dos 22 artigos de k-means e DBSCAN

- **Tipo:** estudo
- **Participantes:** Herik
- **O que aconteceu:** montei a estrutura da revisão de literatura (molde de fichamento, catálogo dos PDFs, `fichamentos.md`, `revisao_literatura.md`, `referencias.bib`). Conferi os 22 PDFs no cabeçalho e no Crossref, e todos são os artigos esperados. Han et al. é de 2016 (a lista inicial dizia 2018), e Sculley, Ene et al., Schubert et al. e He et al. 2014 ficaram no repositório mesmo fora da lista. Li todos por inteiro e fichei em 8 slots (KM1–KM4 e DB1–DB4), com 191 citações literais conferidas no texto dos PDFs. Onde o texto de um artigo diverge de uma tabela ou figura, adotei o dado observado e registrei as 39 divergências em `revisao_literatura.md` §9. As afirmações do referencial teórico original que os PDFs não sustentam foram corrigidas e ficaram no changelog da revisão.
- **Decisões:** D-006
- **Achados:** A-002

### 2026-10-22 (qui) — Catálogo e organização da revisão de literatura (DBSCAN)

- **Tipo:** estudo
- **Participantes:** Herik
- **O que aconteceu:** catálogo de artigos sobre DBSCAN organizado e referências ajustadas; pasta antiga de "referencial teórico" (fora do repositório) removida, com o conteúdo já consolidado dentro de `docs/revisao-literatura/`.

### 2026-10-23 (sex) — Fichamentos e revisão de literatura concluídos; repositório remoto confirmado; `logs.md` preenchido

- **Tipo:** estudo · ferramenta
- **Participantes:** Adriano (sessão com Claude Code, terminal do IntelliJ)
- **O que aconteceu:** `fichamentos.md` e `revisao_literatura.md` atualizados com os artigos de DBSCAN, completando os 8 slots temáticos (KM1–KM4, DB1–DB4), cada um com revisão de consistência; confirmado que o repositório já tem remoto configurado no GitHub (`origin/master` sincronizado, sem pendência de push); este documento (`logs.md`) preenchido pela primeira vez a partir do histórico de commits e do contexto já levantado com o grupo.
- **Achados:** A-001
- **Pendências geradas:** P-001 marcada como concluída (push ao GitHub já tinha sido feito antes desta sessão, mas não estava registrado)

### 2026-10-23 (sex) — Pesquisa de datasets para os dois algoritmos

- **Tipo:** estudo
- **Participantes:** Herik
- **O que aconteceu:** pesquisei datasets que atendem ao enunciado (10k a 150k pontos; cada dataset roda nos dois algoritmos, com um gráfico por dataset e duas linhas). Os candidatos reais são 3D Road Network e Household (UCI), Skin Segmentation (UCI), Birch2, Worms e Europe (Fränti), além dos sintéticos do scikit-learn (blobs e moons). Os pares mais promissores são blobs (já gerado pelo repositório) com 3D Road Network em lat/lon, e blobs com moons como opção sem downloads.
- **Achados:** A-003
- **Pendências geradas:** P-010

### 2026-11-10 (ter) — Prazos de relatório e apresentação adiados

- **Tipo:** comunicação
- **Participantes:** Adriano (sessão com Claude Code)
- **O que aconteceu:** a professora alterou os prazos: relatório final de 12/11/2026 para 30/11/2026 (mantendo o atraso de -10%/dia), e apresentação final de 13/11/2026 para 04/12/2026. Entre 23/10 e 10/11 nada avançou no repositório (datasets, formato de saída e implementação de k-means/DBSCAN continuam pendentes).
- **Decisões:** D-007

### 2026-11-10 (ter) — Implementação da `DBSCANClassico` e análise de estrutura de dados para `regionQuery`

- **Tipo:** implementação
- **Participantes:** Adriano (sessão com Claude Code)
- **O que aconteceu:** Adriano escreveu e ajustou a lógica do `DBSCANClassico.java` (baseline sequencial) até compilar corretamente (corrigidos `getFeatures()` e o construtor de `ClusteringResult` com `numNoisePoints`); revisão de código confirmou que a lógica do DBSCAN está correta, incluindo a reclassificação de pontos de ruído como borda quando revisitados na expansão de um cluster. Também confirmada a convenção de rótulos (`-1` = não visitado, `-2` = ruído final, `0,1,2,...` = clusters — ver D-009) e o registro do `DBSCANClassico` (`getName()` = `"dbscan-classico"`) em um CSV de resultados separado do oficial (ver D-008). Por fim, avaliadas as alternativas de estrutura de dados para `regionQuery` (A-004).
- **Decisões:** D-008, D-009
- **Achados:** A-004
- **Pendências geradas:** P-011

### 2026-11-10 (ter) — Validação visual do DBSCAN clássico com dataset de luas e círculos

- **Tipo:** experimento · ferramenta
- **Participantes:** Adriano (sessão com Claude Code)
- **O que aconteceu:** Adriano gerou, em notebook Python próprio, um dataset sintético de 425 pontos (`make_moons` + `make_circles` + ruído uniforme) exportado para `data/demo_moons_circles.csv`; ligado `"dbscan-classico"` ao switch do `Main.java` (faltava desde a criação do stub); criado `scripts/plot_clusters.py` (visualização reutilizável de qualquer resultado de clusterização, junta dataset + CSV de rótulos por `id`); `DBSCANClassico` rodado com `eps=0.2;minPts=5` encontrou os 4 clusters esperados e 15 pontos de ruído, confirmado visualmente em `results/plots/demo_moons_circles_dbscan.png`.
- **Achados:** A-005

### 2026-11-11 (qua) — k-means paralelo implementado (primeira versão compilando)

- **Tipo:** implementação · ferramenta
- **Participantes:** Herik
- **O que aconteceu:** criei `kmeans/KMeansClassico.java` apenas como esqueleto (javadoc com a convenção de rótulos e os parâmetros; a lógica sequencial ainda não foi escrita). Em `KMeansClustering.java` (versão paralela) escrevi a validação de parâmetros (`k`, `numThreads`), `getName()` (`"kmeans"`), a divisão dos pontos em faixas contíguas (uma tarefa por faixa no `ExecutorService`, cada uma com acumuladores próprios de soma e contagem, sem lock), a redução das faixas sempre na mesma ordem, a atualização dos centróides (cluster vazio mantém o centróide anterior) com critério de parada por `tol`, e a inicialização k-means++ (`initCentroids`, `squaredDistance`). Depois, revisei o arquivo e corrigi os erros que impediam a compilação: faltava o método `assignRange`, além de `numChunk`/`maxSHift` digitados errado, um `]` sobrando e um argumento nomeado (`numNoisePoints: 0`) que não existe em Java. O projeto compila com `javac`, mas o k-means ainda **não foi executado** nem comparado com um resultado de referência. Também comentei o código e reescrevi o javadoc do k-means++ de forma mais simples.
- **Pendências geradas:** P-013

### 2026-11-14 (sáb) — `KMeansClassico.java` implementado

- **Tipo:** implementação
- **Participantes:** Julia
- **O que aconteceu:** implementação do baseline sequencial de k-means (`kmeans/KMeansClassico.java`, antes só esqueleto) e ligação ao switch do `Main.java` (`"kmeans-classico"`).

### 2026-11-15 (dom) — `DBSCANClustering.java` (paralelo) implementado

- **Tipo:** implementação
- **Participantes:** Sofia
- **O que aconteceu:** primeira versão do DBSCAN paralelo: detecção de pontos-núcleo particionada por thread (contagem de vizinhos via `ExecutorService`), fusão de núcleos por Union-Find (método `unir` sincronizado), e atribuição sequencial dos pontos de borda ao primeiro núcleo encontrado dentro de `eps`. Rótulo final de ruído `-2`, mesma convenção da D-009.
- **Pendências geradas:** P-006 passa a "em andamento → concluída" após validação (ver 2026-11-16)

### 2026-11-16 (seg) — Sincronização do repositório e validação do DBSCAN paralelo

- **Tipo:** organização · experimento · ferramenta
- **Participantes:** Adriano (sessão com Claude Code)
- **O que aconteceu:** puxados 7 commits novos do repositório remoto (k-means clássico de Julia, k-means paralelo de Herik, DBSCAN paralelo de Sofia). Revisão de código do `DBSCANClustering.java` não encontrou problemas de concorrência (sincronização correta no Union-Find, nenhuma leitura/escrita concorrente sem lock). Build com Maven limpo. Testado `dbscan-classico` vs. `dbscan` no dataset `demo_moons_circles.csv`: resultados agregados idênticos (4 clusters, 15 ruído) e comparação ponto a ponto sem nenhuma divergência. Testado também `kmeans-classico` vs. `kmeans` no mesmo dataset: ambos rodam e retornam 4 clusters, mas sem comparação ponto a ponto ainda (ver P-014).
- **Decisões:** —
- **Achados:** A-007
- **Pendências geradas:** P-014; P-006 e P-011 marcadas como concluídas

### 2026-11-16 (seg) — `DBSCANClusteringS.java`: segunda versão paralela, com merge lock-free

- **Tipo:** implementação · experimento · ferramenta
- **Participantes:** Adriano (sessão com Claude Code)
- **O que aconteceu:** Adriano implementou `DBSCANClusteringS.java`, uma otimização do DBSCAN paralelo baseada no PDSDBSCAN-S de Patwary et al. (2012): detecção de núcleos por partição, Union-Find local sem sincronização dentro de cada partição, e merge entre partições via CAS sobre `AtomicIntegerArray` (sem lock global, ao contrário do `synchronized unir()` da `DBSCANClustering.java` da Sofia). Revisão de código não encontrou problemas de concorrência. Ligada ao `Main.java` (`"dbscan-paralelo-s"`) e testada contra `DBSCANClassico` em 1/2/4/8/16 threads, com 5 repetições em 16 threads para forçar contenção no CAS — nenhuma divergência em nenhum caso.
- **Decisões:** D-011
- **Achados:** A-008
- **Pendências geradas:** P-015

### 2026-11-17 (ter) — `KMeansClusteringS.java` e validação dos três k-means

- **Tipo:** implementação · experimento · documentação
- **Participantes:** Herik e Julia
- **O que aconteceu:** comentadas as estratégias de `KMeansClassico.java` (algoritmo de Lloyd com k-means++) e de `KMeansClustering.java` (decomposição por faixas, baseada em Dhillon & Modha, 2000). Implementado `KMeansClusteringS.java`, que reduz as somas parciais em paralelo, análogo em memória compartilhada ao `MPI_Allreduce` do artigo, e ligado ao `Main.java` (`"kmeans-paralelo-s"`). Em `KMeansClustering.java`, `initCentroids`, `assignRange` e `squaredDistance` passaram a ser package-private para reaproveitamento, sem mudança de lógica. Cada um rodou os três algoritmos em `blobs_d10_k5.csv`, e o hash MD5 das saídas foi idêntico entre os algoritmos, entre 1/4/8 threads e entre as duas máquinas.
- **Decisões:** —
- **Achados:** A-009
- **Pendências geradas:** P-014 segue em andamento (faltam outras seeds e datasets e o speedup)

### 2026-11-20 (sex) — Revisão do planejamento; datasets e parâmetros oficiais calibrados

- **Tipo:** organização · experimento · ferramenta
- **Participantes:** Adriano (sessão com Claude Code)
- **O que aconteceu:** revisão do repositório confirmou que o formulário inicial do Classroom (P-002) já tinha sido preenchido e entregue por Herik dentro do prazo, só não estava registrado neste log. Confirmada ausência de qualquer coautoria/menção de IA em commits, autoria, ou código-fonte do repositório (checado todo o histórico git — só há menções transparentes em `docs/logs.md`, documento interno, sempre em tarefas permitidas pela disciplina). Retomado o planejamento geral: decidido manter os datasets sintéticos já prontos em `generate_datasets.py` como oficiais (D-012), autorizados pela professora. Ao investigar os parâmetros de `run_scalability_tests.sh`, nunca antes executados, foi encontrada uma miscalibração grave: `eps=0,5` e `k=8` fixo para os dois datasets — testado com `sklearn.cluster.DBSCAN` em grade de valores, `eps=0,5` colapsava os clusters verdadeiros (3 em vez de 8 no dataset1; 2 em vez de 5 no dataset2) e rodava 10-15× mais devagar. Parâmetros recalibrados por dataset (ver A-010) e `planejamento_trabalho.md` atualizado para refletir o estado real do projeto.
- **Decisões:** D-012
- **Achados:** A-010
- **Pendências geradas:** P-002, P-003 e P-010 marcadas como concluídas

### 2026-11-20 (sex, a confirmar data exata) — Experimentos 01 a 04: escalabilidade/speedup, qualidade de agrupamento e dimensionalidade

- **Tipo:** experimento · documentação · ferramenta
- **Participantes:** Adriano (sessão com Claude Code)
- **O que aconteceu:** rodada a bateria completa de escalabilidade e speedup (`run_scalability_tests.sh`, `run_speedup_tests.sh`), 5 repetições por configuração para reduzir ruído de medição (sleep/hibernate do Windows desligado temporariamente via `powercfg` para não interromper a execução, restaurado depois), agregada em mediana/min/max por `scripts/aggregate_results.py` (novo) e documentada em `docs/analise_experimento_01.md` (**experimento_01**) — achado notável: o speedup do DBSCAN lock-based superou o ideal em alguns pontos de thread count, investigado nas sessões seguintes. Em seguida, **experimento_02**: avaliação de qualidade de agrupamento via ARI/NMI contra rótulo verdadeiro e ARI de consistência entre implementações (`scripts/evaluate_clustering_quality.py`, novo; `generate_datasets.py` passou a salvar também os rótulos verdadeiros) — DBSCAN superou o k-means especificamente no dataset anisotrópico (dataset2), consistente com a hipótese de cada algoritmo favorecer a geometria que assume. Documentado em `docs/analise_experimento_02.md`. Depois, **experimento_03** (efeito da dimensionalidade informativa vs. ruído, d=1 a 1000, datasets sintéticos gerados por `scripts/generate_dim_datasets.py`, `eps` do DBSCAN calibrado automaticamente por `scripts/calibrate_eps_by_dimension.py`) e **experimento_04** (mesma metodologia, granularidade fina d=1-10) — o achado central foi a descoberta de que quedas isoladas de qualidade do k-means em certas dimensões (d=50 no exp_03, d=9 no exp_04) eram causadas por bimodalidade de convergência dependente da seed de inicialização dos centróides (`seed=123` fixa por padrão, nunca antes variada), não pela dimensionalidade em si — confirmado repetindo com 5 seeds (ver A-011). `docs/analise_experimento_03.md` e `docs/analise_experimento_04.md` foram corrigidos para refletir essa explicação depois de descoberta.
- **Decisões:** D-013, D-014
- **Achados:** A-011
- **Pendências geradas:** P-007 marcada como concluída

### 2026-11-21 (sáb) — Experimento 05: speedup em função da dimensionalidade; ajuste de escopo pelo custo O(n²·d) do DBSCAN

- **Tipo:** experimento · documentação
- **Participantes:** Adriano (sessão com Claude Code)
- **O que aconteceu:** planejado para replicar a metodologia de speedup do experimento_01 variando a dimensionalidade (d=2, 50, 500; n=150.000) em vez do dataset, para testar se o ganho de paralelismo aumenta com o custo por ponto (hipótese de Amdahl). Uma medição isolada do `dbscan-classico` em d=50/n=150.000 revelou ~22 minutos para uma única execução sequencial (vs. ~2 minutos em d=2), confirmando custo ~O(n²·d) — inviabilizando o plano original. Ajuste de escopo: d=500 removido; DBSCAN passou a usar n=25.000 nesta rodada (k-means manteve n=150.000); `eps` recalibrado para o novo n via `scripts/calibrate_eps_speedup.py` (com subamostra de 15.000 pontos na busca em grade, já que o grid search completo também era caro em alta dimensão). Bateria completa (2 dimensões × 5 repetições × 22 configurações, `scripts/run_speedup_dim_tests.sh`) rodada e documentada em `docs/analise_experimento_05.md`. Ver A-012 para o resultado.
- **Decisões:** —
- **Achados:** A-012
- **Pendências geradas:** P-016

---

## 6. Referências rápidas

### Equipe

| Pessoa | Papel |
|---|---|
| Adriano Lúcio Uchôa Brandão | DBSCAN (com Sofia); setup do repositório e documentação |
| Sofia Gutschow Casal | DBSCAN (com Adriano) |
| Herik Daurizio Ricardo | k-means (com Julia) |
| Julia Yokoyama Massaki | k-means (com Herik) |

### Mapa da pasta

| Caminho | Conteúdo |
|---|---|
| `src/main/java/br/uel/ce/clustering/Main.java` | Interface de execução comum (CLI) para os dois algoritmos |
| `src/main/java/br/uel/ce/clustering/common/` | `DataPoint`, `DatasetReader`, `ResultWriter`, `ClusteringAlgorithm`, `ClusteringResult`, `AlgorithmParams` |
| `src/main/java/br/uel/ce/clustering/kmeans/KMeansClustering.java` | k-means paralelo, redução sequencial (Herik) — implementado e com saída idêntica ao clássico em um dataset (A-009); falta speedup (P-014) |
| `src/main/java/br/uel/ce/clustering/kmeans/KMeansClusteringS.java` | k-means paralelo, redução paralela das somas parciais (Herik) — implementado e com saída idêntica ao clássico em um dataset (A-009); falta speedup (P-014) |
| `src/main/java/br/uel/ce/clustering/kmeans/KMeansClassico.java` | k-means baseline sequencial (Julia) — implementado |
| `src/main/java/br/uel/ce/clustering/dbscan/DBSCANClustering.java` | DBSCAN paralelo, lock global (Sofia) — implementado e validado contra o clássico (A-007) |
| `src/main/java/br/uel/ce/clustering/dbscan/DBSCANClusteringS.java` | DBSCAN paralelo, merge lock-free via CAS (Adriano) — implementado e validado contra o clássico (A-008) |
| `src/main/java/br/uel/ce/clustering/dbscan/DBSCANClassico.java` | DBSCAN baseline sequencial (Adriano) — implementado e validado (A-005) |
| `scripts/` | Geração de dados: `generate_datasets.py`, `generate_dim_datasets.py` (exp_03/04), `generate_speedup_dim_datasets.py` (exp_05). Testes: `run_scalability_tests.sh`, `run_speedup_tests.sh` (exp_01), `run_quality_tests.sh` (exp_02), `run_dim_tests.sh` (exp_03/04), `run_dim_kmeans_seeds.sh` (variância de seed, exp_03/04), `run_speedup_dim_tests.sh` (exp_05). Calibração: `calibrate_eps_by_dimension.py` (exp_03/04), `calibrate_eps_speedup.py` (exp_05). Avaliação/agregação: `aggregate_results.py` (tempo, todos os exp.), `evaluate_clustering_quality.py` (exp_02), `evaluate_dim_quality.py` (exp_03/04), `evaluate_kmeans_seed_variance.py` e `merge_kmeans_seed_quality.py` (exp_03/04). Gráficos: `plot_results.py`, `plot_speedup.py` (exp_01/05), `plot_dim_quality.py` (exp_03/04), `plot_clusters.py` |
| `docs/revisao-literatura/` | `fichamentos.md`, `revisao_literatura.md`, `referencias.bib`, `artigos/` (23 PDFs) |
| `docs/analise_experimento_01.md` | Escalabilidade (n) e speedup (threads) nos 6 algoritmos — 2 datasets oficiais, 5 repetições |
| `docs/analise_experimento_02.md` | Qualidade de agrupamento (ARI/NMI contra rótulo verdadeiro; consistência entre implementações) |
| `docs/analise_experimento_03.md` | Efeito da dimensionalidade (informativa vs. ruído, d=1 a 1000) na qualidade; inclui a descoberta da bimodalidade por seed (A-011) |
| `docs/analise_experimento_04.md` | Mesmo efeito, granularidade fina (d=1-10) |
| `docs/analise_experimento_05.md` | Speedup em função da dimensionalidade (d=2, 50); inclui o achado de custo O(n²·d) do DBSCAN e o speedup superlinear (A-012) |
| `relatorio/` | `template_trabalho_semestre.tex`, `logo_uel.png` |
| `data/` | Datasets de entrada, não versionados (`.gitignore`): `dim_experimento/` (exp_03/04), `speedup_dim_experimento/` (exp_05), e os gerados por `generate_datasets.py` |
| `results/experimento_0{1..5}/` | Resultados brutos/agregados (CSV) e gráficos de cada experimento — ver `docs/analise_experimento_0{1..5}.md` para a leitura |
| `docs/logs.md` | Este documento |
