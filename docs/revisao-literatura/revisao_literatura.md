# Revisão de literatura — k-means e DBSCAN paralelos

> Documento vivo. Organiza a leitura dos 22 artigos de `artigos/`: **o que** cada slot cobre,
> **o que já foi lido**, **o que cada artigo permite afirmar** e **onde** isso entra no relatório.
> Depois vira a fundamentação teórica do relatório (`../../relatorio/template_trabalho_semestre.tex`).
>
> **Objetivo:** mapear, sintetizar e analisar criticamente o conhecimento científico já existente
> sobre k-means e DBSCAN (formulação original e paralelização), para fundamentar a pesquisa e evitar
> a redescoberta do que já foi provado.
>
> **Escopo:** o trabalho da disciplina Computação Escalável (UEL): implementação paralela de k-means
> (Herik, Julia) e de DBSCAN (Adriano, Sofia) em Java (`ExecutorService`/`ForkJoin`, uma máquina) e
> análise de escalabilidade. A revisão é **fundamentação teórica**; não decide nem descreve a
> implementação, que fica com as duplas responsáveis.
>
> **Estado (22/10/2026):** 1ª etapa (k-means) com leitura e fichamento **concluídos**: KM1 (MacQueen,
> Lloyd), KM2 (Arthur & Vassilvitskii, Bahmani et al.), KM3 (Dhillon & Modha, Zhao et al., Ene et al.)
> e KM4 (Sculley, Meng et al.) — 9 artigos. Próximo passo: 2ª etapa (DBSCAN, 13 PDFs), escrita depois
> nestes mesmos arquivos, em commit separado.
>
> **Como usar:** cada artigo lido entra na tabela da §5 e em `referencias.bib`; o PDF fica em
> `artigos/` (catálogo em `artigos/README.md`); a ficha completa fica em `fichamentos.md`.
> Processo em `fichamento_literatura_template.md`.
>
> **Nomenclatura:** "slot" = eixo temático (convenção do PASSO/CURA). Aqui: **KM1–KM4** (k-means) e
> **DB1–DB4** (DBSCAN, 2ª etapa).

---

## 1. De onde saem os slots

1. **Enunciado do trabalho** (README do repositório): clustering com k-means e DBSCAN; paralelismo
   em Java; análise de escalabilidade (tempo × tamanho do dataset); relatório com seções
   Introdução, Algoritmos Implementados, Setup Experimental, Análise de Escalabilidade, Conclusões.
2. **Levantamento prévio do grupo** (`artigos/referencial_teorico.md`, 21/10/2026): 22 artigos em
   duas famílias — formulação original e paralelização/distribuição. Foi escrito **sem leitura
   integral**; suas afirmações estão sendo conferidas e corrigidas (ver §8). Esse documento será
   absorvido por esta síntese e removido no fim da revisão.
3. **Decisão de 22/10/2026:** manter no repositório todos os PDFs, inclusive os que não estavam na
   lista inicial do grupo (Sculley, Ene et al., Schubert et al., He et al. 2014).

Prioridade: todos os slots entram, em ordem de leitura KM1 → KM2 → KM3 → KM4 → DB1 → DB2 → DB3 → DB4.

---

## 2. Slots de busca — 1ª etapa (k-means)

> **Aviso:** o corpus é fixo (22 PDFs já no repositório). "O que extrair" lista o que cada slot
> precisa responder; o que a leitura não responde vira lacuna na §7.

### Slot KM1 — Formulação original do k-means  ·  ler primeiro

- **Por quê:** definir com precisão *o que é* k-means e o que a fonte original garante, antes de
  discutir paralelização.
- **O que extrair:** definição do problema e do critério (variância intra-classe); qual procedimento
  cada fonte descreve (sequencial × lote); garantias de convergência e de otimalidade; terminação;
  sensibilidade à ordem e à partição inicial.
- **PDFs:** MacQueen 1967; Lloyd 1982 (rascunho de 1957).
- **Lidos e fichados (22/10/2026):** ambos.

### Slot KM2 — Inicialização

- **Por quê:** a qualidade e o número de iterações do k-means dependem do ponto de partida; a
  literatura mostra qual estratégia usar e quanto custa em paralelo.
- **O que extrair:** estratégia de semeadura, garantia teórica, custo (passes sobre os dados),
  evidência experimental e as condições em que foi medida.
