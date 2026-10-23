# Log do Projeto — k-means e DBSCAN paralelos (Computação Escalável)

> Documento vivo. Registra, em ordem cronológica, o que foi feito, decidido e descoberto em cada dia do projeto.
> Serve de memória histórica para o grupo e de contexto de entrada para quem retomar o trabalho em outra sessão.

**Projeto:** implementação paralela de k-means e DBSCAN em Java, com análise de escalabilidade
**Instituição:** Departamento de Computação, UEL · **Disciplina:** Computação Escalável (prof.ª Larissa C. Shimomura)
**Mantido por:** Herik Daurizio Ricardo, Julia Yokoyama Massaki, Adriano Lúcio Uchôa Brandão, Sofia Gutschow Casal
**Última atualização:** 2026-10-23

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

## 1. Estado atual (snapshot em 2026-10-23)

| Dimensão | Situação |
|---|---|
| **Fase** | Revisão de literatura concluída; implementação dos algoritmos ainda não iniciada |
| **Frente ativa** | Fichamentos e revisão de literatura (finalizados hoje); próxima frente é a implementação de k-means e DBSCAN |
| **Próximo marco** | Preencher o formulário inicial do Classroom (grupo, tarefa, algoritmos, duplas, link do repo) — prazo **25/10/2026** |
| **Bloqueio principal** | Nenhum bloqueio técnico; falta decidir os 2 datasets e o formato de saída comum antes de começar a implementar |
| **Divisão de responsabilidades** | Herik e Julia → k-means; Adriano e Sofia → DBSCAN |
| **Revisão de literatura** | Concluída — 23 artigos fichados em 8 slots temáticos (KM1–KM4 para k-means, DB1–DB4 para DBSCAN), cada slot com revisão de consistência própria |
| **Implementação (k-means)** | Não iniciada — stub vazio em `src/main/java/br/uel/ce/clustering/kmeans/KMeansClustering.java` |
| **Implementação (DBSCAN)** | Não iniciada — stub vazio em `src/main/java/br/uel/ce/clustering/dbscan/DBSCANClustering.java` |
| **Experimentos de escalabilidade** | Não iniciados — scripts prontos (`generate_datasets.py`, `run_scalability_tests.sh`, `plot_results.py`), datasets ainda não gerados |
| **Relatório** | Template LaTeX pronto (`relatorio/template_trabalho_semestre.tex`), conteúdo ainda não escrito |
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

---

## 3. Pendências

| ID | Aberta em | Pendência | Responsável | Status |
|---|---|---|---|---|
| P-001 | 2026-10-21 | Criar repositório remoto no GitHub e dar push | Adriano | Concluída (confirmado em 2026-10-23: `origin` já aponta para `github.com/cowpox/kmeans-dbscan`, branch sincronizado) |
| P-002 | 2026-10-21 | Preencher formulário inicial do Classroom (grupo, tarefa, algoritmos, duplas, link do repositório) — prazo 25/10/2026 | Grupo todo | Aberta |
| P-003 | 2026-10-21 | Escolher/gerar os 2 datasets (10.000–150.000 pontos; se sintéticos, com distribuições diferentes) | Grupo todo | Aberta |
| P-004 | 2026-10-21 | Definir formato de saída comum entre os dois algoritmos (além da interface já criada em `common/`) | Grupo todo | Aberta |
| P-005 | 2026-10-21 | Implementar k-means paralelo | Herik e Julia | Aberta (stub vazio) |
| P-006 | 2026-10-21 | Implementar DBSCAN paralelo | Adriano e Sofia | Aberta (stub vazio) |
| P-007 | 2026-10-21 | Rodar testes de escalabilidade e gerar os 2 gráficos finais | Grupo todo | Aberta |
| P-008 | 2026-10-21 | Escrever relatório final (template em `relatorio/template_trabalho_semestre.tex`) — prazo 12/11/2026 (atraso: -10%/dia) | Grupo todo | Aberta |
| P-009 | 2026-10-21 | Preparar apresentação final (~20 min, ~5 min por pessoa) — 13/11/2026 | Grupo todo | Aberta |

---

## 4. Achados

Descobertas e observações que mudam ou informam o rumo do projeto.

| ID | Data | Achado | Implicação |
|---|---|---|---|
| A-001 | 2026-10-23 | A revisão de literatura foi organizada em 8 slots temáticos (KM1–KM4 para k-means, DB1–DB4 para DBSCAN), cobrindo os 23 artigos do referencial teórico em `docs/revisao-literatura/artigos/`, cada slot com fichamento (`fichamentos.md`) e revisão de consistência própria em `revisao_literatura.md`. | Base teórica para o relatório final já está estruturada antes mesmo de a implementação começar — ao escrever o relatório, usar os slots como esqueleto das seções de trabalhos relacionados. |

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
| `src/main/java/br/uel/ce/clustering/kmeans/KMeansClustering.java` | Stub do k-means (Herik, Julia) — implementação ainda pendente |
| `src/main/java/br/uel/ce/clustering/dbscan/DBSCANClustering.java` | Stub do DBSCAN (Adriano, Sofia) — implementação ainda pendente |
| `scripts/` | `generate_datasets.py`, `run_scalability_tests.sh`, `plot_results.py`, `requirements.txt` |
| `docs/revisao-literatura/` | `fichamentos.md`, `revisao_literatura.md`, `referencias.bib`, `artigos/` (23 PDFs) |
| `relatorio/` | `template_trabalho_semestre.tex`, `logo_uel.png` |
| `data/` | Datasets de entrada (só `teste.csv` de exemplo por enquanto) |
| `results/`, `results/plots/` | Saídas dos experimentos e gráficos finais (ainda vazios) |
| `docs/logs.md` | Este documento |
