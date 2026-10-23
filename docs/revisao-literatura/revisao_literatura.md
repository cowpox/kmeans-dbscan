# Revisão de literatura: k-means e DBSCAN paralelos

> Documento vivo. Organiza a leitura dos 22 artigos de `artigos/`: o que cada slot cobre, o que já foi
> lido, o que cada artigo permite afirmar e onde isso entra no relatório. Depois vira a fundamentação
> teórica do relatório (`../../relatorio/template_trabalho_semestre.tex`).
>
> Objetivo: mapear, sintetizar e analisar criticamente o conhecimento científico já existente sobre
> k-means e DBSCAN (formulação original e paralelização), para fundamentar a pesquisa e evitar a
> redescoberta do que já foi provado.
>
> Escopo: o trabalho da disciplina Computação Escalável (UEL), com implementação paralela de k-means
> (Herik, Julia) e de DBSCAN (Adriano, Sofia) em Java (`ExecutorService`/`ForkJoin`, uma máquina) e
> análise de escalabilidade. A revisão é só fundamentação teórica. Ela não decide nem descreve a
> implementação, que fica com as duplas responsáveis.
>
> Estado (22/10/2026): leitura e fichamento concluídos para os 22 artigos, em duas etapas escritas
> nestes mesmos arquivos. A 1ª etapa (k-means) tem KM1 (MacQueen, Lloyd), KM2 (Arthur &
> Vassilvitskii, Bahmani et al.), KM3 (Dhillon & Modha, Zhao et al., Ene et al.) e KM4 (Sculley, Meng et
> al.), num total de 9 artigos. A 2ª etapa (DBSCAN) tem DB1 (Ester et al., Schubert et al.), DB2
> (Patwary et al., Wang et al.), DB3 (Xu et al., Götz et al., He et al. 2011 e 2014, Dai & Lin, Han et
> al., Cordova & Moh) e DB4 (Böhm et al., Andrade et al.), num total de 13 artigos.
>
> Como usar: cada artigo lido entra na tabela da §5 e em `referencias.bib`. O PDF fica em `artigos/`
> (catálogo em `artigos/README.md`) e a ficha completa fica em `fichamentos.md`. O processo está em
> `fichamento_literatura_template.md`.
>
> Nomenclatura: "slot" é um eixo temático (convenção do PASSO/CURA). Aqui são KM1 a KM4 (k-means) e
> DB1 a DB4 (DBSCAN, 2ª etapa).

---

## 1. De onde saem os slots

1. Enunciado do trabalho (README do repositório): clustering com k-means e DBSCAN, paralelismo em
   Java, análise de escalabilidade (tempo contra tamanho do dataset) e relatório com as seções
   Introdução, Algoritmos Implementados, Setup Experimental, Análise de Escalabilidade e Conclusões.
2. Levantamento prévio do grupo (`referencial_teorico.md`, 21/10/2026): 22 artigos em duas famílias,
   formulação original e paralelização/distribuição. Foi escrito sem leitura integral, e suas
   afirmações foram conferidas e corrigidas na leitura (ver §8). O conteúdo foi absorvido por esta
   síntese e o arquivo foi removido do repositório ao fim da revisão (22/10/2026). A versão original
   continua no histórico do Git.
3. Decisão de 22/10/2026: manter no repositório todos os PDFs, inclusive os que não estavam na lista
   inicial do grupo (Sculley, Ene et al., Schubert et al., He et al. 2014).

Todos os slots entram, na ordem de leitura KM1, KM2, KM3, KM4, DB1, DB2, DB3, DB4.

---

## 2. Slots de busca

> Aviso: o corpus é fixo (22 PDFs já no repositório). "O que extrair" lista o que cada slot precisa
> responder, e o que a leitura não responde vira lacuna na §7.

### Slot KM1: Formulação original do k-means (ler primeiro)

- Por quê: definir com precisão o que é k-means e o que a fonte original garante, antes de discutir
  paralelização.
- O que extrair: definição do problema e do critério (variância intra-classe); qual procedimento cada
  fonte descreve (sequencial ou em lote); garantias de convergência e de otimalidade; terminação;
  sensibilidade à ordem e à partição inicial.
- PDFs: MacQueen 1967; Lloyd 1982 (rascunho de 1957).
- Lidos e fichados (22/10/2026): ambos.

### Slot KM2: Inicialização

- Por quê: a qualidade e o número de iterações do k-means dependem do ponto de partida, e a literatura
  mostra qual estratégia usar e quanto custa em paralelo.
- O que extrair: estratégia de semeadura, garantia teórica, custo (passes sobre os dados), evidência
  experimental e as condições em que foi medida.
- PDFs: Arthur & Vassilvitskii 2007; Bahmani et al. 2012.
- Lidos e fichados (22/10/2026): ambos.

### Slot KM3: Paralelização em memória distribuída e MapReduce

- Por quê: estabelecer o modelo "particionar os pontos, calcular somas parciais por partição e
  combinar globalmente a cada iteração", e como a literatura mede speedup e escalabilidade.
- O que extrair: o que é particionado e o que é comunicado por iteração; análise de custo;
  speedup e scaleup medidos (número de nós, tamanho do dataset); garantias de aproximação.
- PDFs: Dhillon & Modha 2000; Zhao et al. 2009; Ene et al. 2011.
- Lidos e fichados (22/10/2026): os três.

### Slot KM4: Escala prática, mini-lote e plataforma

- Por quê: alternativas que trocam um pouco de qualidade por custo menor por iteração, e o que uma
  plataforma (Spark) de fato documenta sobre k-means.
- O que extrair: o trade-off entre tempo e qualidade do mini-lote, e o que o artigo do MLlib diz (ou
  não diz) sobre a implementação distribuída do k-means.
- PDFs: Sculley 2010; Meng et al. 2016.
- Lidos e fichados (22/10/2026): ambos.

### 2ª etapa (DBSCAN)

### Slot DB1: Formulação original do DBSCAN e complexidade (ler primeiro)

- Por quê: definir com precisão o que é o DBSCAN (modelo e algoritmo) e qual é o custo do sequencial,
  que é a referência de todo speedup.
- O que extrair: definições (núcleo, borda, ruído, alcançabilidade); algoritmo e uma consulta de
  vizinhança por ponto; custo e a validade do O(n log n); tratamento dos pontos de borda e dependência
  da ordem; escolha de ε e minPts.
- PDFs: Ester et al. 1996; Schubert et al. 2017.
- Lidos e fichados (22/10/2026): ambos.

### Slot DB2: Paralelização em memória compartilhada e com conjuntos disjuntos

- Por quê: é o modelo mais próximo do nosso cenário (uma máquina, *threads*) e traz a ideia de trocar a
  fusão sequencial por *union-find* ou por um grafo de células.
- O que extrair: como se divide o trabalho entre *threads*; como se resolvem as ligações entre
  partições; evidência de speedup (núcleos, tamanho dos dados, baseline); garantias de custo.
- PDFs: Patwary et al. 2012; Wang, Gu & Shun 2020.
- Lidos e fichados (22/10/2026): ambos.

### Slot DB3: Paralelização em memória distribuída (mestre-escravo, MPI, MapReduce, Spark)

- Por quê: a literatura de paralelização do DBSCAN é dominada por esse modelo, e ele mostra as
  decisões de projeto recorrentes (particionamento, halo, fusão) e os custos que elas trazem.
- O que extrair: estratégia de particionamento e como equilibra a carga; tratamento da borda entre
  partições; forma da fusão e se é sequencial; afirmação de exatidão; evidência de speedup e a base.
- PDFs: Xu et al. 1999; Götz et al. 2015; He et al. 2011; He et al. 2014; Dai & Lin 2012; Han et al.
  2016; Cordova & Moh 2015.
- Lidos e fichados (22/10/2026): os sete.

### Slot DB4: DBSCAN em GPU

- Por quê: serve de contraste com o nosso modelo e mostra como o ganho depende do índice e da linha de
  base.
- O que extrair: como o DBSCAN é reformulado para milhares de *threads*, e o speedup medido e sobre o
  quê.
- PDFs: Böhm et al. 2009; Andrade et al. 2013.
- Lidos e fichados (22/10/2026): ambos.

---

## 3. Bases e ferramentas de verificação

| Ferramenta | Uso |
|---|---|
| Cabeçalho do próprio PDF | título, autores, veículo, DOI, paginação (fonte primária) |
| Crossref (`api.crossref.org/works/<DOI>`) | conferir volume, número, páginas, ano, DOI |
| Busca na web (pesquisa padrão) | DOI e páginas que o PDF não traz (marcar "a conferir") |
| PyMuPDF / `pdftotext` | extrair o texto integral com marcação de página e conferir citações |