- **PDFs:** Arthur & Vassilvitskii 2007; Bahmani et al. 2012.
- **Lidos e fichados (22/10/2026):** ambos.

### Slot KM3 — Paralelização em memória distribuída e MapReduce

- **Por quê:** estabelecer o modelo "particionar os pontos; calcular somas parciais por partição;
  combinar globalmente a cada iteração", e como a literatura mede speedup e escalabilidade.
- **O que extrair:** o que é particionado e o que é comunicado por iteração; análise de custo;
  speedup/scaleup medidos (nº de nós, tamanho do dataset); garantias de aproximação.
- **PDFs:** Dhillon & Modha 2000; Zhao et al. 2009; Ene et al. 2011.
- **Lidos e fichados (22/10/2026):** os três.

### Slot KM4 — Escala prática: mini-lote e plataforma

- **Por quê:** alternativas que trocam um pouco de qualidade por custo menor por iteração, e o
  que uma plataforma (Spark) de fato documenta sobre k-means.
- **O que extrair:** o trade-off tempo × qualidade do mini-lote; o que o artigo do MLlib diz (ou
  não diz) sobre a implementação distribuída do k-means.
- **PDFs:** Sculley 2010; Meng et al. 2016.
- **Lidos e fichados (22/10/2026):** ambos.

---

## 3. Bases e ferramentas de verificação

| Ferramenta | Uso |
|---|---|
| Cabeçalho do próprio PDF | título, autores, veículo, DOI, paginação (fonte primária) |
| Crossref (`api.crossref.org/works/<DOI>`) | conferir volume, número, páginas, ano, DOI |
| Busca na web (pesquisa padrão) | DOI/páginas que o PDF não traz (marcar "a conferir") |
| PyMuPDF / `pdftotext` | extrair o texto integral com marcação de página; conferir citações |

Gestor de referências: `referencias.bib` (BibTeX), crescendo à medida que se fichou. O
`relatorio/template_trabalho_semestre.tex` (raiz do repositório) ainda não tem bloco de bibliografia; definir o estilo de
citação antes de preencher a seção de fundamentação.

---

## 4. Fila de leitura (trazidos, ainda não lidos/fichados)

1ª etapa, k-means (dados bibliográficos em `artigos/README.md`). Marcar `[x]` e mover para a §5 ao
terminar.

**Fila da 1ª etapa vazia (22/10/2026):** os 9 artigos de k-means foram lidos e fichados. A fila da 2ª
etapa (DBSCAN: Ester, Schubert, Patwary, Wang, Xu, Götz, He 2011, He 2014, Dai, Han, Cordova, Böhm,
Andrade) será aberta ao iniciá-la.

**Lacuna de fonte:** o resumo de Forgy (1965, *Biometrics* 21:768), citado por MacQueen como origem
do procedimento de dois passos, não está no repositório e não foi lido.

---

## 5. Artigos lidos

Só entra aqui depois do fichamento completo — nunca a partir de título/abstract.

| Ref | Slot | Relevância | O que aproveitar |
|---|---|---|---|
| MacQueen (1967) — *Some methods for classification and analysis of multivariate observations* | KM1 | Origem do termo "k-means". O procedimento do artigo é **sequencial** (uma observação por vez); o laço de **dois passos em lote** aparece na §3.6, atribuído a Forgy e Jennrich, com terminação em número finito de passos. Convergência quase certa só para o procedimento sequencial, e não necessariamente ao ótimo. Experimentos com ≤ 760 pontos. | Citar como origem do termo e do critério; nomear a variante implementada; ressalva de que não trata de paralelismo nem de escala. Ficha em `fichamentos.md`. |
| Lloyd (1982; rascunho de 1957) — *Least squares quantization in PCM* | KM1 | Duas condições necessárias para quantização escalar ótima (centro de massa; fronteira no ponto médio) e iteração alternada que não aumenta o ruído, sem garantia de mínimo global. **Não trata de agrupamento em R^d**; a associação com o k-means é tradição posterior. | Citar a origem histórica das duas condições com a ressalva do escopo; ancorar a descrição do algoritmo em Arthur & Vassilvitskii (§2.1). Ficha em `fichamentos.md`. |
| Arthur & Vassilvitskii (2007) — *k-means++: the advantages of careful seeding* | KM2 | Semeadura D²: E[φ] ≤ 8(ln k + 2)φ_OPT, justa a menos de constante; experimentos preliminares (n até 494.019; 20 execuções) mostram menos erro e menos tempo, com uma exceção no Norm25 (k = 50). PDF é o manuscrito do autor. | Referência da inicialização k-means++; declarar a estratégia adotada no relatório. Ficha em `fichamentos.md`. |
| Bahmani et al. (2012) — *Scalable k-means++* | KM2 | O passo de Lloyd é fácil de paralelizar; a inicialização k-means++ exige k passes. k-means‖: O(log ψ) rodadas, amostragem independente por ponto, reagrupamento dos candidatos; experimentos em Hadoop de 1.968 nós com 4,8 milhões de pontos. A escalabilidade com nº de máquinas é afirmada, não medida. | Justificar onde o paralelismo do k-means é trivial e onde não é; contexto de como a literatura mede escalabilidade. Ficha em `fichamentos.md`. |
| Dhillon & Modha (2000) — *A data-clustering algorithm on distributed memory multiprocessors* | KM3 | Modelo-base de paralelização do k-means em memória distribuída (MPI): blocos de n/P pontos, somas parciais por centróide combinadas por `MPI_Allreduce`; custo ≈ 3nkd por iteração; o ganho cresce com n (condição P·T_reduce/(3·T_flop) ≪ n). SP2 de 16 nós: speedup 15,62 (n = 2²¹) e 6,22 (n = 2¹³); scaleup ≈ constante. Adaptação a *threads* só proposta (p. 14–15), não medida. | Referência do modelo de paralelização e das definições de *speedup* e *scaleup*; lacuna de memória compartilhada. Ficha em `fichamentos.md`. |
| Zhao, Ma & He (2009) — *Parallel k-means clustering based on MapReduce* | KM3 | A mesma decomposição em *map* (atribuição) + *combine* (soma local) + *reduce* (novos centros) no Hadoop. Experimentos de 1 a 4 computadores e 1 a 8 GB, mostrados só em curvas, com o conjunto de dados não descrito. | Tradução do modelo para MapReduce, com o *combiner* como agregação local; ressalva de evidência fraca. Ficha em `fichamentos.md`. |
| Ene, Im & Moseley (2011) — *Fast clustering using MapReduce* | KM3 | **Trata k-median e k-center**, não k-means; paralelização por amostragem com garantia de aproximação. Descreve o Lloyd paralelo exato (p. 16). Experimentos em **simulação numa só máquina, sem comunicação**; Sampling-Lloyd é 18× a 73× mais rápido que o Lloyd paralelo, com custo 5% a 16,5% maior (tabelas). | Contraste entre paralelização exata e aproximada; ressalva sobre medir tempo em simulação. Só contexto para o k-means. Ficha em `fichamentos.md`. |
| Sculley (2010) — *Web-scale k-means clustering* | KM4 | Mini-lote: troca qualidade por custo por iteração; várias ordens de grandeza mais rápido que o lote, com soluções melhores que o SGD, em RCV1 (781.265 exemplos de treino). Pôster de 2 páginas, sequencial. | Contexto do trade-off tempo × qualidade; não se aplica à paralelização do lote. Ficha em `fichamentos.md`. |
| Meng et al. (2016) — *MLlib: machine learning in Apache Spark* | KM4 | Apresenta a biblioteca; **o k-means aparece só numa lista**. Ganho de 2,5× do KMeans entre as versões 1.0 e 1.1 (Fig. 2b), sem descrição do algoritmo, de k nem do tamanho dos dados. | Só contexto (k-means em Spark existe); **não** sustenta nenhuma afirmação sobre o algoritmo distribuído. Ficha em `fichamentos.md`. |

---

## 6. Síntese por tema

*(preencher à medida que se lê; vira o texto da fundamentação do relatório)*