Gestor de referências: `referencias.bib` (BibTeX), que cresce à medida que as fichas são feitas. O
`relatorio/template_trabalho_semestre.tex` (raiz do repositório) ainda não tem bloco de bibliografia,
e é preciso definir o estilo de citação antes de preencher a seção de fundamentação.

---

## 4. Fila de leitura (trazidos, ainda não lidos ou fichados)

Os dados bibliográficos estão em `artigos/README.md`. Cada artigo é marcado com `[x]` e movido para a
§5 ao terminar.

Fila vazia (22/10/2026): os 22 artigos foram lidos e fichados (9 de k-means e 13 de DBSCAN).

Lacuna de fonte: o resumo de Forgy (1965, *Biometrics* 21:768), citado por MacQueen como origem do
procedimento de dois passos, não está no repositório e não foi lido.

---

## 5. Artigos lidos

Um artigo só entra aqui depois do fichamento completo, nunca a partir de título ou abstract.

| Ref | Slot | Relevância | O que aproveitar |
|---|---|---|---|
| MacQueen (1967), *Some methods for classification and analysis of multivariate observations* | KM1 | Origem do termo "k-means". O procedimento do artigo é sequencial (uma observação por vez), e o laço de dois passos em lote aparece na §3.6, atribuído a Forgy e Jennrich, com terminação em número finito de passos. A convergência quase certa vale só para o procedimento sequencial e não leva necessariamente ao ótimo. Os experimentos têm no máximo 760 pontos. | Citar como origem do termo e do critério, nomear a variante implementada e ressalvar que não trata de paralelismo nem de escala. Ficha em `fichamentos.md`. |
| Lloyd (1982; rascunho de 1957), *Least squares quantization in PCM* | KM1 | Duas condições necessárias para quantização escalar ótima (centro de massa e fronteira no ponto médio) e uma iteração alternada que não aumenta o ruído, sem garantia de mínimo global. Não trata de agrupamento em R^d, e a associação com o k-means é tradição posterior. | Citar a origem histórica das duas condições com a ressalva do escopo e ancorar a descrição do algoritmo em Arthur & Vassilvitskii (§2.1). Ficha em `fichamentos.md`. |
| Arthur & Vassilvitskii (2007), *k-means++: the advantages of careful seeding* | KM2 | Semeadura D²: E[φ] ≤ 8(ln k + 2)φ_OPT, justa a menos de constante. Experimentos preliminares (n até 494.019; 20 execuções) mostram menos erro e menos tempo, com uma exceção no Norm25 (k = 50). O PDF é o manuscrito do autor. | Referência da inicialização k-means++, e a estratégia adotada deve ser declarada no relatório. Ficha em `fichamentos.md`. |
| Bahmani et al. (2012), *Scalable k-means++* | KM2 | O passo de Lloyd é fácil de paralelizar, mas a inicialização k-means++ exige k passes. O k-means‖ usa O(log ψ) rodadas, amostragem independente por ponto e reagrupamento dos candidatos, com experimentos em Hadoop de 1.968 nós e 4,8 milhões de pontos. A escalabilidade com o número de máquinas é afirmada e não medida. | Justificar onde o paralelismo do k-means é trivial e onde não é, e dar contexto de como a literatura mede escalabilidade. Ficha em `fichamentos.md`. |
| Dhillon & Modha (2000), *A data-clustering algorithm on distributed memory multiprocessors* | KM3 | Modelo-base de paralelização do k-means em memória distribuída (MPI): blocos de n/P pontos e somas parciais por centróide combinadas por `MPI_Allreduce`. O custo é ≈ 3nkd por iteração, e o ganho cresce com n (condição P·T_reduce/(3·T_flop) ≪ n). No SP2 de 16 nós, o speedup é 15,62 (n = 2²¹) e 6,22 (n = 2¹³), e o scaleup é ≈ constante. A adaptação a *threads* só é proposta (p. 14–15), não medida. | Referência do modelo de paralelização e das definições de *speedup* e *scaleup*, além da lacuna de memória compartilhada. Ficha em `fichamentos.md`. |
| Zhao, Ma & He (2009), *Parallel k-means clustering based on MapReduce* | KM3 | A mesma decomposição em *map* (atribuição), *combine* (soma local) e *reduce* (novos centros) no Hadoop. Os experimentos vão de 1 a 4 computadores e de 1 a 8 GB, mostrados só em curvas, com o conjunto de dados não descrito. | Tradução do modelo para MapReduce, com o *combiner* como agregação local, e a ressalva de evidência fraca. Ficha em `fichamentos.md`. |
| Ene, Im & Moseley (2011), *Fast clustering using MapReduce* | KM3 | Trata de k-median e k-center, e não de k-means, com paralelização por amostragem e garantia de aproximação. Descreve o Lloyd paralelo exato (p. 16). Os experimentos são simulação numa só máquina, sem comunicação, e o Sampling-Lloyd é 18× a 73× mais rápido que o Lloyd paralelo, com custo 5% a 16,5% maior (tabelas). | Contraste entre paralelização exata e aproximada e ressalva sobre medir tempo em simulação. Só contexto para o k-means. Ficha em `fichamentos.md`. |
| Sculley (2010), *Web-scale k-means clustering* | KM4 | Mini-lote, que troca qualidade por custo por iteração: várias ordens de grandeza mais rápido que o lote, com soluções melhores que o SGD, em RCV1 (781.265 exemplos de treino). É um pôster de 2 páginas, sequencial. | Contexto do trade-off entre tempo e qualidade. Não se aplica à paralelização do lote. Ficha em `fichamentos.md`. |
| Meng et al. (2016), *MLlib: machine learning in Apache Spark* | KM4 | Apresenta a biblioteca, e o k-means aparece só numa lista. O ganho de 2,5× do KMeans entre as versões 1.0 e 1.1 (Fig. 2b) vem sem descrição do algoritmo, de k nem do tamanho dos dados. | Só contexto (k-means em Spark existe). Não sustenta nenhuma afirmação sobre o algoritmo distribuído. Ficha em `fichamentos.md`. |
| Ester et al. (1996), *A density-based algorithm for discovering clusters in large spatial databases with noise* | DB1 | Define o modelo (vizinhança-Eps, núcleo, borda, ruído, alcançabilidade) e o algoritmo (uma consulta de vizinhança por ponto). O(n · log n) em média é derivado de forma informal, supondo consultas O(log n) na R\*-tree. A eficiência foi testada até 12.512 pontos, com o DBSCAN 245× a 1.934× mais rápido que o CLARANS (Tabela 1). Um ponto de borda alcançável de dois grupos fica com o que o descobrir primeiro. | Origem do modelo e do algoritmo sequencial. Citar o O(n log n) só com a ressalva de Schubert et al. Ficha em `fichamentos.md`. |
| Schubert et al. (2017), *DBSCAN revisited, revisited* | DB1 | Custo O(C + n·Q + Σrᵢ), com exatamente uma consulta por ponto, Θ(n²) sem índice e O(n log n) "formalmente incorreto" como garantia. Não há índice universal para d ≥ 3. O resultado depende da ordem para pontos de borda. minPts = 4 (2D) ou 2·dim, com sinais de alerta (ruído de 1% a 30%). Com ε razoável, índices dão 10× a 1.000× sobre a varredura linear. | Caracterização correta da complexidade e dos parâmetros, e base do que significa "mesmo resultado" em paralelo. Ficha em `fichamentos.md`. |
| Patwary et al. (2012), *A new scalable parallel DBSCAN algorithm using the disjoint-set data structure* | DB2 | Reformula a expansão como componentes conexos com *union-find*, sem dependência de ordem. O PDSDBSCAN-S (memória compartilhada, travas por ponto) chega a speedup de 25,97 em 40 núcleos, para até 1,9 milhão de pontos, sem contar a construção da kd-tree, e os conjuntos reais pequenos saturam em ≈ 8 núcleos. O PDSDBSCAN-D chega a 5.765 em 8.192 núcleos (melhor caso, 72,3 milhões de pontos). O mestre-escravo reimplementado fica em 3,35 a 4,12. | Referência do modelo de memória compartilhada com fusão sem trava global e evidência de speedup com a base declarada. Ficha em `fichamentos.md`. |
| Wang, Gu & Shun (2020), *Theoretically-efficient and practical parallel DBSCAN* | DB2 | Células de lado ε/√d, grafo de células e componentes conexos, com trabalho O(n log n) em 2D e profundidade polilogarítmica. Para d ≥ 3, a implementação usa BCP quadrático. Com 36 núcleos e HT, o speedup é de 5× a 33× sobre a melhor serial e de 2× a 89× self-relative, e o `pdsdbscan` e o `hpdbscan` não superam a serial na maioria dos conjuntos. | Alternativa à expansão por consulta, crítica ao speedup self-relative e método de reportar contra a melhor serial. Ficha em `fichamentos.md`. |
| Xu, Jäger & Kriegel (1999), *A fast parallel clustering algorithm for large spatial databases* | DB3 | PDBSCAN: dR\*-tree distribuída (curva de Hilbert), PartDBSCAN em cada nó e fusão por candidatos (Lema 10: mesmo resultado do DBSCAN). Usa 8 estações HP e PVM, com speedup "quase linear" e scaleup "muito bom" sem valores no texto, e o conjunto `birch3` tem speedup menor por desbalanceamento. | Origem do modelo mestre-escravo e definições de speedup, scaleup e sizeup. Ficha em `fichamentos.md`. |
| Götz, Bodenstein & Riedel (2015), *HPDBSCAN: highly parallel DBSCAN* | DB3 | Grade de lado ε como índice e divisão do espaço, halo, heurística de custo e fusão por regras de equivalência, em MPI + OpenMP. Com até 768 núcleos, o speedup chega a 460,0 (Bremen, 81,3 milhões de pontos) e 263,8 (Twitter), e o PDSDBSCAN-D achata em ≈ 3,5 num conjunto pequeno. | Particionamento por grade com halo e balanceamento por custo, com evidência em dados reais enviesados. Ficha em `fichamentos.md`. |
| He et al. (2011), *MR-DBSCAN: an efficient parallel density-based clustering algorithm using MapReduce* | DB3 | Quatro etapas em MapReduce: partição por *grid file*, DBSCAN local com halo Eps, pares de agrupamentos por pontos de fusão (Teorema 1) e mapeamento global em uma só *thread*. Usa 13 nós e 1,9 bilhão de pontos de GPS, com speedup só no menor conjunto, "quase linear" (curvas). | Particionamento com halo e fusão por pontos de fusão, com ressalva de evidência fraca. Ficha em `fichamentos.md`. |
| He et al. (2014), *MR-DBSCAN: a scalable MapReduce-based DBSCAN algorithm for heavily skewed data* | DB3 | Particionamento por estimativa de custo (CBP) para dados enviesados, com 128 máquinas, 1,2 bilhão (GPS) e 0,8 bilhão (TIGER). O tempo máximo entre tarefas é 9.558 s no CBP, 32.435 s no ESP e 80.944 s no RBP, e o speedup tem base em 4 tarefas, com 71% de eficiência em 64. | O desbalanceamento como causa central da perda de speedup, e como medir. Ficha em `fichamentos.md`. |
| Dai & Lin (2012), *Efficient Map/Reduce-based DBSCAN algorithm with optimized data partition* | DB3 | PRBP: fatias de 2ε, com corte na fatia de menos pontos para reduzir os pontos de fronteira, e 5 fases em Hadoop. No nt7_1000k, são 328.900 pontos de fronteira no GRIDBSCAN contra 114.700 no DBSCAN-MR, que é 21% a 37% mais rápido (em gráficos). | O custo dos pontos duplicados na fronteira. Ficha em `fichamentos.md`. |
| Han et al. (2016), *A novel scalable DBSCAN algorithm with Spark* | DB3 | Sem comunicação entre *executors*, com partições por faixas de índices, SEEDs e fusão sequencial no *driver*. O speedup só dos executores chega a 137 em 512 núcleos (1 milhão de pontos, aproximado), e o total no 100k é 5,6 em 32 núcleos (executores: 10,2). O Spark é 9× a 17× mais rápido que o próprio MapReduce dos autores, em 10 mil pontos. | A distinção entre speedup dos executores e total, e o custo da fusão centralizada. Ficha em `fichamentos.md`. |
| Cordova & Moh (2015), *DBSCAN on resilient distributed datasets* | DB3 | RDD-DBSCAN: BSP por contagem de pontos, margem ε, DBSCAN local com R-tree e rotulagem global por aliases (sequencial). Restrito a 2-D, com 5 VMs e 1 milhão de pontos, e tempos só em curvas. | Exemplo de avaliação fraca e das restrições declaradas. Ficha em `fichamentos.md`. |
| Böhm et al. (2009), *Density-based clustering using graphics processors* | DB4 | CUDA-DClust: cadeias (expansões simultâneas), matriz de colisões e índice simples em GPU. O speedup vai de 3,5 a ≈ 15 (até 2 milhões de pontos) sobre um DBSCAN de uma só *thread*, e depende de n, de MinPts e do índice. O ε quase não importa. | Contraste, e a dependência de parâmetros e do índice. Ficha em `fichamentos.md`. |
| Andrade et al. (2013), *G-DBSCAN: a GPU accelerated algorithm for density-based clustering* | DB4 | Grafo de vizinhança (O(V²)) e BFS em GPU. O speedup total máximo é de 112× (de 9.261,1 s para 82,9 s em 700 mil pontos) sobre o sequencial O(V²) dos próprios autores, com construção do grafo em ≈ 82× e BFS em ≈ 21×. | Contraste, e a importância de declarar a base. Ficha em `fichamentos.md`. |