- **KM1. Formulação original.** Dois artigos lidos (MacQueen 1967; Lloyd 1982). O que ambos
  estabelecem é o **par de operações** em torno do qual o k-means é construído: representar cada
  grupo pela média (centro de massa) e atribuir cada ponto ao representante mais próximo (no Lloyd,
  fronteiras no ponto médio entre quanta). Os dois também concordam, de forma independente, que o
  procedimento **não garante o ótimo global**: MacQueen dá um exemplo de convergência com
  probabilidade positiva a uma partição que não minimiza a variância (p. 288), e Lloyd mostra que as
  condições necessárias não são suficientes e dá exemplos com vários mínimos locais (p. 131, 136).
  O ponto delicado é o **nome**: o procedimento do artigo de MacQueen é sequencial (atualiza a média
  a cada observação); o laço de dois passos em lote vem na §3.6 atribuído a Forgy e Jennrich
  (convergência em número finito de passos); e o artigo de Lloyd trata quantização escalar e não menciona
  agrupamento de pontos. Os artigos posteriores (Arthur & Vassilvitskii; Bahmani et al.) chamam o
  laço em lote de "iteração de Lloyd". Nenhum dos dois artigos fala de paralelismo, de custo por
  iteração ou de escala: MacQueen testa no máximo 760 pontos. Consequência para o relatório: dizer
  qual variante foi implementada e citar a fonte certa para ela, sem atribuir a Lloyd 1982 ou a
  MacQueen 1967 o que eles não descrevem. Faltam: o resumo de Forgy (1965) e qualquer análise do custo por
  iteração (o custo O(nkd) da semeadura aparece em Bahmani et al., p. 623, slot KM2).
- **KM2. Inicialização.** Dois artigos lidos (Arthur & Vassilvitskii 2007; Bahmani et al. 2012). A
  semeadura é onde se ganha qualidade e se reduz o número de iterações: Arthur & Vassilvitskii provam
  uma garantia O(log k) em esperança para a ponderação D², mostram que ela é justa e medem, em
  dados de até 494 mil pontos, potenciais menores e execuções mais rápidas na maioria dos casos
  (com uma exceção no Norm25, k = 50); Bahmani et al. medem que menos iterações de Lloyd são
  necessárias com o k-means‖ (Spam, Tabela 6). O segundo artigo localiza também o **gargalo
  paralelo**: o passo de Lloyd "é fácil" de paralelizar (cada ponto escolhe o centro mais próximo
  de forma independente e o novo centro é uma média), mas a semeadura D² exige k passes; o k-means‖
  troca isso por O(log ψ) rodadas de amostragem independente. Ressalvas: os experimentos de Arthur &
  Vassilvitskii se descrevem como preliminares e o PDF é o manuscrito do autor; Bahmani et al.
  afirmam que o tempo "melhora linearmente com o número de máquinas" sem medir a variação do número
  de nós, e comparam num cluster de 1.968 nós com k de 500 a 1.000 — um cenário muito distante de uma
  máquina e 150 mil pontos. Nenhum dos dois mede speedup por threads em memória compartilhada;
  essa evidência, se existir, está no slot KM3.
- **KM3. Paralelização distribuída / MapReduce.** Três artigos lidos (Dhillon & Modha 2000; Zhao et al.
  2009; Ene et al. 2011). Os dois primeiros descrevem a **mesma decomposição** do k-means em lote, com
  dois vocabulários: blocos fixos de pontos por trabalhador, centros conhecidos por todos, atribuição
  local ao centro mais próximo com somas e contagens parciais, combinação global (`MPI_Allreduce` em
  Dhillon & Modha; *map*/*combine*/*reduce* em Zhao et al.) e novo centro = soma/contagem. Ene et al.
  descrevem o mesmo esquema como *Parallel-Lloyd* e afirmam que a solução é **idêntica** à do Lloyd
  sequencial (p. 16); Dhillon & Modha fixam os centróides iniciais para isolar o efeito do
  paralelismo. O ponto sobre **escalabilidade** é o que mais interessa à Análise de Escalabilidade:
  **só Dhillon & Modha** apresentam medidas numéricas por número de processadores (speedup de 15,62 em
  16 processadores com n = 2²¹ e de 6,22 com n = 2¹³; scaleup quase constante) e uma condição
  analítica — a comunicação fica desprezível quando n é grande frente a P·T_reduce/(3·T_flop) —, além
  das definições de *speedup* e *scaleup* e da regra de medir tempo **por iteração**. Zhao et al.
  mostram curvas de 1 a 4 computadores, sem valores no texto e sem descrever o conjunto de dados; Ene
  et al. medem em **simulação numa só máquina e sem comunicação**. Há também uma família **aproximada**:
  Ene et al. paralelizam por amostragem (k-median/k-center), com custo 5% a 16,5% maior que o do Lloyd
  paralelo nas tabelas, e o ganho de tempo (18× a 73×) varia com o tamanho do conjunto — aqui vale a
  tabela e não o "cerca de 20×" do texto. **Inconsistência interna resolvida:** o texto de Dhillon &
  Modha cita 2¹¹ como o menor conjunto, mas os dados da Fig. 3 correspondem a 2¹³. **Lacuna:** Dhillon
  & Modha dizem que a adaptação a memória compartilhada com *threads* é direta (p. 14–15), mas não a
  medem, e nenhum outro artigo lido mede speedup por *threads* de k-means — que é justamente o nosso
  cenário. Correções ao referencial: o Ene et al. é sobre k-median/k-center, sem *coresets*, e não cita
  Dhillon & Modha nem Zhao et al.
- **KM4. Escala prática (mini-lote, MLlib).** Dois artigos lidos (Sculley 2010; Meng et al. 2016).
  Sculley propõe o k-means em mini-lotes — alternativa estocástica que troca qualidade por custo por
  iteração: em RCV1 (781.265 exemplos) chega a um valor quase ótimo várias ordens de grandeza mais
  rápido que o lote, mas é sequencial, para dados esparsos de texto e sem quantificar a perda além de
  curvas; não reproduz o resultado do lote. Meng et al. descrevem a biblioteca MLlib: o k-means
  aparece só numa lista de métodos, e o ganho de 2,5× do KMeans na Fig. 2(b) é entre versões da
  biblioteca, não entre números de nós. **O artigo não descreve o algoritmo de k-means do MLlib** (nem
  *broadcast* de centróides, nem inicialização, nem k-means‖), o que contradiz o que o referencial
  original afirmava. O slot serve só de contexto: mostra que existem alternativas aproximadas e
  implementações de plataforma, sem sustentar decisões sobre o algoritmo implementado.

---

## 7. Posicionamento da implementação

*(No PASSO/CURA esta seção é "lacuna e posicionamento". Aqui o objetivo é outro: mapear cada
decisão de projeto à literatura que a sustenta ou limita, sem decidi-la.)*

### Decisões de projeto × literatura (k-means, após a leitura dos 9 artigos)

| Questão de projeto | O que a literatura lida diz | Fonte |
|---|---|---|
| Que variante de k-means se implementa e como nomeá-la | Sequencial (MacQueen) × lote de dois passos (MacQueen §3.6, Forgy–Jennrich; "iteração de Lloyd" em Arthur e Bahmani) | MacQueen p. 283, 294; Arthur §2.1 |
| Critério de parada / terminação | O laço em lote termina em número finito de passos; tempo pode ser exponencial no pior caso | MacQueen p. 294; Arthur p. 1; Bahmani p. 622 |
| Qualidade e mínimos locais | Sem garantia de ótimo global; k-means++ garante O(log k) em esperança na semeadura | MacQueen p. 282, 288; Lloyd p. 131; Arthur Teor. 3.1 |
| Estratégia de inicialização | Aleatória uniforme × D²; ganho em dados gaussianos bem separados; custo de k passes | Arthur §2.2, §6; Bahmani p. 623 |
| Onde o paralelismo do k-means é trivial | Atribuição por ponto independente; centro novo = média; as iterações em si são seriais | Bahmani p. 622; Zhao p. 675 |
| Como particionar e combinar | Blocos de n/P pontos por trabalhador; somas e contagens parciais por centro, combinadas a cada iteração (`MPI_Allreduce` ou *map*/*combine*/*reduce*); o resultado coincide com o sequencial | Dhillon & Modha p. 5–7; Zhao p. 676–677; Ene p. 16 |
| Quando o paralelismo compensa | O ganho melhora com n; condição P·T_reduce/(3·T_flop) ≪ n; speedup de 6,22 (n = 2¹³) a 15,62 (n = 2²¹) em 16 processadores | Dhillon & Modha p. 8–9 |
| Como medir escalabilidade | *speedup* = T(1)/T(P); *scaleup* com tempo **por iteração**; centróides iniciais fixos; tempos sem leitura de disco; resultados numéricos só em Dhillon & Modha, curvas em Zhao, simulação em Ene | Dhillon & Modha p. 8–9, 11; Zhao p. 677–678; Ene p. 17 |
| Memória compartilhada com *threads* | Descrita como adaptação direta (blocos de pontos, cópia dos centróides por *thread*), **sem medida** em nenhum artigo lido | Dhillon & Modha p. 14–15 *(lacuna)* |
| Alternativas aproximadas | Amostragem com garantia (k-median/k-center); mini-lotes estocásticos; ambos trocam qualidade por tempo | Ene p. 3, 14, 17; Sculley p. 1177–1178 |