---

## 6. Síntese por tema

Esta síntese é a base do texto de fundamentação do relatório.

- KM1, formulação original. Dois artigos lidos (MacQueen 1967; Lloyd 1982). O que os dois estabelecem
  é o par de operações em torno do qual o k-means é construído: representar cada grupo pela média
  (centro de massa) e atribuir cada ponto ao representante mais próximo (no Lloyd, fronteiras no ponto
  médio entre quanta). Os dois também concordam, de forma independente, que o procedimento não garante
  o ótimo global. MacQueen dá um exemplo de convergência, com probabilidade positiva, a uma partição
  que não minimiza a variância (p. 288), e Lloyd mostra que as condições necessárias não bastam e dá
  exemplos com vários mínimos locais (p. 131, 136). O ponto delicado é o nome. O procedimento do
  artigo de MacQueen é sequencial (atualiza a média a cada observação), o laço de dois passos em lote
  vem na §3.6 atribuído a Forgy e Jennrich (convergência em número finito de passos), e o artigo de
  Lloyd trata quantização escalar e não menciona agrupamento de pontos. Os artigos posteriores (Arthur
  & Vassilvitskii; Bahmani et al.) chamam o laço em lote de "iteração de Lloyd". Nenhum dos dois
  artigos fala de paralelismo, de custo por iteração ou de escala, e MacQueen testa no máximo 760
  pontos. Para o relatório, é preciso dizer qual variante foi implementada e citar a fonte certa para
  ela, sem atribuir a Lloyd 1982 ou a MacQueen 1967 o que eles não descrevem. Faltam o resumo de Forgy
  (1965) e qualquer análise do custo por iteração (o custo O(nkd) da semeadura aparece em Bahmani et
  al., p. 623, slot KM2).
- KM2, inicialização. Dois artigos lidos (Arthur & Vassilvitskii 2007; Bahmani et al. 2012). A
  semeadura é onde se ganha qualidade e se reduz o número de iterações. Arthur & Vassilvitskii provam
  uma garantia O(log k) em esperança para a ponderação D², mostram que ela é justa e medem, em dados
  de até 494 mil pontos, potenciais menores e execuções mais rápidas na maioria dos casos (com uma
  exceção no Norm25, k = 50). Bahmani et al. medem que o k-means‖ precisa de menos iterações de Lloyd
  (Spam, Tabela 6). O segundo artigo localiza também o gargalo paralelo: o passo de Lloyd "é fácil" de
  paralelizar (cada ponto escolhe o centro mais próximo de forma independente e o novo centro é uma
  média), mas a semeadura D² exige k passes, e o k-means‖ troca isso por O(log ψ) rodadas de
  amostragem independente. Quanto às ressalvas, os experimentos de Arthur & Vassilvitskii se descrevem
  como preliminares e o PDF é o manuscrito do autor. Bahmani et al. afirmam que o tempo "melhora
  linearmente com o número de máquinas" sem variar o número de nós, e comparam num cluster de 1.968
  nós com k de 500 a 1.000, um cenário muito distante de uma máquina e 150 mil pontos. Nenhum dos dois
  mede speedup por *threads* em memória compartilhada, e essa evidência, se existir, está no slot KM3.
- KM3, paralelização distribuída e MapReduce. Três artigos lidos (Dhillon & Modha 2000; Zhao et al.
  2009; Ene et al. 2011). Os dois primeiros descrevem a mesma decomposição do k-means em lote, com
  dois vocabulários: blocos fixos de pontos por trabalhador, centros conhecidos por todos, atribuição
  local ao centro mais próximo com somas e contagens parciais, combinação global (`MPI_Allreduce` em
  Dhillon & Modha; *map*, *combine* e *reduce* em Zhao et al.) e novo centro igual a soma sobre
  contagem. Ene et al. descrevem o mesmo esquema como *Parallel-Lloyd* e afirmam que a solução é
  idêntica à do Lloyd sequencial (p. 16), e Dhillon & Modha fixam os centróides iniciais para isolar o
  efeito do paralelismo. O que mais interessa à Análise de Escalabilidade é que só Dhillon & Modha
  apresentam medidas numéricas por número de processadores (speedup de 15,62 em 16 processadores com
  n = 2²¹ e de 6,22 com n = 2¹³; scaleup quase constante) e uma condição analítica: a comunicação
  fica desprezível quando n é grande frente a P·T_reduce/(3·T_flop). Eles trazem ainda as definições
  de *speedup* e *scaleup* e a regra de medir o tempo por iteração. Zhao et al. mostram curvas de 1 a
  4 computadores, sem valores no texto e sem descrever o conjunto de dados, e Ene et al. medem em
  simulação numa só máquina e sem comunicação. Há também uma família aproximada: Ene et al.
  paralelizam por amostragem (k-median e k-center), com custo 5% a 16,5% maior que o do Lloyd paralelo
  nas tabelas, e o ganho de tempo (18× a 73×) varia com o tamanho do conjunto. Aqui vale a tabela, e
  não o "cerca de 20×" do texto. Uma inconsistência interna foi resolvida: o texto de Dhillon & Modha
  cita 2¹¹ como o menor conjunto, mas os dados da Fig. 3 correspondem a 2¹³. A lacuna é que Dhillon &
  Modha dizem que a adaptação a memória compartilhada com *threads* é direta (p. 14–15), mas não a
  medem, e nenhum outro artigo lido mede speedup por *threads* de k-means, que é justamente o nosso
  cenário. Correções ao referencial: o Ene et al. é sobre k-median e k-center, sem *coresets*, e não
  cita Dhillon & Modha nem Zhao et al.
- KM4, escala prática (mini-lote, MLlib). Dois artigos lidos (Sculley 2010; Meng et al. 2016). Sculley
  propõe o k-means em mini-lotes, uma alternativa estocástica que troca qualidade por custo por
  iteração. Em RCV1 (781.265 exemplos), chega a um valor quase ótimo várias ordens de grandeza mais
  rápido que o lote, mas é sequencial, para dados esparsos de texto, e não quantifica a perda além de
  curvas. Não reproduz o resultado do lote. Meng et al. descrevem a biblioteca MLlib. O k-means
  aparece só numa lista de métodos, e o ganho de 2,5× do KMeans na Fig. 2(b) é entre versões da
  biblioteca e não entre números de nós. O artigo não descreve o algoritmo de k-means do MLlib (nem
  *broadcast* de centróides, nem inicialização, nem k-means‖), o que contradiz o que o referencial
  original afirmava. O slot serve só de contexto: mostra que existem alternativas aproximadas e
  implementações de plataforma, sem sustentar decisões sobre o algoritmo implementado.
- DB1, formulação original do DBSCAN. Dois artigos lidos (Ester et al. 1996; Schubert et al. 2017). O
  primeiro define o modelo (vizinhança-Eps, núcleo, borda, ruído, alcançabilidade e conexão por
  densidade) e o algoritmo (varredura com expansão por lista de sementes, uma consulta de vizinhança
  por ponto). O segundo corrige a leitura da complexidade: o custo é O(C + n·Q + Σrᵢ) e Θ(n²) sem
  índice. O O(n log n) de Ester et al. supõe consultas O(log n) e não é garantia, mas o O(n²) no pior
  caso já era conhecido. O ponto delicado para a paralelização é a ordem: o resultado é
  determinístico, mas um ponto de borda alcançável de dois grupos vai para o primeiro que o descobrir,
  e os rótulos mudam com a ordem (Schubert et al., 19:4). Isso define o que significa "mesmo
  resultado" em paralelo. Quanto aos parâmetros, minPts = 4 em 2D (Ester) ou 2·dim (Schubert), e ε
  pelo gráfico *k*-dist. Schubert acrescenta sinais de alerta (ruído entre 1% e 30% e maior componente
  com no máximo 20% a 50% dos agrupados). Ester testa até 12.512 pontos e Schubert testa conjuntos
  maiores, mas sequenciais, e nenhum fala de paralelismo. Correção ao referencial: "O(n log n) com
  índice, O(n²) sem" é uma simplificação, e vale o custo O(C + n·Q + Σrᵢ).
- DB2, memória compartilhada e conjuntos disjuntos. Dois artigos (Patwary et al. 2012; Wang et al.
  2020), com duas filosofias. Patwary et al. mantêm a consulta de vizinhança por ponto e mudam como as
  árvores de agrupamento se unem (*union-find* com travas por ponto, em ordem arbitrária). No
  PDSDBSCAN-S, cada *thread* trata os seus pontos e as ligações entre partições são resolvidas numa
  segunda fase paralela. Medem speedup até 25,97 em 40 núcleos (para até 1,9 milhão de pontos), sem
  contar a construção da kd-tree, com os conjuntos reais pequenos saturando em torno de 8 núcleos, e
  de 3,35 a 4,12 contra um mestre-escravo próprio. Wang et al. mudam a estrutura do algoritmo
  (células de lado ε/√d, grafo de células, componentes conexos), obtendo trabalho O(n log n) em 2D, e
  a implementação para d ≥ 3 usa BCP quadrático. Medem 5× a 33× sobre a melhor serial e 2× a 89×
  self-relative em 36 núcleos com HT, e mostram que `pdsdbscan` e `hpdbscan` têm bom speedup
  self-relative, mas não superam a serial na maioria dos conjuntos. A lição para o relatório é que o
  speedup self-relative isolado não basta, e que é preciso declarar a base e se a construção do índice
  entra no tempo. Os dois testaram em máquinas de 36 a 48 núcleos e conjuntos de milhões de pontos,
  enquanto o nosso intervalo (até 150 mil pontos) é menor. Correção ao referencial: Wang et al. não
  usam k-d trees para *range queries* sobre pontos, e a k-d tree só acha células vizinhas em d ≥ 3.
- DB3, memória distribuída. Sete artigos (Xu 1999; Götz 2015; He 2011; He 2014; Dai & Lin 2012; Han
  2016; Cordova & Moh 2015). Todos seguem o mesmo esqueleto, que é particionar, agrupar localmente com
  uma margem e fundir, e diferem em três decisões. No particionamento, usam curva de Hilbert com
  dR\*-tree (Xu), grade de lado ε com heurística de custo (Götz), *grid file* por contagem de pontos
  (He 2011), modelo de custo (He 2014), fatia de menos pontos na fronteira (Dai & Lin), BSP por
  contagem de pontos (Cordova & Moh) e faixas de índices, sem partição espacial (Han). Na margem ou
  halo, replicam largura Eps ou ε nas fronteiras (todos exceto Han, que difunde a kd-tree inteira). Na
  fusão, usam candidatos no mestre (Xu), regras de equivalência (Götz), pontos de fusão com teorema
  (He), lista de fusão (Dai), aliases (Cordova) e SEEDs no *driver* (Han). Dois resultados se
  repetem. O desbalanceamento com dados enviesados é a causa central da perda de speedup (Xu: birch3;
  He 2014: 9.558 s contra 32.435 s e 80.944 s; Götz: heurística de custo), e a fusão centralizada
  custa caro (Han: speedup total 5,6 contra 10,2 dos executores). Os números não são comparáveis entre
  artigos, porque as bases diferem (1 computador em Xu; 1 núcleo em Götz; 4 tarefas em He 2014; só
  executores em Han) e vários resultados estão apenas em gráficos. Nenhum usa Java em uma só máquina.
  Correções ao referencial: He 2011 não usa kd-tree nem amostragem (grid file e perfil de contagem);
  Cordova & Moh particionam por contagem de pontos e não por grade; Han et al. não particionam
  espacialmente nem redistribuem pontos de borda; Xu et al. usam de fato dR\*-tree.
- DB4, GPU. Dois artigos (Böhm et al. 2009; Andrade et al. 2013). Böhm et al. mantêm a expansão do
  DBSCAN e a paralelizam por cadeias, com matriz de colisões e um índice simples, e têm speedup de 3,5
  a ≈ 15 sobre um DBSCAN de uma só *thread*, dependente de n, de MinPts e do índice. Andrade et al.
  reformulam em grafo de vizinhança e BFS, com 112× sobre o sequencial O(V²) dos próprios autores. Os
  15× e os 112× não são comparáveis (bases, hardware e n diferentes). O slot é contraste: mostra que o
  ganho depende do índice e da linha de base, e não descreve o nosso cenário.

### Quadro comparativo das paralelizações do DBSCAN