### Artigos-âncora por seção do relatório (parcial)

- **§1 Introdução:** MacQueen 1967, Lloyd 1982 (origem); Bahmani et al. 2012 e Dhillon & Modha 2000
  (por que escalar o k-means); Sculley 2010 (alternativas aproximadas), como contexto.
- **§2 Algoritmos Implementados — k-means:** MacQueen §3.6 e Arthur & Vassilvitskii §2.1 (variante
  em lote); Arthur & Vassilvitskii §2.2 e §6 (inicialização); Dhillon & Modha §3 (decomposição por
  dados) e Zhao et al. §2.2 (mesma ideia em *map*/*combine*/*reduce*).
- **§3 Setup Experimental:** Arthur & Vassilvitskii §6.1, Bahmani §4 e Dhillon & Modha §4.1 (como a
  literatura descreve datasets sintéticos, hardware e controles: centróides iniciais fixos, sem I/O).
- **§4 Análise de Escalabilidade:** Dhillon & Modha §4 (definições e resultados de *speedup* e
  *scaleup*); Bahmani §5 (tempo e iterações até convergir); Zhao §3 e Ene §4 (ressalvas sobre como
  outros reportam: curvas sem valores; simulação sem comunicação).
- **§5 Conclusões / Discussão:** lacuna de medidas de *speedup* por *threads* em memória compartilhada
  (Dhillon & Modha p. 14–15 apenas propõem); Ene et al. e Sculley como contraste exato × aproximado.

---

## 8. Changelog

- **22/10/2026** — documento criado. Estrutura replicada do PASSO/CURA (template, catálogo, revisão,
  fichamentos, `.bib`); um único `fichamentos.md`, escrito em duas etapas (k-means, depois DBSCAN). Decisões do grupo registradas na §1.
- **22/10/2026** — **verificação dos 22 PDFs** (cabeçalho, contagem de páginas, Crossref): todos são
  os artigos esperados. Divergências corrigidas ou registradas:
  - Han et al. é IPDPSW **2016**, p. **1393–1402** (a lista do grupo dizia 2018; o
    `referencial_teorico.md` dizia p. 271–280). *(Corrigir na 2ª etapa, DBSCAN.)*
  - Quatro PDFs estão no repositório mas não na lista inicial: Sculley 2010, Ene et al. 2011,
    Schubert et al. 2017, He et al. 2014. Mantidos por decisão do grupo.
  - Arthur & Vassilvitskii: o PDF é o manuscrito do autor (sem veículo); Bahmani et al. e Ene et al.:
    PDFs são arXiv (Bahmani com rodapé da VLDB; Ene é a versão estendida de 20 p.).
  - Dhillon & Modha: capítulo do LNCS 1759 (2000), originado no workshop do KDD'99; o Crossref data
    de 2002.
  - Afirmações do `referencial_teorico.md` que os PDFs **não sustentam** (a conferir na leitura de
    cada um): "coresets" em Ene et al. (a palavra não aparece; o artigo trata k-center/k-median por
    amostragem); "k-d trees" como estrutura principal em Wang et al. (aparece 4 vezes; as estruturas
    centrais são grade, quadtree, caixas e Delaunay); descrição detalhada de k-means distribuído em
    Meng et al. (k-means só aparece numa lista de métodos); "dR\*-tree" em Xu et al. (o PDF diz
    "dR-tree"); partição em "grade" em Cordova & Moh (a palavra "grid" não aparece no PDF).