| Artigo | Modelo e hardware | Particionamento | Fusão / ligação | Dados | Speedup e base |
|---|---|---|---|---|---|
| Patwary 2012 | OpenMP (40 núcleos) e MPI (Hopper) | pontos iguais por *thread*; kd-tree na versão MPI | *union-find*; travas por ponto | ≤ 1,9 milhão (OpenMP); ≤ 115,9 milhões (MPI) | 25,97 (40 núcleos), 5.765 (8.192 núcleos); self-relative, sem a kd-tree |
| Wang 2020 | Cilk (36 núcleos com HT) | células de lado ε/√d | grafo de células, componentes conexos | 10 milhões a 4,4 bilhões | 5–33× sobre a melhor serial; 2–89× self-relative |
| Xu 1999 | PVM, 8 estações | curva de Hilbert, dR\*-tree | candidatos de fusão no mestre | 10 mil a 1 milhão; birch; SEQUOIA | "quase linear" (só figuras); 1 computador |
| Götz 2015 | MPI + OpenMP (768 núcleos) | grade ε, custo, halo | regras de equivalência | 16,6 e 81,3 milhões | 460,0 e 263,8; o mesmo programa em 1 núcleo |
| He 2011 | Hadoop (13 nós) | *grid file*, halo Eps | pontos de fusão; global em 1 *thread* | 1,9 bilhão (GPS) | "quase linear" no menor conjunto (curvas) |
| He 2014 | Hadoop (128 máquinas) | modelo de custo (CBP) | pontos de fusão; 1 *reducer* | 1,2 + 0,8 bilhão | 71% de eficiência em 64 tarefas; base 4 tarefas |
| Dai & Lin 2012 | Hadoop (10 nós) | PRBP, fatias de 2ε | lista de fusão por interseção | ≤ 1 milhão | 21–37% sobre GRIDBSCAN (4 nós; gráficos) |
| Han 2016 | Spark (Edison, 512 núcleos) | faixas de índices, kd-tree difundida | SEEDs; sequencial no *driver* | ≤ 1 milhão | 137 só dos executores (aproximado); 5,6 total (100k, 32 núcleos) |
| Cordova 2015 | Spark (5 VMs) | BSP por contagem, margem ε | aliases + grafo (sequencial) | 1 milhão, 2-D | sem valores (curva) |
| Böhm 2009 | GPU GTX 280 | cadeias simultâneas | matriz de colisões | ≤ 2 milhões | 3,5–15; DBSCAN de 1 *thread* |
| Andrade 2013 | GPU Tesla M2050 | uma *thread* por vértice | BFS sobre o grafo | ≤ 700 mil | 112; sequencial O(V²) próprio |

Lacuna comum aos 13 artigos de DBSCAN: nenhum mede *threads* Java em uma só máquina, nenhum usa o nosso
intervalo de n (até 150 mil pontos) com speedup por *threads* como foco, e vários dos resultados
numéricos estão em figuras. A evidência mais próxima é a de Patwary et al. (memória compartilhada,
conjuntos reais pequenos saturando em ≈ 8 núcleos) e a de Wang et al. (speedup contra a melhor serial).

---

## 7. Posicionamento da implementação

No PASSO/CURA esta seção é "lacuna e posicionamento". Aqui o objetivo é outro: ligar cada decisão de
projeto à literatura que a sustenta ou limita, sem decidi-la.

### Decisões de projeto e literatura (k-means, após a leitura dos 9 artigos)

| Questão de projeto | O que a literatura lida diz | Fonte |
|---|---|---|
| Que variante de k-means se implementa e como nomeá-la | Sequencial (MacQueen) ou lote de dois passos (MacQueen §3.6, Forgy–Jennrich; "iteração de Lloyd" em Arthur e Bahmani) | MacQueen p. 283, 294; Arthur §2.1 |
| Critério de parada e terminação | O laço em lote termina em número finito de passos, e o tempo pode ser exponencial no pior caso | MacQueen p. 294; Arthur p. 1; Bahmani p. 622 |
| Qualidade e mínimos locais | Não há garantia de ótimo global, e o k-means++ garante O(log k) em esperança na semeadura | MacQueen p. 282, 288; Lloyd p. 131; Arthur Teor. 3.1 |
| Estratégia de inicialização | Aleatória uniforme ou D², com ganho em dados gaussianos bem separados e custo de k passes | Arthur §2.2, §6; Bahmani p. 623 |
| Onde o paralelismo do k-means é trivial | A atribuição por ponto é independente e o centro novo é a média, mas as iterações em si são seriais | Bahmani p. 622; Zhao p. 675 |
| Como particionar e combinar | Blocos de n/P pontos por trabalhador, com somas e contagens parciais por centro combinadas a cada iteração (`MPI_Allreduce` ou *map*/*combine*/*reduce*), e o resultado coincide com o sequencial | Dhillon & Modha p. 5–7; Zhao p. 676–677; Ene p. 16 |
| Quando o paralelismo compensa | O ganho melhora com n, pela condição P·T_reduce/(3·T_flop) ≪ n, com speedup de 6,22 (n = 2¹³) a 15,62 (n = 2²¹) em 16 processadores | Dhillon & Modha p. 8–9 |
| Como medir escalabilidade | *Speedup* = T(1)/T(P); *scaleup* com tempo por iteração; centróides iniciais fixos; tempos sem leitura de disco. Há resultados numéricos só em Dhillon & Modha, curvas em Zhao e simulação em Ene | Dhillon & Modha p. 8–9, 11; Zhao p. 677–678; Ene p. 17 |
| Memória compartilhada com *threads* | Descrita como adaptação direta (blocos de pontos, cópia dos centróides por *thread*), sem medida em nenhum artigo lido | Dhillon & Modha p. 14–15 (lacuna) |
| Alternativas aproximadas | Amostragem com garantia (k-median e k-center) e mini-lotes estocásticos, que trocam qualidade por tempo | Ene p. 3, 14, 17; Sculley p. 1177–1178 |

### Decisões de projeto e literatura (DBSCAN, após a leitura dos 13 artigos)

As páginas "do PDF" são as de PDFs sem paginação do veículo (Patwary, Götz, He 2011).

| Questão de projeto | O que a literatura lida diz | Fonte |
|---|---|---|
| Definição que se implementa | Vizinhança fechada (≤ ε); núcleo com pelo menos minPts pontos (incluindo o próprio); borda e ruído; agrupamento maximal | Ester p. 227–228; Schubert 19:2–19:5; Götz Def. 1 |
| Custo do sequencial | Uma consulta por ponto, com custo O(C + n·Q + Σrᵢ) e Θ(n²) sem índice. O(n log n) só vale como observação com índice eficaz, sem garantia | Schubert 19:3, 19:5–19:9; Ester p. 229 |
| Escolha de ε e minPts | minPts = 4 (2D) ou 2·dim, ε pelo gráfico *k*-dist e sinais de alerta para resultados degenerados | Ester p. 230; Schubert 19:11–19:13 |
| Pontos de borda e ordem | Uma borda compartilhada vai ao primeiro grupo que a alcança, o resultado depende da ordem, e a versão paralela pode diferir só nesses pontos | Ester p. 229–230; Schubert 19:4; Patwary p. 5 (PDF) |
| Onde está o paralelismo | A consulta de vizinhança por ponto é independente e a expansão do agrupamento é sequencial. *Union-find* ou grafo de células a tornam paralela | Patwary p. 1–4 (PDF); Wang p. 2556–2558 |
| Memória compartilhada | Pontos divididos entre *threads*, com as ligações entre partições resolvidas depois por *union* e travas por ponto, ou células e grafo | Patwary p. 4–5 (PDF); Wang p. 2558–2561 |
| Índice espacial | kd-tree (Patwary, Han), R\*-tree e dR\*-tree (Ester, Xu), grade de lado ε (Götz, Wang). A construção do índice não entra no speedup de Patwary | Patwary p. 8 (PDF); Götz §4.1; Wang §3 |
| Particionamento | Pontos iguais, curva de Hilbert, grade, contagem, modelo de custo ou faixas de índices. Com dados enviesados, a contagem desbalanceia | Xu p. 268–273; He 2014 p. 84, 88, 94–96; Götz p. 4–5 (PDF); Han p. 1401 |
| Halo e margem | A réplica de largura ε nas fronteiras evita comunicação na fase local, ao custo de pontos duplicados e de memória | Xu p. 270; Götz p. 2, 6–7 (PDF); He 2011 p. 3–4 (PDF); Dai p. 61–63; Patwary p. 6, 10 (PDF) |
| Fusão | Candidatos no mestre, regras, pontos de fusão com teorema, lista por interseção, aliases ou SEEDs. Quatro dos sete artigos de DB3 têm etapa sequencial | Xu p. 274–282; Götz p. 6 (PDF); He 2011/2014; Dai p. 61; Cordova p. 537; Han p. 1397–1399 |
| Exatidão do resultado | Xu, He, Dai e Cordova afirmam equivalência (provas parciais ou ausentes), Han usa poda no maior conjunto, e Böhm e Andrade não demonstram | Xu p. 282; He 2014 p. 84; Dai p. 64; Han p. 1400; Böhm p. 661 |
| Como medir speedup | Declarar a base (1 núcleo do mesmo programa, melhor serial ou 4 tarefas), separar executores e total, e dizer se a construção do índice entra | Wang p. 2565; Han p. 1400; He 2014 p. 96; Patwary p. 8 (PDF) |
| Evidência em memória compartilhada | 25,97 em 40 núcleos (≤ 1,9 milhão) sobre o próprio 1 núcleo, e 5–33× sobre a melhor serial em 36 núcleos com HT. Conjuntos pequenos saturam em ≈ 8 núcleos | Patwary p. 8 (PDF); Wang p. 2565 |
| GPU | Ganhos de 3,5× a 112×, dependentes de índice, parâmetros e base sequencial | Böhm p. 668; Andrade p. 376 |