- **22/10/2026** — **P0 + P1 concluídas.** Lidos e fichados KM1 (MacQueen, Lloyd) e KM2 (Arthur &
  Vassilvitskii, Bahmani et al.). 24 citações literais conferidas por busca no texto extraído.
  Correções ao `referencial_teorico.md`, vindas da leitura:
  - MacQueen: o procedimento do artigo é **sequencial**; o laço em lote é a §3.6, atribuído a Forgy e
    Jennrich. O referencial atribuía o "algoritmo de dois passos" só a Lloyd.
  - Lloyd: o artigo trata **quantização escalar**; não descreve agrupamento de pontos em R^d. Páginas
    corretas: 129–137 (Arthur & Vassilvitskii as citam como 129–136). A p. 137 do PDF é de outro
    artigo (Newman).
  - Bahmani et al.: o artigo **não menciona** o MLlib/Spark (cita Hadoop e Mahout); o referencial
    afirmava que o k-means‖ é a inicialização do MLlib. A conferir em Meng et al.
  - Bahmani et al.: a afirmação de que o k-means‖ escala linearmente com o número de máquinas não é
    sustentada por experimento do próprio artigo.
- **22/10/2026** — criado o §9 (registro de inconsistências encontradas nos artigos e valor adotado),
  reunindo o que estava nas fichas e no changelog; regra incluída no template (§3).
- **22/10/2026** — **P2 concluída (1ª etapa, k-means, lida e fichada por inteiro).** Lidos e fichados
  KM3 (Dhillon & Modha, Zhao et al., Ene et al.) e KM4 (Sculley, Meng et al.). 55 citações literais
  conferidas por busca no texto extraído (24 de KM1/KM2 e 31 de KM3/KM4). Critério adotado onde o
  texto diverge de figura ou tabela: **vale o dado observado**, registrando a divergência. Resolvido:
  - Dhillon & Modha: o texto fala em n = 2¹¹ como o menor conjunto, mas a legenda e os traços vetoriais
    da Fig. 3 correspondem a 2¹³ (os tempos com P = 1 seguem n·I e reproduzem os speedups de 15,6 e
    6,2). Adotado 2¹³.
  - Ene et al.: "cerca de 20×" sobre o Lloyd paralelo e perda "desprezível" no texto; as tabelas dão
    18× a 73× e custo 5% a 16,5% maior. Adotadas as tabelas. Registrado também que o tempo do
    *Parallel-Lloyd* com 10 milhões de pontos (702,3 s) é menor que com 5 milhões (1.333,3 s), sem
    explicação.
  Correções ao `referencial_teorico.md`, vindas da leitura:
  - Ene et al.: trata **k-median e k-center** (k-means só numa frase de conclusão); **não há
    *coresets*** (a técnica é amostragem); o artigo não cita Dhillon & Modha nem Zhao et al.; as
    experiências são simulações numa máquina, sem comunicação.
  - Meng et al.: o k-means aparece uma única vez, numa lista; não há descrição de *broadcast* de
    centróides, de inicialização ou de k-means‖; a Fig. 2(b) dá 2,5× para o KMeans entre as versões 1.0
    e 1.1 da biblioteca.
  - Zhao et al.: o referencial omitia o *combiner*; o conjunto de dados do experimento não é descrito.
  - Sculley: o pôster tem uma segunda contribuição (centros esparsos por projeção L1) que o referencial
    não mencionava.
  - Dhillon & Modha: o mecanismo é `MPI_Allreduce`, não *map*/*reduce*; a adaptação a memória
    compartilhada é descrita (p. 14–15), mas não medida.

---

## 9. Registro de inconsistências encontradas nos artigos e valor adotado

**Critério.** Quando duas partes do mesmo artigo divergem (texto × figura ou tabela), ou quando um
artigo cita outro com dado diferente do que está no PDF deste, **vale o dado observado**: a figura, a
tabela, os traços vetoriais da figura ou o cabeçalho do próprio PDF, nessa ordem de preferência sobre
a frase de resumo. A divergência não é apagada: fica aqui e na ficha do artigo, e a citação no
relatório deve usar o valor adotado. Anotações do `referencial_teorico.md` que os PDFs não sustentam
ficam no changelog (§8), não neste registro.

| # | Artigo | Inconsistência encontrada | Valor adotado | Evidência | Documentado em |
|---|---|---|---|---|---|
| 1 | Dhillon & Modha (2000) | O texto (p. 9) chama de menor conjunto o de n = 2¹¹ e fala em crescer de 2¹¹ a 2²¹; a legenda da Fig. 3 e as curvas listam cinco conjuntos, de 2¹³ a 2²¹. | **n mínimo = 2¹³** (speedup de 6,22 em 16 processadores) | Na Fig. 3, os tempos com P = 1 dos cinco conjuntos seguem n·I (I = 3, 10, 8, 164, 50) e as razões tempo(1)/tempo(16) lidas na figura reproduzem 15,6 e 6,2; com 2¹¹ não reproduziriam. | `fichamentos.md` (ficha Dhillon & Modha); §6 KM3; §8 |
| 2 | Dhillon & Modha (2000) | O resumo diz que, num conjunto de 2 GB, o algoritmo chega a "mais de 1,8 gigaflops"; o corpo diz ≈ 1,2 Gflops ao escalar n (p. 13) e "quase 1,8" ao escalar d (p. 14). | **≈ 1,2 Gflops (escalando n) e ≈ 1,8 Gflops (escalando d)**; o resumo se refere ao segundo caso | Texto das p. 13–14. | `fichamentos.md` (ficha Dhillon & Modha, scaleup) |
| 3 | Ene et al. (2011) | O texto resume o ganho do *Sampling-Lloyd* sobre o *Parallel-Lloyd* como "cerca de 20×" (p. 3 e 17). | **18× a 73×**, conforme n (≈ 51× com 1 milhão, ≈ 57× com 2 milhões, ≈ 73× com 5 milhões, ≈ 18× com 10 milhões; razões calculadas a partir das tabelas) | Tabelas das Fig. 1–2 (p. 14). "20×" só corresponde ao menor ganho, o de 10 milhões. | `fichamentos.md` (ficha Ene et al.); §6 KM3; §8 |
| 4 | Ene et al. (2011) | O texto fala em perda "pequena" ou "desprezível" de qualidade. | **Custo de 5% a 16,5% maior** que o do Lloyd paralelo (13,2% com 1 milhão de pontos; 7,3% com 10 milhões) | Linhas de custo normalizado das Fig. 1–2. | idem |
| 5 | Ene et al. (2011) | O tempo do *Parallel-Lloyd* com 10 milhões de pontos (702,3 s) é **menor** que o com 5 milhões (1.333,3 s); o texto não comenta. | **Registrado como observado, sem explicação.** Não se infere tendência de tempo a partir desse ponto; o número de iterações do Lloyd não é informado. | Tabela da Fig. 2. | idem |
| 6 | Zhao et al. (2009) | A frase "o conjunto é dividido e globalmente difundido a todos os *mappers*" (p. 676) contradiz os pseudocódigos, em que o que todos os *mappers* conhecem é o vetor `centers`. | **O que é difundido é o vetor `centers`; o conjunto é dividido entre os *mappers*** | Algoritmo 1 e texto que o acompanha (p. 676). | `fichamentos.md` (ficha Zhao et al., limitações) |
| 7 | Bahmani et al. (2012) | O texto afirma que o tempo do k-means‖ "melhora linearmente com o número de máquinas" (p. 627), mas nenhum experimento varia o número de nós. | **Tratada como afirmação não medida**; não é citada como resultado experimental. | Seções 4–5: comparam algoritmos num cluster fixo de 1.968 nós. | `fichamentos.md` (ficha Bahmani et al., limitações); §5 e §6 KM2 |
| 8 | Arthur & Vassilvitskii (2007), citando Lloyd | A lista de referências cita Lloyd (1982) com páginas 129–136. | **129–137** | Cabeçalho do PDF do Lloyd (p. 129 a 137) e Crossref. | `fichamentos.md` (ficha Lloyd, limitações); §8 |
| 9 | Han et al. (lista do grupo × PDF) | A lista inicial do grupo traz o ano 2018 e o `referencial_teorico.md` traz as páginas 271–280. | **IPDPSW 2016, p. 1393–1402** | Cabeçalho do PDF (2016 IEEE IPDPSW), DOI 10.1109/IPDPSW.2016.57 e Crossref. | §8; será repetido na ficha na 2ª etapa (DBSCAN) |

Itens da 2ª etapa (DBSCAN) entram nesta tabela à medida que forem encontrados.