### Artigos-âncora por seção do relatório

k-means:

- §1 Introdução: MacQueen 1967 e Lloyd 1982 (origem), Bahmani et al. 2012 e Dhillon & Modha 2000 (por
  que escalar o k-means) e Sculley 2010 (alternativas aproximadas), como contexto.
- §2 Algoritmos Implementados, k-means: MacQueen §3.6 e Arthur & Vassilvitskii §2.1 (variante em lote),
  Arthur & Vassilvitskii §2.2 e §6 (inicialização), Dhillon & Modha §3 (decomposição por dados) e Zhao
  et al. §2.2 (mesma ideia em *map*/*combine*/*reduce*).
- §3 Setup Experimental: Arthur & Vassilvitskii §6.1, Bahmani §4 e Dhillon & Modha §4.1 (como a
  literatura descreve datasets sintéticos, hardware e controles: centróides iniciais fixos, sem I/O).
- §4 Análise de Escalabilidade: Dhillon & Modha §4 (definições e resultados de *speedup* e *scaleup*),
  Bahmani §5 (tempo e iterações até convergir) e Zhao §3 e Ene §4 (ressalvas sobre como outros
  reportam: curvas sem valores, simulação sem comunicação).
- §5 Conclusões e Discussão: a lacuna de medidas de *speedup* por *threads* em memória compartilhada
  (Dhillon & Modha p. 14–15 apenas propõem), e Ene et al. e Sculley como contraste entre exato e
  aproximado.

DBSCAN:

- §1 Introdução: Ester et al. 1996 (modelo e motivação), Schubert et al. 2017 (custo e limites) e
  Patwary et al. 2012, Götz et al. 2015 e Wang et al. 2020 (por que paralelizar).
- §2 Algoritmos Implementados, DBSCAN: Ester et al. §3–4 e Schubert et al. §2 (modelo, algoritmo e
  custo), Patwary et al. §III–IV (*union-find* e fase de fusão), Wang et al. §3–6 (células e grafo de
  células) e Götz et al. §4, He et al. 2011 e 2014 e Xu et al. §4 (particionamento, halo e fusão).
- §3 Setup Experimental: Schubert et al. §4 (parâmetros, sinais de alerta e comparação de índices),
  Patwary et al. §V e Wang et al. §7 (conjuntos, hardware e baselines) e Götz et al. §6 (dados reais).
- §4 Análise de Escalabilidade: Patwary et al. §V (speedup self-relative, kd-tree fora do tempo), Wang
  et al. §7 (melhor serial como base), He et al. 2014 §6 (desbalanceamento), Han et al. §V (executores
  e total) e Böhm et al. e Andrade et al. (contraste de GPU e de bases).
- §5 Conclusões e Discussão: a lacuna de medidas de *threads* Java em uma só máquina e para n ≤ 150
  mil, e Schubert et al. sobre a dependência de ε e do índice.

---

## 8. Changelog

- 22/10/2026: documento criado. A estrutura foi replicada do PASSO/CURA (template, catálogo, revisão,
  fichamentos, `.bib`), com um único `fichamentos.md` escrito em duas etapas (k-means, depois DBSCAN).
  As decisões do grupo estão registradas na §1.
- 22/10/2026: verificação dos 22 PDFs (cabeçalho, contagem de páginas, Crossref). Todos são os
  artigos esperados. Divergências corrigidas ou registradas:
  - Han et al. é IPDPSW 2016, p. 1393–1402. A lista do grupo dizia 2018 e o `referencial_teorico.md`
    dizia p. 271–280. Corrigido na ficha da 2ª etapa.
  - Quatro PDFs estão no repositório mas não na lista inicial: Sculley 2010, Ene et al. 2011, Schubert
    et al. 2017 e He et al. 2014. Foram mantidos por decisão do grupo.
  - O PDF de Arthur & Vassilvitskii é o manuscrito do autor (sem veículo). Os PDFs de Bahmani et al. e
    Ene et al. são do arXiv (Bahmani com rodapé da VLDB, e Ene na versão estendida de 20 p.).
  - Dhillon & Modha é um capítulo do LNCS 1759 (2000), originado no workshop do KDD'99. O Crossref data
    de 2002.
  - Afirmações do `referencial_teorico.md` que os PDFs não sustentam (a conferir na leitura de cada
    um): "coresets" em Ene et al. (a palavra não aparece, e o artigo trata k-center e k-median por
    amostragem); "k-d trees" como estrutura principal em Wang et al. (aparece 4 vezes, e as estruturas
    centrais são grade, quadtree, caixas e Delaunay); descrição detalhada de k-means distribuído em
    Meng et al. (k-means só aparece numa lista de métodos); partição em "grade" em Cordova & Moh (a
    palavra "grid" não aparece no PDF). Retificação feita na 2ª etapa: a nota de que o PDF de Xu et
    al. diria "dR-tree" estava errada (a extração automática do texto perdera o asterisco), e a
    leitura integral confirmou dR\*-tree.
- 22/10/2026: leitura e fichamento de KM1 (MacQueen, Lloyd) e KM2 (Arthur & Vassilvitskii, Bahmani et
  al.), com 24 citações literais conferidas no texto extraído. Correções ao `referencial_teorico.md`
  vindas da leitura:
  - MacQueen: o procedimento do artigo é sequencial, e o laço em lote é a §3.6, atribuído a Forgy e
    Jennrich. O referencial atribuía o "algoritmo de dois passos" só a Lloyd.
  - Lloyd: o artigo trata de quantização escalar e não descreve agrupamento de pontos em R^d. As
    páginas corretas são 129–137 (Arthur & Vassilvitskii as citam como 129–136), e a p. 137 do PDF é
    de outro artigo (Newman).
  - Bahmani et al.: o artigo não menciona o MLlib/Spark (cita Hadoop e Mahout), embora o referencial
    afirmasse que o k-means‖ é a inicialização do MLlib. Isso ficou para conferir em Meng et al.
  - Bahmani et al.: a afirmação de que o k-means‖ escala linearmente com o número de máquinas não é
    sustentada por experimento do próprio artigo.
- 22/10/2026: criado o §9 (registro de inconsistências encontradas nos artigos e valor adotado), que
  reúne o que estava nas fichas e no changelog. A regra também entrou no template (§3).
- 22/10/2026: leitura e fichamento de KM3 (Dhillon & Modha, Zhao et al., Ene et al.) e KM4 (Sculley,
  Meng et al.), o que fecha a 1ª etapa (k-means). Foram conferidas 55 citações literais no texto
  extraído (24 de KM1 e KM2 e 31 de KM3 e KM4). Onde o texto diverge de figura ou tabela, vale o dado
  observado, e a divergência fica registrada. Resolvido:
  - Dhillon & Modha: o texto fala em n = 2¹¹ como o menor conjunto, mas a legenda e os traços
    vetoriais da Fig. 3 correspondem a 2¹³ (os tempos com P = 1 seguem n·I e reproduzem os speedups de
    15,6 e 6,2). Adotado 2¹³.
  - Ene et al.: o texto fala em "cerca de 20×" sobre o Lloyd paralelo e em perda "desprezível", mas as
    tabelas dão 18× a 73× e custo 5% a 16,5% maior. Adotadas as tabelas. Também ficou registrado que o
    tempo do *Parallel-Lloyd* com 10 milhões de pontos (702,3 s) é menor que com 5 milhões (1.333,3
    s), sem explicação.

  Correções ao `referencial_teorico.md` vindas da leitura:
  - Ene et al.: trata de k-median e k-center (k-means só numa frase de conclusão), não há *coresets*
    (a técnica é amostragem), o artigo não cita Dhillon & Modha nem Zhao et al., e as experiências
    são simulações numa máquina, sem comunicação.
  - Meng et al.: o k-means aparece uma única vez, numa lista. Não há descrição de *broadcast* de
    centróides, de inicialização ou de k-means‖, e a Fig. 2(b) dá 2,5× para o KMeans entre as
    versões 1.0 e 1.1 da biblioteca.
  - Zhao et al.: o referencial omitia o *combiner*, e o conjunto de dados do experimento não é
    descrito.
  - Sculley: o pôster tem uma segunda contribuição (centros esparsos por projeção L1) que o
    referencial não mencionava.
  - Dhillon & Modha: o mecanismo é `MPI_Allreduce` e não *map*/*reduce*, e a adaptação a memória
    compartilhada é descrita (p. 14–15), mas não medida.
- 22/10/2026: leitura e fichamento da 2ª etapa (DBSCAN): DB1 (Ester et al., Schubert et al.), DB2
  (Patwary et al., Wang et al.), DB3 (Xu et al., Götz et al., He et al. 2011 e 2014, Dai & Lin, Han et
  al., Cordova & Moh) e DB4 (Böhm et al., Andrade et al.). Foram conferidas 136 citações literais novas
  no texto extraído (191 no total). O critério é o mesmo da 1ª etapa: vale o dado observado. As
  divergências internas estão no §9 (itens 10 a 39). Correções ao `referencial_teorico.md` vindas da
  leitura:
  - Schubert et al.: o custo do DBSCAN é O(C + n·Q + Σrᵢ), com Θ(n²) sem índice, e o O(n log n) é
    informal ("formalmente incorreto" como garantia). O referencial dizia "O(n log n) com índice,
    O(n²) sem".
  - Wang et al.: a k-d tree só acha células vizinhas em d ≥ 3 (e não indexa pontos para *range
    queries*), a contagem usa quadtrees e a implementação para d ≥ 3 é quadrática.
  - Xu et al.: dR\*-tree confirmada, o que corrige a nota do changelog da verificação dos PDFs.
  - He et al. 2011: sem kd-tree nem amostragem (partição por *grid file* com perfil de contagem por
    MapReduce). O modelo de custo é do artigo de 2014.
  - Cordova & Moh: BSP por contagem de pontos e não grade, restrito a 2-D, com rotulagem global
    sequencial.
  - Han et al.: ano 2016, p. 1393–1402. O artigo não particiona espacialmente nem redistribui pontos
    de borda (faixas de índices e SEEDs), e a fusão é sequencial no *driver*.
  - Götz et al.: a comparação "uma ordem de grandeza" é só com o PDSDBSCAN-D no Twitter pequeno.
  - Böhm et al. e Andrade et al.: speedups de 3,5 a ≈ 15 e de 112×, sobre bases diferentes.
- 22/10/2026: fechamento da revisão. Foram consolidados o catálogo DBSCAN em `artigos/README.md`, 13
  entradas novas em `referencias.bib` (22 no total, sem chave repetida e com cada chave usada em uma
  ficha), o quadro comparativo e a tabela de decisões do DBSCAN (§6 e §7), as âncoras por seção do
  relatório e a lacuna comum (nenhum artigo mede *threads* Java em uma só máquina). O
  `referencial_teorico.md` foi absorvido por esta síntese e removido, e o fascículo de Xu et al. (v. 3,
  n. 3) foi confirmado no Crossref.

---

## 9. Registro de inconsistências encontradas nos artigos e valor adotado

Critério: quando duas partes do mesmo artigo divergem (texto e figura ou tabela), ou quando um artigo
cita outro com dado diferente do que está no PDF deste, vale o dado observado. A ordem de preferência
é a figura, a tabela, os traços vetoriais da figura ou o cabeçalho do próprio PDF, todos acima da
frase de resumo. A divergência não é apagada: fica aqui e na ficha do artigo, e a citação no relatório
deve usar o valor adotado. As anotações do `referencial_teorico.md` que os PDFs não sustentam ficam no
changelog (§8), e não neste registro.

| # | Artigo | Inconsistência encontrada | Valor adotado | Evidência | Documentado em |
|---|---|---|---|---|---|
| 1 | Dhillon & Modha (2000) | O texto (p. 9) chama de menor conjunto o de n = 2¹¹ e fala em crescer de 2¹¹ a 2²¹, mas a legenda da Fig. 3 e as curvas listam cinco conjuntos, de 2¹³ a 2²¹. | n mínimo = 2¹³ (speedup de 6,22 em 16 processadores) | Na Fig. 3, os tempos com P = 1 dos cinco conjuntos seguem n·I (I = 3, 10, 8, 164, 50) e as razões tempo(1)/tempo(16) lidas na figura reproduzem 15,6 e 6,2. Com 2¹¹ não reproduziriam. | `fichamentos.md` (ficha Dhillon & Modha); §6 KM3; §8 |
| 2 | Dhillon & Modha (2000) | O resumo diz que, num conjunto de 2 GB, o algoritmo chega a "mais de 1,8 gigaflops", mas o corpo diz ≈ 1,2 Gflops ao escalar n (p. 13) e "quase 1,8" ao escalar d (p. 14). | ≈ 1,2 Gflops (escalando n) e ≈ 1,8 Gflops (escalando d). O resumo se refere ao segundo caso. | Texto das p. 13–14. | `fichamentos.md` (ficha Dhillon & Modha, scaleup) |
| 3 | Ene et al. (2011) | O texto resume o ganho do *Sampling-Lloyd* sobre o *Parallel-Lloyd* como "cerca de 20×" (p. 3 e 17). | 18× a 73×, conforme n (≈ 51× com 1 milhão, ≈ 57× com 2 milhões, ≈ 73× com 5 milhões, ≈ 18× com 10 milhões; razões calculadas a partir das tabelas) | Tabelas das Fig. 1–2 (p. 14). O "20×" só corresponde ao menor ganho, o de 10 milhões. | `fichamentos.md` (ficha Ene et al.); §6 KM3; §8 |
| 4 | Ene et al. (2011) | O texto fala em perda "pequena" ou "desprezível" de qualidade. | Custo de 5% a 16,5% maior que o do Lloyd paralelo (13,2% com 1 milhão de pontos; 7,3% com 10 milhões) | Linhas de custo normalizado das Fig. 1–2. | idem |
| 5 | Ene et al. (2011) | O tempo do *Parallel-Lloyd* com 10 milhões de pontos (702,3 s) é menor que o com 5 milhões (1.333,3 s), e o texto não comenta. | Registrado como observado, sem explicação. Não se infere tendência de tempo a partir desse ponto, e o número de iterações do Lloyd não é informado. | Tabela da Fig. 2. | idem |
| 6 | Zhao et al. (2009) | A frase "o conjunto é dividido e globalmente difundido a todos os *mappers*" (p. 676) contradiz os pseudocódigos, em que o que todos os *mappers* conhecem é o vetor `centers`. | O que é difundido é o vetor `centers`, e o conjunto é dividido entre os *mappers*. | Algoritmo 1 e texto que o acompanha (p. 676). | `fichamentos.md` (ficha Zhao et al., limitações) |
| 7 | Bahmani et al. (2012) | O texto afirma que o tempo do k-means‖ "melhora linearmente com o número de máquinas" (p. 627), mas nenhum experimento varia o número de nós. | Tratada como afirmação não medida, e não citada como resultado experimental. | Seções 4–5: comparam algoritmos num cluster fixo de 1.968 nós. | `fichamentos.md` (ficha Bahmani et al., limitações); §5 e §6 KM2 |
| 8 | Arthur & Vassilvitskii (2007), citando Lloyd | A lista de referências cita Lloyd (1982) com páginas 129–136. | 129–137 | Cabeçalho do PDF do Lloyd (p. 129 a 137) e Crossref. | `fichamentos.md` (ficha Lloyd, limitações); §8 |
| 9 | Han et al. (lista do grupo e PDF) | A lista inicial do grupo traz o ano 2018 e o `referencial_teorico.md` traz as páginas 271–280. | IPDPSW 2016, p. 1393–1402 | Cabeçalho do PDF (2016 IEEE IPDPSW), DOI 10.1109/IPDPSW.2016.57 e Crossref. | `fichamentos.md` (ficha Han et al.); §8 |
| 10 | Ester et al. (1996) | O resumo diz que o DBSCAN supera o CLARANS "por um fator de mais de 100", e o texto (p. 231) diz "entre 250 e 1900". | ≈ 245× a ≈ 1.934×, crescente com n | Razões calculadas a partir da Tabela 1 (1.252 a 12.512 pontos). | `fichamentos.md` (ficha Ester et al.) |
| 11 | Patwary et al. (2012) | O resumo e a conclusão associam o speedup de 25,97 em 40 núcleos a "conjuntos de até centenas de milhões de pontos". | 25,97 em 40 núcleos para ≈ 1,9 milhão de pontos (c1.9m) | Seção V-A: memória compartilhada com até 1,9 milhão de pontos. Os conjuntos de dezenas de milhões (máx. 115,9 milhões) só aparecem na versão distribuída. | `fichamentos.md` (ficha Patwary et al.) |
| 12 | Patwary et al. (2012) | O resumo apresenta "5.765 com 8.192 núcleos" como resultado geral. | Melhor caso (conjunto *mm*, 72,3 milhões de pontos), citado sempre com o conjunto | Fig. 6 e texto das p. 9–10 (PDF): nos demais, o speedup é menor. | idem |
| 13 | Patwary et al. (2012) | O texto dá "0,91% a 1,88%" para a fração da construção da kd-tree nos sintéticos agrupados e, na frase seguinte, "0,23% a 1,23%" com o mesmo rótulo. | 0,91%–1,88% para os agrupados, com a segunda faixa tratada como a dos aleatórios (inferência nossa) | A Fig. 3(a) se refere aos agrupados. | idem |
| 14 | Patwary et al. (2012) | A Tabela II traz o mesmo tempo de 1 núcleo (4.121,75 s) para c61m e r115.9m. | Registrado como aparece, e nenhum cálculo usa esses dois valores | Tabela II. | idem |
| 15 | Wang et al. (2020) | O resumo diz que os algoritmos igualam os limites de trabalho dos melhores sequenciais, mas para d ≥ 3 exato a implementação usa BCP quadrático. | Os limites teóricos valem para os algoritmos, e as medidas de d ≥ 3 valem para a implementação quadrática. | p. 2563. | `fichamentos.md` (ficha Wang et al.) |
| 16 | Wang et al. (2020) | Os speedups "de até 33×" (sobre a melhor serial) e "de até 89×" (*self-relative*) misturam bases. | Citar sempre a base. O 89× inclui *hyper-threading* (72 *threads* em 36 núcleos). | p. 2565. | idem |
| 17 | Xu et al. (1999) | O resumo e a §5.2 dizem Ethernet de 10 Mbit/s, e a §5 diz "10 MB LAN". | 10 Mbit/s | É a unidade usual da tecnologia, repetida duas vezes no texto (p. 282). | `fichamentos.md` (ficha Xu et al.) |
| 18 | Götz et al. (2015) | A Definição 1 diz vizinhança "menor ou igual a ε", mas a fórmula escreve `dist(p, q) < ε`. | ≤ ε | Prosa da definição e Ester et al. (p. 227). | `fichamentos.md` (ficha Götz et al.) |
| 19 | Götz et al. (2015) | O resumo fala em "uma ordem de grandeza" de melhoria sobre as tentativas anteriores. | A comparação é só com o PDSDBSCAN-D, no Twitter pequeno, e deve ser citada assim. | §6.6: os maiores conjuntos esgotaram a memória do PDSDBSCAN-D, e os autores dizem não ter reproduzido o desempenho publicado. | idem |
| 20 | He et al. (2011) | A introdução descreve os dados como "1,9 bilhão de registros de táxis de Xangai de um mês", e a avaliação diz "ao longo de dois anos". | Dois anos | Seção de avaliação (p. 6), onde os dados são usados. | `fichamentos.md` (ficha He et al. 2011) |
| 21 | He et al. (2011) | A definição de "diretamente alcançável" usa `Card(N_Eps(o)) ≤ MinPts`, e a de núcleo usa `≥ MinPts`. | ≥ MinPts | Definição de núcleo de Ester et al. | idem |
| 22 | He et al. (2011) | A introdução afirma que o DBSCAN é "insensível à ordem dos pontos". | Só vale para núcleos e ruído, e a borda compartilhada depende da ordem. | Ester et al. p. 230; Schubert et al. 19:4. | idem |
| 23 | He et al. (2014) | O resumo diz que todos os subprocedimentos críticos são totalmente paralelizados. | As etapas que dependem do tamanho dos dados são paralelas, e o mapeamento global usa um único *reducer*. | §5 (p. 92). | `fichamentos.md` (ficha He et al. 2014) |
| 24 | He et al. (2014) | O resumo fala em "uma ordem de grandeza" de melhoria. | Até ≈ 18× sobre o RBP e ≈ 3,4× sobre o ESP (TIGER-5) | Tabelas 2–5. | idem |
| 25 | He et al. (2014) | A síntese diz que o CBP é o melhor em todos os experimentos. | CBP melhor em geral, com o ESP mais rápido no G4 (338 s contra 365 s) | Tabela 5. | idem |
| 26 | Dai & Lin (2012) | A lista de dados sintéticos cita "nt5,800k" duas vezes. | nt8,800k como o quarto conjunto | Fig. 7 (nt7_1000k, nt8_800k, nt4_800k, nt5_800k). | `fichamentos.md` (ficha Dai & Lin) |
| 27 | Dai & Lin (2012) | As faixas "21% a 37%" (tempo) e "43% a 77%" (pontos de fronteira) vêm de gráficos, e o texto cita "Figura 2(b)" para o exemplo que é da Fig. 1(b). | Valor tabelado só no nt7_1000k (328.900 contra 114.700), percentuais citados como valores de gráfico e Fig. 1(b) | p. 61 e 65. | idem |
| 28 | Han et al. (2016) | O resumo cita speedups de 6, 10 e 137 sem dizer que são só da computação nos executores. | Speedups dos executores, com o total menor (5,6 no 100k com 32 núcleos) | p. 1400. | `fichamentos.md` (ficha Han et al.) |
| 29 | Han et al. (2016) | O texto afirma que todas as execuções paralelas geram o resultado do serial, mas o maior conjunto usa poda da kd-tree e filtra agrupamentos pequenos. | A igualdade ao serial vale para os conjuntos menores, e o resultado de 1 milhão é aproximado. | p. 1399–1400. | idem |
| 30 | Han et al. (2016) | O resumo e a conclusão dizem "mais de 10 vezes" mais rápido que o MapReduce. | 9× a 17× em 10 mil pontos, contra implementação própria | Tempos da §V-D (178/93/50/31 s contra 1.666/1.248/832/521 s). | idem |
| 31 | Han et al. (2016) | O speedup de 58 a 137 (64 a 512 núcleos, 1 milhão de pontos) não declara a base, e os tempos multiplicados pelos speedups dão 1 núcleo de ≈ 203.000 a 297.000 s. | Base não reconstruível, e os valores não são usados em cálculos. | Figs. 6(b) e 8. | idem |
| 32 | Cordova & Moh (2015) | A lista de definições diz que "noise point: p é um *border point* se...". | Ruído é o ponto que não pertence a nenhum agrupamento. | Definição de borda na linha anterior e restante do artigo. | `fichamentos.md` (ficha Cordova & Moh) |
| 33 | Cordova & Moh (2015) | A conclusão diz que se "provou experimentalmente" a escalabilidade com o número de nós. | O experimento mostra redução do tempo (curva de 2 a 10 núcleos), sem speedup numérico. | Fig. 4. | idem |
| 34 | Böhm et al. (2009) | O resumo e a introdução dizem "tipicamente uma ordem de grandeza". | 3,5× a ≈ 15× (CUDA-DClust\*), com ≥ 10× só acima de 250 mil pontos e 2× com MinPts muito grande | p. 668. | `fichamentos.md` (ficha Böhm et al.) |
| 35 | Böhm et al. (2009) | A legenda da Fig. 7 atribui a dimensionalidade à "Figura 7(c)", que é o impacto de MinPts. | Fig. 7(e) | Legenda (p. 669). | idem |
| 36 | Böhm et al. (2009) | O resumo diz "garantido equivalente" ao DBSCAN e a introdução "pode ser provado idêntico", sem prova no texto. | Afirmação não demonstrada, e não citada como resultado. | Leitura integral. | idem |
| 37 | Andrade et al. (2013) | O texto diz que a complexidade da construção do grafo "pode ser reduzida de O(V²) para O(V)". | O(V²) de trabalho, com O(V) só no caminho crítico paralelo. | O total declarado é O(V²) (p. 373). | `fichamentos.md` (ficha Andrade et al.) |
| 38 | Andrade et al. (2013) | Atribuem ao CUDA-DClust a complexidade O(V log V). | Böhm et al. descrevem n vezes o custo da consulta (sem declarar O(n log n)). | Böhm p. 665. | idem |
| 39 | Andrade et al. (2013) e Böhm et al. (2009) | Comparam 112× com 15× e dizem que a comparação é "justa" porque os tempos sequenciais são próximos. | Valores válidos só dentro de cada artigo, porque bases e hardware diferem. | Andrade p. 376; Böhm p. 668. | idem |
