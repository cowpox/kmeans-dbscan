# Fichamentos — revisão de literatura k-means e DBSCAN

> Uma ficha completa por artigo **lido na íntegra** (não por artigo apenas coletado; esses ficam na
> fila em `revisao_literatura.md` §4). Molde e regras em `fichamento_literatura_template.md`. Ao terminar uma
> ficha: (1) mover a linha da fila para "Artigos lidos" (`revisao_literatura.md` §5);
> (2) adicionar a entrada em `referencias.bib`.
>
> Organizado por slot, na ordem de `revisao_literatura.md` §2. A revisão é escrita em duas etapas, nos
> mesmos arquivos: 1ª etapa, k-means (slots KM1–KM4); 2ª etapa, DBSCAN (slots DB1–DB4), acrescentada
> a este arquivo depois do k-means.
>
> **Divergências internas dos artigos** (texto × figura/tabela) e o valor adotado: ver
> `revisao_literatura.md` §9.
>
> **Convenção de página:** quando o PDF traz a paginação do veículo, cito a página **impressa**; quando
> não traz (Arthur & Vassilvitskii), cito "p. N do PDF". Toda citação literal foi conferida por busca
> no texto extraído do PDF.

---

## Slot KM1 — Formulação original do k-means

*(2 fichas, 22/10/2026: MacQueen 1967 e Lloyd 1982. Lidos na íntegra.)*

### [KM1] MacQueen, 1967 — Alguns métodos para classificação e análise de observações multivariadas

**Arquivo:** `MACQUEEN_1967_some_methods_classification_analysis_multivariate_observations.pdf`

**Referência completa:**
MacQueen, J. Some methods for classification and analysis of multivariate observations. In:
**Proceedings of the Fifth Berkeley Symposium on Mathematical Statistics and Probability**, v. 1,
p. 281–297, Berkeley: University of California Press, 1967.

**Chave BibTeX:** `macqueen1967some`

**Tipo:** Artigo de conferência (clássico de referência); sem DOI.

**Versão lida:** reprodução digital do artigo publicado; 17 páginas, paginação impressa 281–297
conferida no cabeçalho de cada página (página do PDF + 280).

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Autor, título, veículo e páginas
conferidos no próprio PDF e nas listas de referências de Patwary et al. (2012) e Zhao et al. (2009),
que citam "vol. 1, pp. 281–297".

**Estado da leitura:** texto integral lido em 22/10/2026 (resumo, desenvolvimento, experimentos e conclusão; demonstrações apenas percorridas); citações literais conferidas por busca no texto extraído do PDF. **Conferência por integrante do grupo pendente** — conferir no PDF antes de citar no relatório.

**O que o artigo diz** (lido na íntegra, p. 281–297). Achados principais:
- **Objetivo.** Descrever "um processo para particionar uma população N-dimensional em k conjuntos a
  partir de uma amostra", chamado *k-means* (p. 281). O critério é a variância intra-classe
  W²(S) (p. 281); o texto diz que o processo "tende a" baixá-la, por razões intuitivas, análise
  matemática parcial e experiência computacional.
- **O procedimento definido no artigo é sequencial (*online*), não em lote** (§2.1, p. 283): as
  primeiras k observações viram as k médias iniciais, e cada observação seguinte é somada ao grupo
  de média mais próxima, que é atualizada com peso (contagem de pontos do grupo).
- **Resultados assintóticos** (§2, p. 283–288), sob amostragem i.i.d. de uma medida absolutamente
  contínua em conjunto convexo limitado: o Teorema 1 mostra que W(xⁿ) converge quase certamente e
  que o limite coincide com V(x) para algum k-ponto "não enviesado" (cada ponto igual à média
  condicional da sua região); o Teorema 2 dá uma lei fraca para a distância entre médias correntes e
  médias condicionais. As provas usam martingales (Lema 3, p. 285).
- **Não garante o ótimo.** "Em geral o procedimento não converge para uma partição ótima" (p. 282);
  o exemplo do retângulo levemente alongado (§2.3, p. 288) converge, com probabilidade positiva,
  para uma partição que "claramente não tem variância intra-classe mínima".
- **Aplicações (§3, p. 288–294):** agrupamento por similaridade com parâmetros de *coarsening* C e
  *refinement* R que fundem médias próximas e criam novas (p. 289); classificação relevante;
  aproximação de distribuição por mistura de normais; teste de independência por embaralhamento de
  dimensões; árvores de classificação por distância; e, na §3.6 (p. 294), o **procedimento de dois
  passos** (calcular as médias de uma partição; reclassificar cada ponto para a média mais
  próxima), que o autor atribui a Forgy e Jennrich (comunicado em 1963; p. 282) e do qual afirma que
  gera partições com variância intra-classe decrescente e "converge em um número finito de passos".
- **Experimentos, todos de pequena escala** (p. 289–290): amostras reais de 70 faculdades (5 dim.),
  360 palavras (20 dim.), 760 documentos (15 dim.) e 560 sujeitos (15 dim.), avaliadas por inspeção.
  Estabilidade à ordem dos dados: 250 pontos em 5 dimensões, k = 18, três ordens aleatórias; a
  variação intra-classe mudou no máximo 7% (p. 290). Tempo no IBM 7094: cerca de 1 minuto para 200
  pontos de 20 dimensões com C e R ativos; o k-means "sem enfeites" com k = 20 processa 500 pontos de
  20 dimensões em cerca de 10 segundos (p. 290).
- **Extensão a espaços métricos gerais** (§4, p. 294–296) por meio de "centroide de ordem r"
  (r = 2 dá a média, r = 1 a mediana).

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means) — origem do termo e do objetivo de variância intra-classe;
  distinguir as variantes (sequencial de MacQueen × dois passos em lote) ao dizer qual foi
  implementada.
- Relatório §1 (Introdução) — citação histórica ao lado de Lloyd.

**Aplicabilidade ao nosso caso:** alta como referência de origem e de terminologia; **nenhuma** em
paralelismo ou escala (a maior amostra tem 760 pontos). O procedimento sequencial depende da ordem
de chegada dos pontos (p. 290); a variante em lote (§3.6) é a que os artigos posteriores
(Arthur & Vassilvitskii; Bahmani et al.) chamam de iteração de Lloyd (inferência nossa, a partir da
comparação das descrições).

**Dado/afirmação específica a usar:**
> "The main purpose of this paper is to describe a process for partitioning an N-dimensional
> population into k sets on the basis of a sample." (p. 281) — fixa o que "k-means" significa na
> fonte original.

> "Stated informally, the k-means procedure consists of simply starting with k groups each of which
> consists of a single random point, and thereafter adding each new point to the group whose mean
> the new point is nearest." (p. 283) — a versão sequencial, que não é o laço atribuição/atualização
> em lote.

> "In general, the k-means procedure will not converge to an optimal partition, although there are
> special cases where it will." (p. 282) — o limite de qualidade que Arthur & Vassilvitskii (2007)
> quantificam décadas depois.

> "Starting with an arbitrary partition into k sets, the means of the points in each set are first
> computed. Then a new partition of the points is formed by the rule of putting the points into
> groups of the basis of nearness to the first set of means." (p. 294) — o procedimento de dois
> passos em lote (a grafia "of the basis" é do original).

> "This can be iterated and the series of the partitions thus produced have decreasing within-class
> variances and will converge in a finite number of steps." (p. 294).

> "When the sample points are rearranged in a new random order, there is some variation in the
> grouping which is obtained." (p. 290) — sensibilidade à ordem do procedimento sequencial.

**Limitações ou ressalvas:**
- A convergência demonstrada vale para o procedimento sequencial sob amostragem i.i.d., não para o
  laço em lote iterado até estabilizar sobre um conjunto finito (a garantia desse último é só a de
  terminação em número finito de passos, p. 294).
- Avaliação empírica qualitativa, com amostras de centenas de pontos e sem medida de escalabilidade.
- A atribuição a Forgy e Jennrich cita um resumo da *Biometrics* de 1965 (referência [7], p. 296–297)
  que **não está** no repositório; só conseguimos verificar a atribuição pela palavra de MacQueen.
- O artigo é anterior a qualquer discussão de paralelismo; nada nele sustenta uma estratégia de
  paralelização.

---

### [KM1] Lloyd, 1982 — Quantização por mínimos quadrados em PCM

**Arquivo:** `LLOYD_1982_least_squares_quantization_in_pcm.pdf`

**Referência completa:**
Lloyd, S. P. Least squares quantization in PCM. **IEEE Transactions on Information Theory**, v. IT-28,
n. 2, p. 129–137, mar. 1982. DOI: 10.1109/TIT.1982.1056489. (Rascunho de 1957, circulado nos Bell
Laboratories; publicado em 1982.)

**Chave BibTeX:** `lloyd1982least`

**Tipo:** Artigo original (clássico de referência), revisado por pares; texto de 1957 publicado em
1982.

**Versão lida:** PDF digitalizado da *IEEE Trans. Inf. Theory* (9 páginas, paginação impressa
129–137 conferida no cabeçalho). **A última página (137) traz o início de outro artigo, "The Hexagon
Theorem" de D. J. Newman**, que não é de Lloyd e não foi fichado aqui.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Autor, título, volume, páginas e DOI
conferidos no cabeçalho do PDF e no Crossref (22/10/2026).

**Estado da leitura:** texto integral lido em 22/10/2026 (resumo, desenvolvimento, experimentos e conclusão; demonstrações apenas percorridas); citações literais conferidas por busca no texto extraído do PDF. **Conferência por integrante do grupo pendente** — conferir no PDF antes de citar no relatório.

**O que o artigo diz** (lido na íntegra, p. 129–137). Achados principais:
- **Problema.** Em modulação por código de pulso (PCM), dado um número fixo e finito v de "quanta"
  (valores representativos) e as propriedades estatísticas do sinal, achar os subconjuntos do eixo de
  tensão e os quanta que minimizam a potência média do ruído de quantização (p. 129–130). O problema
  é **unidimensional** (uma tensão real x com distribuição F), não um conjunto de pontos em R^d.
- **Duas condições necessárias** (§IV–V, p. 130–131): (i) dado o particionamento, o melhor quantum de
  cada conjunto é o seu **centro de massa** (eq. 14); (ii) dados os quanta, o melhor particionamento
  tem fronteiras no **ponto médio** entre quanta sucessivos (eq. 15–17).
- **Não são suficientes** (§VI, p. 131): os pontos que satisfazem as duas condições são
  "estacionários" e podem ser mínimos locais (vários) ou pontos de sela; o autor "não conseguiu
  determinar condições suficientes para um mínimo absoluto". O Apêndice B (p. 136) dá exemplos com
  v − 1 mínimos locais distintos.
- **Método I** (§VI, p. 131–132): impor as condições alternadamente, partindo de pontos de
  fronteira arbitrários; a cada passo o ruído **não aumenta** (N(p⁽ᵐ⁾) é não crescente) e, como o
  ruído é não negativo, o limite existe; se N tem um único ponto estacionário, a sequência converge
  para ele (a menos que divirja). **Método II** (p. 132–133) varre valores iniciais de q₁ e encadeia
  as condições, e serve para localizar todos os pontos estacionários.
- **Exemplos numéricos** (§VII, p. 133): para a distribuição uniforme o ótimo são v intervalos iguais
  (N = 1/v²); tabelas para a gaussiana e a laplaciana com v = 2ᵇ, b = 1…7. Algumas tabelas "nunca
  foram completadas" e não estão no artigo (nota 3, p. 133).
- **Comportamento assintótico** (§VIII, p. 133–134): a densidade fracional de quanta é proporcional
  a f^(1/3) e N ≈ (1/12v²)(∫f^(1/3))³, o que recupera o resultado de Panter e Dite.
- **Nota do autor de 1981** (p. 136–137): o texto é "quase uma reprodução literal" de um rascunho de
  31/07/1957; não foi publicado antes porque os cálculos numéricos nunca foram completados; uma
  condição suficiente para o mínimo único veio depois, de Fleischer (1964).

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means) — origem histórica do par de condições "centro de massa" e
  "fronteira no ponto médio" que sustenta o passo de atualização e o de atribuição.
- Relatório §1 (Introdução) — citação junto a MacQueen (1967).

**Aplicabilidade ao nosso caso:** média, como referência histórica. O artigo **não trata** de
agrupamento de pontos em R^d, de k-means, de critério de parada nem de complexidade; é quantização
escalar de um sinal. A associação "algoritmo de Lloyd = laço atribuição/atualização do k-means" é
feita por trabalhos posteriores (Arthur & Vassilvitskii 2007, §1: "Lloyd [20] proposed a local search
solution"), e MacQueen (1967, p. 282) atribui o procedimento de dois passos sobre pontos a Forgy e
Jennrich.

**Dado/afirmação específica a usar:**
> "The number v of quanta is to be regarded throughout as a fixed finite preassigned number."
> (p. 129) — o análogo do k fixo do k-means.

> "We continue in this way, imposing conditions (14) and (17) alternately." (p. 132) — o esquema
> alternado do Método I.

> "This step does not increase the noise, in view of the discussion in Section V" (p. 132) —
> monotonicidade do ruído a cada passo.

> "As it turns out, however, they are not sufficient conditions for a minimum of N(p)." (p. 131) —
> convergência a ponto estacionário não implica ótimo global.

> "The author has not been able to determine sufficient conditions for an absolute minimum."
> (p. 131).

> "This is nearly a verbatim reproduction of a draft manuscript, which was circulated for comments
> at Bell Laboratories" (p. 136) — a data real da ideia (1957), anterior a MacQueen (1967).

**Limitações ou ressalvas:**
- Unidimensional, distribuição conhecida F, v pequeno; sem experimento em dados multivariados e sem
  análise de custo computacional.
- Citar "Lloyd (1982)" para o algoritmo em R^d é uma **atribuição por tradição**, não um conteúdo do
  artigo; no relatório convém dizer "iteração de Lloyd" e ancorar a descrição em Arthur &
  Vassilvitskii (2007, §2.1) ou MacQueen (1967, §3.6).
- Arthur & Vassilvitskii (2007) citam o artigo com páginas "129–136" (lista de referências, p. 9 do
  PDF deles); o correto é 129–137 (cabeçalho do PDF e Crossref).

---

## Slot KM2 — Inicialização do k-means

*(2 fichas, 22/10/2026: Arthur & Vassilvitskii 2007 e Bahmani et al. 2012. Lidos na íntegra.)*

### [KM2] Arthur & Vassilvitskii, 2007 — k-means++: as vantagens de uma semeadura cuidadosa

**Arquivo:** `ARTHUR_2007_k_means_plusplus_advantages_of_careful_seeding.pdf`

**Referência completa:**
Arthur, D.; Vassilvitskii, S. k-means++: the advantages of careful seeding. In: **Proceedings of the
Eighteenth Annual ACM-SIAM Symposium on Discrete Algorithms (SODA '07)**, p. 1027–1035,
Philadelphia: SIAM, 2007. DOI: 10.5555/1283383.1283494 (a conferir: DOI vindo de listagem
secundária; páginas conferidas na lista de referências de Ene et al. 2011 e em listagens
bibliográficas).

**Chave BibTeX:** `arthur2007kmeanspp`

**Tipo:** Artigo de conferência, revisado por pares.

**Versão lida:** **manuscrito do autor** (metadado de criação do PDF de 02/10/2006; o texto não traz
veículo nem paginação do SODA). 9 páginas; as páginas citadas abaixo são **páginas do PDF**.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Título e autores conferidos no PDF; o
veículo e as páginas vêm de fontes externas e das referências cruzadas de outros artigos do repo.

**Estado da leitura:** texto integral lido em 22/10/2026 (resumo, desenvolvimento, experimentos e conclusão; demonstrações apenas percorridas); citações literais conferidas por busca no texto extraído do PDF. **Conferência por integrante do grupo pendente** — conferir no PDF antes de citar no relatório.

**O que o artigo diz** (lido na íntegra, p. 1–9 do PDF). Achados principais:
- **Problema.** O k-means (iteração de Lloyd) é simples e rápido, mas "o custo da velocidade
  empírica é a precisão": há exemplos em que gera agrupamentos arbitrariamente ruins (φ/φ_OPT
  ilimitado), inclusive com semeadura aleatória padrão (p. 1). Resolver o problema de forma exata é
  NP-difícil, mesmo com dois grupos (p. 1).
- **Algoritmo k-means++** (§2.2, p. 2–3): o primeiro centro é escolhido uniformemente; cada centro
  seguinte x′ é escolhido com probabilidade D(x′)²/Σ D(x)² ("ponderação D²"), em que D(x) é a
  distância ao centro já escolhido mais próximo; depois segue-se o k-means padrão (passos 2–4).
- **Garantia** (Teorema 1.1/3.1, p. 1 e 3–5): após a semeadura, E[φ] ≤ 8(ln k + 2) φ_OPT, ou seja,
  O(log k)-competitivo **em esperança e para qualquer conjunto de dados**. A garantia é provada para o
  passo de semeadura; as iterações seguintes "só podem diminuir φ" (p. 3).
- **A análise é justa a menos de constante** (§4, p. 5–6): existe uma instância (k simplexes
  regulares em dimensões ortogonais) em que a semeadura D² não é melhor que 2(ln k)-competitiva
  (Teorema 4.1).
- **Generalização** (§5, p. 6): a semeadura Dℓ vale em espaços métricos arbitrários e para o
  potencial φ[ℓ] (ℓ = 1 é o k-median), com E[φ[ℓ]] ≤ 2²ℓ (ln k + 2) φ[ℓ]_OPT (Teorema 5.1).
- **Trabalho relacionado** (§1.1, p. 1–2): Ostrovsky et al. propuseram "essencialmente o mesmo
  algoritmo" de forma independente, com garantia O(1) sob uma condição de separação
  (φ_OPT,k / φ_OPT,k−1 ≤ ε²); combinados, k-means++ nunca é pior que O(log k) e melhora para O(1) em
  dados bem formados.
- **Experimentos "preliminares"** (§6, p. 6–8), implementação em C++ "padrão, sem otimizações
  especiais", 20 execuções por caso, k = 10, 25, 50: Norm25 (sintético: 25 centros num hipercubo
  15-D de lado 500, gaussianas de variância 1; n = 10.000), Cloud (n = 1.024, d = 10), Intrusion
  (n = 494.019, d = 35) e Spam (n = 4.601, d = 58). O k-means++ foi melhor em potencial e em tempo
  médio na maioria dos casos: no Intrusion, o k-means leva em média 63,94 s (k = 10), 257,34 s
  (k = 25) e 917,00 s (k = 50), e o k-means++ reduz o tempo em 44,49%, 49,19% e 66,70% (Tabela 3,
  p. 7); nos conjuntos Spam e Intrusion os potenciais ficaram 20 a 1.000 vezes menores (p. 8). **Há
  exceção:** no Norm25 com k = 50, o tempo médio do k-means++ foi 1,62% *pior* (Tabela 1, p. 7).
- **Em dados sintéticos bem separados** a semeadura uniforme "inevitavelmente funde" alguns
  agrupamentos verdadeiros, que a busca local nunca consegue separar; o k-means++ "quase sempre"
  atingiu o agrupamento ótimo (p. 8).
- **Questões em aberto** (§7, p. 8): se várias execuções melhoram o limite assintótico; uma variante
  que sorteia vários candidatos por iteração e escolhe o que mais reduz φ "geralmente teve melhor
  desempenho" nos experimentos, mas sem prova.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means) — declarar a estratégia de inicialização adotada e justificá-la
  com a literatura (aleatória × D²); o artigo é a referência de k-means++.
- Relatório §3 (Setup Experimental) — relação entre o tipo de dataset (gaussianas bem separadas ou
  densidades diferentes) e o risco de semeadura ruim.
- Relatório §4 (Análise de Escalabilidade) — o tempo total depende do número de iterações de Lloyd,
  que a semeadura altera (p. 8).

**Aplicabilidade ao nosso caso:** média a alta para a decisão de inicialização. O artigo é
inteiramente sequencial e não discute paralelismo. O `dataset1` do repositório (blobs gaussianos bem
separados, 150.000 pontos) se parece com o Norm25 (inferência nossa, a partir da descrição em
`data/README.md`), cenário em que o artigo relata o maior ganho do k-means++.

**Dado/afirmação específica a usar:**
> "Specifically, we choose a point p as a center with probability proportional to p's contribution to
> the overall potential." (p. 1) — a regra da ponderação D².

> "Unfortunately, it is guaranteed only to find a local optimum, which can often be quite poor."
> (p. 2) — limite do k-means padrão.

> "Solving this problem exactly is NP-hard, even with just two clusters [10]" (p. 1).

> "The D2 seeding is slightly slower than uniform seeding, but it still leads to a faster algorithm
> since it helps the local search converge after fewer iterations." (p. 8) — o custo da semeadura é
> compensado por menos iterações.

> "Indeed, on the Spam and Intrusion datasets, k-means++ achieved potentials 20 to 1000 times
> smaller than those achieved by standard k-means." (p. 8).

> "Also, experiments showed that k-means++ generally performed better if it selected several new
> centers during each iteration, and then greedily chose the one that decreased φ as much as
> possible." (p. 8) — variante gulosa, sem prova no artigo.

**Limitações ou ressalvas:**
- O próprio resumo chama os experimentos de "preliminares". Não informam hardware; só medem tempo
  total (não o número de iterações de Lloyd, que Bahmani et al. 2012 medem na Tabela 6); a
  comparação é só com a semeadura uniforme.
- A garantia é em esperança, não com alta probabilidade, e vale para o potencial *logo após* a
  semeadura (p. 3).
- Manuscrito do autor: pode diferir em detalhes da versão do SODA; usar números de seção e de teorema
  ao citar, não só a página do PDF.
- Sem tratamento de paralelismo; o custo de k passes sobre os dados para escolher k centros é
  apontado por Bahmani et al. (2012, p. 623), não por este artigo.

---

### [KM2] Bahmani, Moseley, Vattani, Kumar & Vassilvitskii, 2012 — k-means++ escalável (k-means‖)

**Arquivo:** `BAHMANI_2012_scalable_k_means_plusplus.pdf`

**Referência completa:**
Bahmani, B.; Moseley, B.; Vattani, A.; Kumar, R.; Vassilvitskii, S. Scalable k-means++. **Proceedings
of the VLDB Endowment**, v. 5, n. 7, p. 622–633, 2012. DOI: 10.14778/2180912.2180915.

**Chave BibTeX:** `bahmani2012scalable`

**Tipo:** Artigo de conferência (VLDB 2012), revisado por pares.

**Versão lida:** arXiv:1203.6402v1 [cs.DB], 29/03/2012, que traz o aviso de copyright da VLDB
Endowment (Vol. 5, No. 7). A paginação impressa 622–633 aparece no texto (página do PDF + 621) e é a
citada abaixo.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Título, autores, volume, número, páginas
e DOI conferidos no Crossref (22/10/2026).

**Estado da leitura:** texto integral lido em 22/10/2026 (resumo, desenvolvimento, experimentos e conclusão; demonstrações apenas percorridas); citações literais conferidas por busca no texto extraído do PDF. **Conferência por integrante do grupo pendente** — conferir no PDF antes de citar no relatório.

**O que o artigo diz** (lido na íntegra, p. 622–633). Achados principais:
- **Problema.** O k-means++ tem custo O(nkd), igual ao de uma iteração de Lloyd, mas é
  "inerentemente sequencial": a probabilidade do i-ésimo centro depende dos i−1 anteriores, e uma
  implementação ingênua faz **k passes** sobre os dados (p. 623). Já as iterações de Lloyd são fáceis
  de paralelizar: cada ponto escolhe independentemente o centro mais próximo e o novo centro é a
  média dos pontos atribuídos (p. 622).
- **Algoritmo k-means‖** (Algoritmo 2, §3.3, p. 625): inicia com um centro uniforme e calcula o custo
  inicial ψ; em O(log ψ) rodadas, amostra **cada ponto independentemente** com probabilidade
  ℓ·d²(x,C)/φ_X(C) (ℓ = Ω(k), esperados ℓ pontos por rodada) e os acrescenta a C; ao final pesa cada
  candidato pelo número de pontos que lhe são mais próximos e **reagrupa** os cerca de ℓ·log ψ
  candidatos ponderados em k centros com um algoritmo de aproximação (p. ex. k-means++).
- **Garantias.** Teorema 1 (p. 625): se o reagrupamento é uma α-aproximação, o k-means‖ é uma
  O(α)-aproximação; com k-means++ no passo 8, O(log k). Teorema 2 (p. 628): em cada rodada,
  E[φ_X(C∪C′)] ≤ 8φ* + ((1+α)/2)·φ_X(C), com α ≈ e^(−ℓ/2k); o Corolário 3 leva a um custo de ordem
  O(φ*) após O(log ψ) rodadas. A análise usa dualidade de programação linear (Lemas 4–6, p. 630–631).
- **Realização em MapReduce** (§3.5, p. 625–626): cada *mapper* amostra de forma independente; φ_X(C)
  é calculado somando, no *reducer*, os custos parciais φ_X′(C) das partições; pressupõe-se que o
  conjunto C cabe na memória ou é distribuído a todos os *mappers* (p. 626).
- **Experimentos** (§4–5, p. 626–628). *Datasets*: GaussMixture (n = 10.000, 15-D, centros de
  gaussiana esférica com variância R ∈ {1, 10, 100}), Spam (4.601 × 58) e KDDCup1999 (4,8 milhões × 42;
  amostra de 10% para parâmetros); k ∈ {20, 50, 100} nos dois primeiros e {500, 1000} no KDD.
  *Hardware*: versão sequencial em uma estação com processadores quad-core de 2,5 GHz e 16 GB; versão
  paralela num **cluster Hadoop de 1.968 nós** (dois quad-core 2,5 GHz e 16 GB cada) (p. 626).
  *Baselines*: Random, k-means++ e Partition (Ailon et al.). Medianas de 11 execuções nas tabelas de
  custo.
- **Resultados.** Custo final do k-means‖ comparável ao do k-means++ e muito menor que o Random
  (Tabelas 1–2, p. 627; p. ex. Spam, k = 50: 65–66 contra 68 para o k-means++ e 1.488 para o Random,
  em unidades de 10⁵). Tempo no KDD, em minutos, para k = 500 / 1000 (Tabela 4, p. 627): Random
  300,0 / 489,4; Partition 420,2 / 1.021,7; k-means‖ com ℓ = 0,5k: 69,0 / 46,2; ℓ = k: 75,6 / 89,1;
  ℓ = 2k: 69,8 / 86,7; ℓ = 10k: 75,7 / 101,0. O k-means‖ escolhe só 10–40% dos centros intermediários
  do Partition (Tabela 5, p. 628). Iterações de Lloyd até convergir, no Spam, média de 10 execuções,
  k = 20 / 50 / 100 (Tabela 6, p. 628): Random 176,4 / 166,8 / 60,4; k-means++ 38,3 / 42,2 / 36,6;
  k-means‖ (ℓ = 2k, r = 5) 23,3 / 28,1 / 29,7.
- **Rodadas e superamostragem** (§5.3, p. 628): com r·ℓ < k o resultado é bem pior que o do k-means++;
  com r·ℓ ≥ k fica equivalente; o ponto ótimo experimental é r ≈ 8, e a superamostragem ajuda para
  r ≤ 8.

**Onde usar no trabalho:**
- Relatório §1 (Introdução) / §2 (Algoritmo 1) — a observação de que a fase de atribuição/atualização
  é naturalmente paralelizável, e o problema que sobra (a inicialização) é sequencial.
- Relatório §4 (Análise de Escalabilidade) — contexto de como a literatura mede escalabilidade do
  k-means em larga escala (tempo, passes, iterações até convergir).
- Relatório §2 — se a inicialização adotada for k-means++ ou aleatória, este artigo registra o que
  cada uma custa em paralelo.

**Aplicabilidade ao nosso caso:** média, como contexto. O artigo trata de MapReduce em cluster, com
k grande (500–1000) e 4,8 milhões de pontos; o nosso cenário é memória compartilhada, uma máquina e
até 150.000 pontos. O valor está na observação sobre onde o paralelismo do k-means é fácil e onde
não é, e nas medidas de iterações até a convergência.

**Dado/afirmação específica a usar:**
> "Scaling k-means to massive data is relatively easy due to its simple iterative nature." (p. 622).

> "Given a set of cluster centers, each point can independently decide which center is closest to it
> and, given an assignment of points to clusters, computing the optimum center can be done by simply
> averaging the points." (p. 622) — justificativa do paralelismo de dados do passo de Lloyd.

> "A naive implementation of k-means++ initialization will make k passes over the data in order to
> produce the initial centers." (p. 623).

> "the running time can be exponential in the worst case [32, 4] and even though the final solution
> is locally optimal, it can be very far away from the global optimum (even under repeated random
> initializations)." (p. 622) — o limite do k-means padrão, citado como estado da arte em 2012.

> "Note that we have tacitly assumed that the set C of centers is small enough to be held in memory
> or be distributed among all the mappers." (p. 626).

> "In the sequential setting we ran Random until convergence, while in the parallel version, we
> bounded the number of iterations to 20." (p. 626) — condição do experimento paralelo.

**Limitações ou ressalvas:**
- **A escalabilidade com o número de máquinas é afirmada, não medida.** O texto diz que o tempo do
  k-means‖ "melhora linearmente com o número de máquinas disponíveis" (p. 627), mas nenhum
  experimento varia o número de nós; os experimentos comparam algoritmos num cluster fixo.
- No experimento paralelo, o Random foi limitado a 20 iterações de Lloyd (p. 626), enquanto o
  restante da comparação em tempo não declara limite equivalente; os tempos da Tabela 4 podem não ser
  comparáveis termo a termo.
- O foco é a **inicialização**; o artigo declara que o esforço anterior em paralelizar o k-means "se
  concentrou nas fases pós-inicialização" (resumo, p. 622) e não propõe nada novo para elas.
- O PDF é a versão do arXiv com o rodapé da VLDB; a paginação 622–633 foi conferida no texto, mas as
  figuras 5.1–5.3 (p. 629–631) só trazem as curvas, não os valores.
- Divergência com o `referencial_teorico.md` original: ele dizia que o k-means‖ "é a técnica de
  inicialização usada internamente pelo MLlib do Spark". Essa afirmação **não está neste artigo**
  (que cita só Hadoop e Mahout) e será verificada na ficha de Meng et al. (2016).

---

## Revisão de consistência dos slots KM1 e KM2

*(22/10/2026. Reler as quatro fichas em sequência.)*

- **Três camadas de "k-means" que a literatura mistura.** (1) MacQueen 1967: procedimento
  **sequencial**, uma observação por vez. (2) MacQueen §3.6, atribuindo a Forgy e Jennrich: o laço
  de **dois passos em lote**. (3) Lloyd 1957/1982: quantização **escalar** com as mesmas duas
  condições necessárias. Arthur & Vassilvitskii (2007) e Bahmani et al. (2012) chamam (2) de
  "algoritmo/iteração de Lloyd". O relatório deve dizer qual variante foi implementada e citar a fonte
  certa para ela.
- **Os quatro artigos concordam que a convergência não implica ótimo global:** MacQueen (p. 282 e o
  exemplo da p. 288), Lloyd (p. 131 e Apêndice B, p. 136), Arthur & Vassilvitskii (p. 2) e Bahmani
  et al. (p. 622). Arthur & Vassilvitskii é o primeiro dos quatro a **quantificar** isso para a
  semeadura (8(ln k + 2)).
- **Terminação.** MacQueen (p. 294) e Arthur & Vassilvitskii (p. 1, "no máximo kⁿ agrupamentos
  possíveis") dão terminação em número finito de passos; Bahmani et al. (p. 622) lembram que o tempo
  pode ser exponencial no pior caso. Não há contradição: são afirmações de naturezas diferentes
  (finitude × cota de tempo).
- **Sensibilidade ao ponto de partida.** MacQueen vê dependência da ordem dos dados (7%, p. 290) e da
  partição inicial no laço em lote (p. 294); Arthur & Vassilvitskii e Bahmani et al. tornam isso
  quantitativo, com a inicialização como o principal lugar onde se ganha qualidade e se reduzem
  iterações (Bahmani, Tabela 6).
- **Falta nesta fase:** nenhum dos quatro mede escalabilidade do passo de Lloyd com o número de
  processadores. Isso é assunto do slot KM3 (Dhillon & Modha 2000; Zhao et al. 2009; Ene et al. 2011).
  O resumo de Forgy (1965) citado por MacQueen não está no repositório.
- **Redundância:** Lloyd e MacQueen não se substituem: um dá as condições de otimalidade
  (unidimensionais), o outro o procedimento sobre amostras multivariadas.

---

## Slot KM3 — Paralelização do k-means em memória distribuída e MapReduce

*(3 fichas, 22/10/2026: Dhillon & Modha 2000, Zhao et al. 2009 e Ene et al. 2011. Lidos na íntegra.)*

### [KM3] Dhillon & Modha, 2000 — Um algoritmo de agrupamento de dados em multiprocessadores de memória distribuída

**Arquivo:** `DHILLON_2000_data_clustering_algorithm_distributed_memory_multiprocessors.pdf`

**Referência completa:**
Dhillon, I. S.; Modha, D. S. A data-clustering algorithm on distributed memory multiprocessors. In:
**Large-Scale Parallel Data Mining** (Lecture Notes in Computer Science, v. 1759, a conferir),
p. 245–260, Berlin: Springer, 2000. DOI: 10.1007/3-540-46502-2_13. Origem: workshop do KDD'99; o
Crossref data o capítulo de 2002 (publicação online), e adotamos 2000 (volume impresso).

**Chave BibTeX:** `dhillon2000data`

**Tipo:** Capítulo de livro (anais de workshop), revisado por pares.

**Versão lida:** manuscrito no formato LNCS (metadado do PDF: `pkmeans.dvi`), 17 páginas, com
numeração própria (texto 1–15, depois referências); a paginação do LNCS (245–260) não aparece.
**Páginas citadas = páginas do manuscrito** (iguais às do PDF).

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Dados conferidos no Crossref e em busca
bibliográfica (22/10/2026).

**Estado da leitura:** texto integral lido em 22/10/2026 (resumo, seções 1–5, figuras e legendas;
referências percorridas); citações literais conferidas por busca no texto extraído do PDF; os dados
da Fig. 3 foram conferidos também nos traços vetoriais da figura. **Conferência por integrante do
grupo pendente** — conferir no PDF antes de citar no relatório.

**O que o artigo diz** (lido na íntegra, p. 1–15). Achados principais:
- **Contribuição.** Uma versão paralela do k-means clássico em lote "direto", para máquinas de
  **memória distribuída** (*shared-nothing*), no modelo de troca de mensagens (SPMD com MPI)
  (p. 2–4). Os autores dizem não conhecer implementação paralela de k-means na literatura (p. 2).
- **Algoritmo** (Fig. 1 e 2, p. 5): o processo 0 escolhe os centróides iniciais e os difunde
  (`MPI_Bcast`); cada um dos P processos fica com um bloco de n/P pontos, calcula as distâncias aos k
  centróides, acumula somas parciais por centróide (vetor de somas, contagens e erro quadrático) e
  essas somas são combinadas com `MPI_Allreduce` a cada iteração; todos recalculam os centróides e
  decidem a parada de forma independente pelo MSE. Na implementação real, as 2k+1 reduções viram uma
  só sobre um bloco contíguo de memória (p. 7).
- **Custo** (§3.2, p. 6–8): o custo sequencial por iteração é ≈ (3nkd + nk + nd + kd) flops, dominado
  por 3nkd quando n é grande (T₁ ∼ 3nkd·I·T_flop); em paralelo, T_P ∼ 3nkd·I·T_flop/P + d·k·I·T_reduce(P),
  com T_reduce(P) = O(log P). A comunicação é desprezível frente à computação se
  P·T_reduce(P)/(3·T_flop) ≪ n (condição (7), p. 8): quanto maior n, mais perto do speedup linear.
  A condição não depende de d nem de k.
- **Definições** (p. 9 e 11–12): *speedup* relativo = tempo com 1 processador / tempo com P para o
  mesmo conjunto; *scaleup* relativo = tempo **por iteração** com n pontos em 1 processador / tempo por
  iteração com n·P pontos em P processadores (usa-se o tempo por iteração porque o número de iterações I
  varia com o conjunto).
- **Experimentos** (§4, p. 8–14). *Máquina:* IBM SP2 com até 16 nós (Thin Node 2, POWER2 a 160 MHz,
  256 MB cada), C e MPI. Os tempos **ignoram a leitura do conjunto do disco**; cada ponto é a média de
  5 medidas; os centróides iniciais são idênticos para qualquer P (p. 8–9). *Dados:* sintéticos, com
  8 agrupamentos, gerados por um algoritmo de terceiros (p. 8).
  - **Variando n** (d = k = 8; P = 1, 2, 4, 8, 16): cinco conjuntos de 2¹³, 2¹⁵, 2¹⁷, 2¹⁹ e 2²¹ pontos,
    com I = 3, 10, 8, 164 e 50 iterações (legenda da Fig. 3, p. 10). Speedup em 16 processadores:
    **15,62** para o maior e **6,22** para o menor (p. 9).
  - **Variando d** (2, 4, 8; n = 2²¹, k = 8): speedups de 15,42 a 15,53 em 16 processadores.
    **Variando k** (2, 4, 8, 16): 15,08 a 15,65 (p. 10). Ou seja, independentes de d e de k.
  - **Scaleup** (p. 11–14): tempo por iteração praticamente constante ao escalar n, k ou d
    proporcionalmente a P; o maior conjunto (n = 2²⁵ pontos, ≈ 2 GB) não cabe na memória de um nó
    (256 MB) e roda a ≈ 1,2 Gflops; com d = 128 (também ≈ 2 GB) chega a ≈ 1,8 Gflops. Para o escalamento
    em d o tempo por iteração fica **melhor que constante**, o que os autores atribuem a uma
    conjectura sobre menor custo de laço (p. 14).
- **Trabalho futuro** (§5, p. 14–15): o algoritmo "é facilmente adaptado a multiprocessadores de
  **memória compartilhada**": dividir os n pontos em P blocos, calcular as distâncias de cada bloco em
  um processador e manter uma cópia separada dos centróides para cada um, o que "pode ser
  implementado com *threads*". **Não foi implementado nem medido.** Propõem também a mesma estratégia
  para EM.
- **Resolução de uma inconsistência interna.** O texto da p. 9 chama de "menor conjunto" o de 2¹¹ pontos
  (e fala em crescer de 2¹¹ a 2²¹), mas a legenda da Fig. 3, as curvas da própria figura e a frase "cinco
  conjuntos" da p. 9 listam 2¹³ a 2²¹. **Adotamos 2¹³ como o menor conjunto, por ser o que foi
  observado:** nos traços da Fig. 3, os tempos com P = 1 diferem entre os cinco conjuntos
  como n·I (por exemplo, o segundo conjunto leva ≈ 13 vezes mais que o primeiro, razão de
  2¹⁵·10 / 2¹³·3), o que não ocorreria com 2¹¹; e as razões tempo(1)/tempo(16) lidas na figura
  reproduzem 15,6 e 6,2 (verificação nossa). O "2¹¹" do texto é erro de grafia.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means) — a decomposição por dados (blocos de pontos, somas parciais por
  centróide, redução global a cada iteração) como o modelo-base de paralelização do k-means.
- Relatório §4 (Análise de Escalabilidade) — definições de *speedup* e *scaleup* e a condição de que o
  ganho melhora com n; como a literatura mede (tempo por iteração; centróides iniciais fixos; sem I/O).
- Relatório §1 (Introdução) — motivação: dados que não cabem na memória de uma máquina.

**Aplicabilidade ao nosso caso** (Java, `ExecutorService`/`ForkJoin`, uma máquina, até 150.000
pontos): **alta como referência conceitual e metodológica**, média como referência de resultado. O
modelo de execução do artigo é memória distribuída com MPI em máquinas de 1990; a adaptação a memória
compartilhada é só descrita (p. 14–15). O intervalo de n do artigo (2¹³ a 2²¹ ≈ 8 mil a 2 milhões)
contém o nosso (150.000 ≈ 2¹⁷), mas o texto dá só os extremos de speedup (6,22 e 15,62); os valores
intermediários estão apenas na Fig. 4. Descritivo; não é recomendação de implementação.

**Dado/afirmação específica a usar:**
> "We analytically show that the speedup and the scaleup of our algorithm approach the optimal as the
> number of data points increases." (p. 1) — a tese central do artigo.

> "a simple, but effective, parallelization strategy is to divide the n data points into P blocks
> (each of size roughly n/P) and compute lines 14-21 for each of these blocks in parallel on a
> different processor." (p. 6) — a decomposição por dados.

> "each iteration of our parallel k-means algorithm consists of an asynchronous computation phase
> followed by a synchronous communication phase." (p. 7).

> "Relative speedup is defined as the ratio of the execution time for clustering a data set into k
> clusters on 1 processor to the execution time for identically clustering the same data set on P
> processors." (p. 9) — definição de speedup relativo.

> "for a fixed data set, identical starting cluster centroids are used–irrespective of the number of
> processors used." (p. 9) — controle experimental para que o número de iterações não varie com P.

> "we observe a relative speedup of 15.62 on 16 processors." (p. 9).

> "that relative speedup flattens at 6.22 on 16 processors." (p. 9) — o speedup do menor conjunto.

> "Our algorithm is also easily adapted to shared memory multiprocessors where all processors have
> access to the same memory space." (p. 14).

> "Such an algorithm can be implemented on a shared memory machine using threads [42]." (p. 15).

**Limitações ou ressalvas:**
- Memória distribuída (SP2, rede de troca de alto desempenho), dados sintéticos com 8 grupos e k = 8
  na maioria dos testes, d ≤ 128; sem I/O nos tempos (p. 8) e com centróides iniciais idênticos (p. 9),
  condições que facilitam o speedup.
- O número de iterações varia muito entre os conjuntos (3 a 164, Fig. 3), então os tempos totais entre
  tamanhos não são comparáveis; por isso o artigo usa tempo por iteração no *scaleup*.
- Os valores de speedup intermediários só existem como curva (Fig. 4–6); o texto cita apenas os extremos.
- A adaptação a *threads* é uma proposta de uma frase, sem experimento: não há, neste artigo, nenhum
  resultado de speedup em memória compartilhada.
- A afirmação de que não havia implementação paralela do k-means na literatura é dos autores; este
  levantamento não a verifica.
- Concordância com o referencial original: a descrição "atribuição local + soma global dos centróides"
  se confirma, mas o mecanismo é `MPI_Allreduce` (não *map*/*reduce*).

---

### [KM3] Zhao, Ma & He, 2009 — K-means paralelo baseado em MapReduce (PKMeans)

**Arquivo:** `ZHAO_2009_parallel_k_means_clustering_based_on_mapreduce.pdf`

**Referência completa:**
Zhao, W.; Ma, H.; He, Q. Parallel k-means clustering based on MapReduce. In: Jaatun, M. G.; Zhao, G.;
Rong, C. (eds.) **Cloud Computing (CloudCom 2009)**, LNCS 5931, p. 674–679, Springer, 2009.
DOI: 10.1007/978-3-642-10665-1_71.

**Chave BibTeX:** `zhao2009parallel`

**Tipo:** Artigo de conferência (LNCS), revisado por pares.

**Versão lida:** PDF da editora (6 páginas), paginação impressa 674–679 conferida no cabeçalho.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Dados conferidos no cabeçalho do PDF e
no Crossref (22/10/2026).

**Estado da leitura:** texto integral lido em 22/10/2026 (resumo, seções 1–4, pseudocódigos e
figura); citações literais conferidas por busca no texto extraído do PDF. **Conferência por integrante
do grupo pendente** — conferir no PDF antes de citar no relatório.

**O que o artigo diz** (lido na íntegra, p. 674–679). Achados principais:
- **Contribuição.** O PKMeans: o k-means de MacQueen [10] sobre MapReduce/Hadoop, em um único tipo
  de *job* (p. 675–677). Os autores justificam dizendo que os algoritmos paralelos anteriores
  (refs. 1–3) supõem todos os objetos na memória e usam modelos de programação restritos (p. 674).
- **Análise** (§2.1, p. 675): o cálculo mais intenso é o das distâncias (nk por iteração); as
  distâncias de um objeto aos centros são independentes das dos outros, logo podem ser paralelizadas;
  como os novos centros são necessários na iteração seguinte, "os procedimentos iterativos devem ser
  executados em série".
- **Projeto** (§2.2, p. 676–677): a função *map* atribui cada amostra ao centro mais próximo
  (entrada: *offset* e registro; variável global `centers`; saída: índice do centro e a amostra); a
  função *combine*, executada ao fim de cada tarefa *map*, soma parcialmente as amostras do mesmo
  centro e conta quantas são, para reduzir o tráfego de rede; a função *reduce* soma as parciais de
  todos os nós e divide pela contagem total para obter os novos centros (Algoritmos 1–3).
- **Experimentos** (§3, p. 677–678). Cluster com computadores de dois núcleos de 2,8 GHz e 4 GB cada,
  Hadoop 0.17.0 e Java 1.5.0. Mede *speedup* (mesmo conjunto, 1 a 4 computadores, conjuntos de 1, 2, 4 e
  8 GB), *scaleup* (1, 2, 3 e 4 GB em 1, 2, 3 e 4 computadores) e *sizeup* (nº de computadores fixo,
  conjunto crescente) (Fig. 1). Conclusão: o PKMeans tem "muito bom" speedup, que melhora quando o
  conjunto cresce.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means) — como o mesmo particionamento de pontos e a mesma agregação de
  somas parciais se expressam em *map*/*combine*/*reduce*; o uso do *combiner* como agregação local.
- Relatório §4 (Análise de Escalabilidade) — uso das três métricas (*speedup*, *scaleup*, *sizeup*),
  com a ressalva de que aqui são apresentadas só como gráficos.

**Aplicabilidade ao nosso caso:** média. O resultado de maior interesse é a estrutura: atribuição
independente por ponto, agregação local antes da global e novos centros calculados por redução. O
cenário experimental (Hadoop, HDFS, 1–4 computadores, 1–8 GB) é distinto de memória compartilhada
em uma máquina. Descritivo.

**Dado/afirmação específica a usar:**
> "In k-means algorithm, the most intensive calculation to occur is the calculation of distances."
> (p. 675).

> "distance computations between different objects with centers can be parallel executed." (p. 675).

> "Hence the iterative procedures must be executed serially." (p. 675) — as iterações em si não são
> paralelizáveis.

> "The map function performs the procedure of assigning each sample to the closest center while the
> reduce function performs the procedure of updating the new centers." (p. 676).

> "In order to decrease the cost of network communication, a combiner function is developed to deal
> with partial combination of the intermediate values with the same key within the same map task."
> (p. 676).

> "To measure the speedup, we kept the dataset constant and increase the number of computers in the
> system." (p. 678).

**Limitações ou ressalvas:**
- **Conjunto de dados não descrito:** o texto não informa número de pontos, dimensões, k, distribuição
  nem origem; só o tamanho em GB (1 a 8 GB) (p. 678). Os resultados de *speedup*, *scaleup* e
  *sizeup* aparecem **só como curvas** (Fig. 1) e são qualificados com "muito bom" e "escala muito
  bem"; **nenhum valor numérico está no texto**.
- Só 1 a 4 computadores: o efeito de mais nós não é medido.
- Não descreve o laço externo (como cada iteração dispara um novo *job*, como os novos centros chegam
  à iteração seguinte, qual o critério de parada) nem a inicialização além de "k objetos aleatórios"
  (§2.1); o custo de iniciar *jobs* do Hadoop a cada iteração não é discutido.
- A frase "o conjunto é dividido e **globalmente difundido** a todos os *mappers*" (p. 676) é ambígua
  (os pseudocódigos mostram que o que todos os *mappers* conhecem é o vetor `centers`, não o conjunto).
- Não cita Dhillon & Modha (2000), que já propunha a mesma decomposição em MPI; a crítica aos trabalhos
  anteriores (refs. 1–3, sobre agrupamento hierárquico) não trata de k-means paralelo.
- Concordância com o referencial original: a descrição *map* (atribuição) + *reduce* (novos centros) se
  confirma; o referencial omitia o *combiner*, que é parte central do projeto.

---

### [KM3] Ene, Im & Moseley, 2011 — Agrupamento rápido com MapReduce

**Arquivo:** `ENE_2011_fast_clustering_mapreduce.pdf`

**Referência completa:**
Ene, A.; Im, S.; Moseley, B. Fast clustering using MapReduce. In: **Proceedings of the 17th ACM SIGKDD
International Conference on Knowledge Discovery and Data Mining (KDD '11)**, p. 681–689, 2011.
DOI: 10.1145/2020408.2020515.

**Chave BibTeX:** `ene2011fast`

**Tipo:** Artigo de conferência (KDD '11), revisado por pares. O PDF é o *preprint* arXiv.

**Versão lida:** **arXiv:1109.1579v1** [cs.DC], 7 set. 2011, 20 páginas (a data "29 de outubro de 2018"
na capa é a da compilação do PDF). A versão do KDD tem 9 páginas; as páginas citadas abaixo são as do
arXiv, e o conteúdo pode diferir do artigo publicado.

**Origem:** trazido por Adriano (commit `2f4599a`, 21/10/2026). Dados do KDD conferidos no Crossref
(22/10/2026).

**Estado da leitura:** texto integral lido em 22/10/2026 (resumo, seções 1, 2, 4 e 5, tabelas das
Fig. 1–2; as demonstrações da seção 3 foram apenas percorridas); citações literais conferidas por
busca no texto extraído do PDF. **Conferência por integrante do grupo pendente** — conferir no PDF
antes de citar no relatório.

**O que o artigo diz** (lido na íntegra, p. 1–20). Achados principais:
- **Problema.** Agrupamento **k-center e k-median** métricos em MapReduce (p. 1–2). **Não é um artigo
  sobre k-means:** o k-means só aparece (i) como algoritmo de Lloyd usado como sub-rotina e como
  comparação, e (ii) numa frase da conclusão (p. 18). A entrada é um grafo completo com as Θ(n²)
  distâncias dadas explicitamente (p. 2).
- **Algoritmo.** *Iterative-Sample* (p. 5–7): amostra pontos aleatoriamente, descarta os já "bem
  representados" pela amostra (comparando com um ponto-pivô) e repete até restar pouco; o resultado
  (amostra mais o que sobrou) é agrupado numa só máquina. *MapReduce-kCenter* e *MapReduce-kMedian*
  (Alg. 4–5, p. 8); no k-median cada ponto da amostra recebe um peso (nº de pontos que o escolheram
  como mais próximo). **Nenhuma menção a *coresets*:** a técnica é amostragem.
- **Garantias** (Teoremas 1.1 e 1.2, p. 3): aproximação de fator constante, com alta probabilidade,
  em O(1/δ) rodadas de MapReduce e memória O(k²nᵟ) por máquina. Mostram que os problemas estão em
  MRC⁰ (constante de rodadas, memória e máquinas sublineares, p. 3 e 5).
- **Baselines** (§4.1, p. 15–17): Lloyd paralelo (*Parallel-Lloyd*), busca local, e um esquema de
  partição (*Divide*, de Guha et al.). O *Parallel-Lloyd* particiona os pontos uniformemente entre as
  máquinas, envia os k centros a cada uma, que atribui localmente os pontos e calcula médias e
  contagens por centro; estas são agregadas para atualizar os centros. "A solução é a mesma da versão
  sequencial do Lloyd" (p. 16).
- **Experimentos** (§4.2–4.3, p. 17–18). *Dados:* pontos aleatórios em R³ ao redor de k = 25 centros num
  cubo unitário (σ = 0,1; tamanhos de grupo por Zipf com α = 0), de 10.000 a 10 milhões de pontos.
  ***Todos os experimentos rodaram em uma única máquina*** (Intel Core i7 870 a 2,93 GHz, 8 GB); as 100
  máquinas do MapReduce foram **simuladas**, somando por rodada o tempo da máquina mais lenta e
  **ignorando o custo de comunicação**; cada configuração foi repetida 3 vezes (p. 17).
- **Resultados observados nas tabelas** (Fig. 1, p. 14, de 10 mil a 1 milhão de pontos; Fig. 2, p. 14,
  de 2 a 10 milhões). Tempos em segundos; custo relativo ao do *Parallel-Lloyd*:

  | Pontos | Parallel-Lloyd | Sampling-Lloyd | Divide-Lloyd | Sampling-LocalSearch | Custo Sampling-Lloyd |
  |---|---|---|---|---|---|
  | 1.000.000 | 205,7 | 4,0 | 2,7 | 11,0 | 1,132 |
  | 2.000.000 | 458,0 | 8,0 | 8,3 | 16,3 | 1,064 |
  | 5.000.000 | 1.333,3 | 18,3 | 24,7 | 29,3 | 1,106 |
  | 10.000.000 | 702,3 | 38,0 | 50,7 | 86,3 | 1,073 |

  Para 10 mil a 40 mil pontos, o *LocalSearch* leva 666,7 a 2.569,3 s, contra 1,7 a 3,0 s do
  *Sampling-LocalSearch*. O custo do *Sampling-Lloyd* relativo ao do *Parallel-Lloyd* varia de 1,051 a
  1,165 em todos os tamanhos da Fig. 1.
- **Resolução de inconsistências entre texto e tabelas** (adotamos as tabelas, por serem os dados
  observados): (i) o texto resume o ganho sobre o *Parallel-Lloyd* como "cerca de 20×" (p. 3 e 17), mas
  as tabelas dão razões de ≈ 51× (1 milhão), ≈ 57× (2 milhões), ≈ 73× (5 milhões) e ≈ 18× (10 milhões)
  — cálculo nosso a partir dos tempos acima; "20×" corresponde ao menor ganho, o de 10 milhões;
  (ii) o texto fala em perda "pequena" ou "desprezível" de qualidade, mas as tabelas mostram custo
  **de 5% a 16,5% maior** que o do Lloyd paralelo (13,2% com 1 milhão de pontos e 7,3% com 10
  milhões); (iii) o tempo do *Parallel-Lloyd* com 10 milhões (702,3 s) é **menor** que com 5 milhões
  (1.333,3 s), sem explicação no texto; como o número de iterações do Lloyd não é informado, não é
  possível atribuir a diferença.
- **k-center:** o método amostral piora o objetivo em até um fator 4 em alguns casos, "porque o
  objetivo do k-center é sensível à amostragem" (p. 3 e 14); por isso a seção de experimentos trata só
  do k-median.
- **k-means:** "evidência preliminar" de que a análise se estende ao k-means euclidiano (p. 18);
  sem teorema nem experimento.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means) — a descrição do Lloyd paralelo (partição fixa dos pontos, envio
  dos k centros, médias e contagens locais, agregação) e a nota de que o resultado é idêntico ao
  sequencial; o contraste entre paralelização **exata** e paralelização por **amostragem**.
- Relatório §4 (Análise de Escalabilidade) — como medir tempo em simulação e o que isso deixa de fora
  (comunicação); referência da prática de reportar tempo e custo normalizado.

**Aplicabilidade ao nosso caso:** só como contexto. O objeto do artigo é k-median/k-center, em
MapReduce simulado, com garantia de aproximação e entrada de n² distâncias; o k-means sobre pontos
euclidianos em R^d é tratado apenas de passagem. A descrição do Lloyd paralelo (p. 15–16) é, entre as
três fichas do slot, a única em que o autor afirma explicitamente a equivalência com o sequencial.

**Dado/afirmação específica a usar:**
> "We focus on the practical and popular clustering problems, k-center and k-median." (p. 1) —
> corrige o escopo atribuído ao artigo.

> "Our algorithms use sampling to decrease the data size and they run a time consuming clustering
> algorithm such as local search or Lloyd's algorithm on the resulting data set." (p. 1).

> "It is important to note that the solution computed by the algorithm is the same as the sequential
> version of Lloyd's algorithm." (p. 16) — equivalência do Lloyd paralelo com o sequencial.

> "All experiments were performed on a single machine. When running MapReduce algorithms, we
> simulated each machine used by the algorithm." (p. 17).

> "In these experiments, the communication cost was ignored." (p. 17).

> "A careful reader may note that Lloyd's algorithm is generally used for the k-means objective and
> not for k-median." (p. 15) — o próprio artigo admite o desvio de objetivo.

> "We note that we have preliminary evidence that the analysis used for the k-median problem can be
> extended to the k-means problem in Euclidean space" (p. 18).

**Limitações ou ressalvas:**
- **Não é k-means.** Objetivo k-median/k-center, métrico, com as n² distâncias na entrada (p. 2).
- Resultados de tempo em **simulação** numa máquina, sem comunicação (p. 17): não medem escalabilidade
  real, e os "100 nós" são um parâmetro do modelo.
- Dados sintéticos em apenas R³, k = 25; as variações de α, k e σ foram "omitidas" por espaço (p. 18).
- *Preprint* estendido; a versão do KDD tem 9 páginas e pode não conter as mesmas tabelas.
- Divergências com o referencial original: (i) "*coresets*" — a palavra não aparece no texto; (ii)
  "k-means/k-median/k-center" — o artigo trata de k-median e k-center; (iii) o referencial o apresentava
  como contraponto às estratégias de Dhillon & Modha e de Zhao et al., mas o artigo **não cita nenhum
  dos dois**: o Lloyd paralelo vem das refs. [28, 7, 1] do próprio artigo (Lloyd, um blog e uma página
  do Google sobre MapReduce).

---

## Slot KM4 — Escala prática: mini-lote e plataforma

*(2 fichas, 22/10/2026: Sculley 2010 e Meng et al. 2016. Lidos na íntegra.)*

### [KM4] Sculley, 2010 — Agrupamento k-means em escala web (mini-lote)

**Arquivo:** `SCULLEY_2010_webscale_kmeans_clustering.pdf`

**Referência completa:**
Sculley, D. Web-scale k-means clustering. In: **Proceedings of the 19th International Conference on
World Wide Web (WWW '10)**, p. 1177–1178, Raleigh, NC, 2010. DOI: 10.1145/1772690.1772862.

**Chave BibTeX:** `sculley2010webscale`

**Tipo:** Artigo curto (*poster*), 2 páginas, revisado por pares.

**Versão lida:** PDF do pôster, paginação impressa 1177–1178 conferida.

**Origem:** trazido por Adriano (commit `2f4599a`, 21/10/2026). Dados conferidos no Crossref
(22/10/2026).

**Estado da leitura:** texto integral lido em 22/10/2026 (as duas páginas); citações literais
conferidas por busca no texto extraído do PDF. **Conferência por integrante do grupo pendente** —
conferir no PDF antes de citar no relatório.

**O que o artigo diz** (lido na íntegra, p. 1177–1178). Achados principais:
- **Problema.** O k-means em lote é caro para conjuntos grandes (O(kns), com s o máximo de
  não-zeros por exemplo) e a variante *online* de Bottou & Bengio (gradiente descendente estocástico, um
  exemplo por vez) converge rápido mas encontra soluções piores por ruído estocástico (p. 1177).
- **Proposta 1: k-means em mini-lotes** (Algoritmo 1, p. 1177). A cada uma de t iterações, sorteia b
  exemplos, guarda o centro mais próximo de cada um e depois atualiza cada centro com taxa de
  aprendizado por centro η = 1/v[c] (v[c] é a contagem acumulada de exemplos do centro). Os centros
  iniciais são k exemplos sorteados. A convergência "segue de perto" o resultado de Bottou & Bengio
  (p. 1177), sem prova no pôster.
- **Experimentos** (p. 1177–1178). Coleção RCV1: 781.265 exemplos de treino e 23.149 de teste; mesmos
  centros iniciais aleatórios em todos os métodos; erro = fração acima do melhor valor do objetivo
  encontrado pelo k-means em lote rodado até convergir, avaliado no conjunto de teste; b = 1000,
  "escolhido em testes iniciais, com resultados robustos para uma faixa de b"; k = 3, 10, 50 (Fig. 1).
  O mini-lote chegou a um valor quase ótimo **várias ordens de grandeza mais rápido** que o lote e
  com soluções significativamente melhores que o SGD; testes omitidos por espaço indicam ser "várias
  vezes mais rápido" que o k-means em lote com desigualdade triangular. Para k pequeno, produziu
  centros quase ótimos para quase um milhão de documentos em uma fração de segundo de CPU numa máquina
  comum de 2,4 GHz.
- **Proposta 2: centros esparsos** (§3, p. 1178): projeção na bola L1 após cada mini-lote, com um
  algoritmo de bisseção (ε-L1, Alg. 2) comparado ao de Duchi et al. (LTL1P). Com k = 10, b = 1000 e
  t = 16: o lote tem 200.319 não-zeros e 133,96 s de CPU; com λ = 5,0, LTL1P tem 46.446 não-zeros e
  0,51 s (objetivo de teste +0,4%), e ε-L1 tem 44.060 e 0,27 s (+0,7%); com λ = 1,0, LTL1P tem 3.181 e
  0,48 s (+1,8%), e ε-L1 tem 2.547 e 0,19 s (+2,8%).

**Onde usar no trabalho:**
- Relatório §2 / Discussão (Algoritmo 1: k-means) — alternativa que troca qualidade por custo por
  iteração; contextualiza o k-means "clássico" (lote completo) escolhido para paralelizar.
- Relatório §4 (Análise de Escalabilidade) — o trade-off tempo × qualidade quando o conjunto cresce.

**Aplicabilidade ao nosso caso:** só como contexto. O pôster é sequencial, para dados esparsos de
texto (RCV1) e altas dimensões, e modifica o algoritmo (estocástico): não reproduz o resultado do
k-means em lote. Descritivo.

**Dado/afirmação específica a usar:**
> "This reduces computation cost by orders of magnitude compared to the classic batch algorithm while
> yielding significantly better solutions than online stochastic gradient descent." (p. 1177).

> "The classic batch k-means algorithm is expensive for large data sets, requiring O(kns) computation
> time where n is the number of examples and s is the maximum number of non-zero elements in any
> example vector." (p. 1177) — custo por iteração do lote.

> "While SGD converges quickly on large data sets, it finds lower quality solutions than the batch
> algorithm due to stochastic noise [1]." (p. 1177).

> "The mini-batch method converged to a near optimal value several orders of magnitude faster than the
> full batch method, and also achieved significantly better solutions than SGD." (p. 1178).

> "We set the mini-batch b to 1000 based on separate initial tests; results were robust for a range of
> b." (p. 1178).

**Limitações ou ressalvas:**
- Pôster de duas páginas: experimentos com um único conjunto (RCV1, texto esparso) e três valores de k;
  os resultados são curvas (Fig. 1), sem tabela de tempos do mini-lote; os testes contra o lote com
  desigualdade triangular foram omitidos.
- Algoritmo estocástico: o resultado é "quase ótimo", não idêntico ao do lote; o pôster não quantifica
  a perda além das curvas de erro (escala de até ≈ 0,03 a 0,1 do objetivo, conforme k).
- Execução sequencial numa máquina de 2,4 GHz; nenhuma análise de paralelismo.
- A segunda contribuição (esparsidade por projeção L1) não é relevante para dados densos de baixa
  dimensão; o referencial original não a mencionava.
- Concordância com o referencial original: a descrição do mini-lote e da troca de tempo por qualidade
  se confirma.

---

### [KM4] Meng et al., 2016 — MLlib: aprendizado de máquina no Apache Spark

**Arquivo:** `MENG_2016_mllib_machine_learning_in_apache_spark.pdf`

**Referência completa:**
Meng, X.; Bradley, J.; Yavuz, B.; Sparks, E.; Venkataraman, S.; Liu, D.; Freeman, J.; Tsai, D. B.;
Amde, M.; Owen, S.; Xin, D.; Xin, R.; Franklin, M. J.; Zadeh, R.; Zaharia, M.; Talwalkar, A. MLlib:
machine learning in Apache Spark. **Journal of Machine Learning Research**, v. 17, n. 34 (a conferir),
p. 1–7, 2016.

**Chave BibTeX:** `meng2016mllib`

**Tipo:** Artigo curto de descrição de software (*software paper*), revisado por pares.

**Versão lida:** PDF da JMLR ("Submitted 5/15; Published 4/16"), 7 páginas, paginação impressa
1–7 conferida (página do PDF = página impressa).

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Dados do cabeçalho conferidos.

**Estado da leitura:** texto integral lido em 22/10/2026 (7 páginas, incluindo a Fig. 2 renderizada);
citações literais conferidas por busca no texto extraído do PDF. **Conferência por integrante do grupo
pendente** — conferir no PDF antes de citar no relatório.

**O que o artigo diz** (lido na íntegra, p. 1–7). Achados principais:
- **O que é.** MLlib é a biblioteca de aprendizado de máquina distribuída do Apache Spark; cobre
  classificação, regressão, filtragem colaborativa, agrupamento e redução de dimensionalidade (p. 2).
  Escrita em Scala, com bibliotecas de álgebra linear nativas, e API em Java, Scala e Python (p. 2).
- **Histórico e comunidade** (§2, p. 3): desenvolvimento desde 2012, código aberto desde set. de 2013;
  de 11 contribuidores iniciais a mais de 140 no Spark 1.4.
- **Funcionalidades** (§3, p. 3–4). O **k-means aparece uma única vez, numa lista de métodos**
  ("k-means clustering and principal component analysis for clustering and dimensionality reduction").
  As otimizações citadas são de outros algoritmos (ALS com blocos, árvores de decisão, modelos lineares);
  em geral, a agregação em árvore evita que o *driver* seja gargalo e o *broadcast* do Spark
  distribui rapidamente modelos grandes aos trabalhadores (p. 4).
- **Desempenho e escala** (§4, p. 5). *Benchmark* de ALS com cópias ampliadas do conjunto de avaliações
  da Amazon, 5 iterações, cluster EC2 de 16 nós (m3.2xlarge), MLlib 1.1 e 1.4 contra o Mahout 0.9
  (Hadoop MapReduce) (Fig. 2a). Ganho entre versões: nos dados sintéticos do `spark-perf` com 16
  trabalhadores, a Fig. 2(b) mostra o ganho de MLlib 1.0 → 1.1 por algoritmo: regressão logística 5,6×,
  *ridge* 1,2×, árvores de decisão 3,2×, ALS 2,8× e **KMeans 2,5×**; o texto resume "3× em média".
- **O que não está no artigo:** a descrição do algoritmo de k-means do MLlib (inicialização,
  paralelização, tratamento dos centróides), o tamanho e o número de dimensões dos dados do KMeans, o
  valor de k, ou qualquer medida de escalabilidade do k-means. A palavra "centroid" não aparece.

**Onde usar no trabalho:**
- Relatório §1 (Introdução) / §2 — contexto de que bibliotecas de plataforma (Spark/MLlib) incluem
  k-means distribuído como método padrão. **Não** serve como fonte do algoritmo.
- Relatório §4 — só como referência de que MLlib é o contexto de "k-means em Spark".

**Aplicabilidade ao nosso caso:** só contexto, e **baixa**. É um artigo de apresentação de biblioteca
de 7 páginas; não descreve o k-means nem sua paralelização.

**Dado/afirmação específica a usar:**
> "k-means clustering and principal component analysis for clustering and dimensionality reduction"
> (p. 3) — a única menção ao k-means no artigo.

> "Many algorithms benefit from efficient communication primitives; in particular tree-structured
> aggregation prevents the driver from being a bottleneck, and Spark broadcast quickly distributes
> large models to workers." (p. 4) — afirmação geral, que **não** nomeia o k-means.

> "The library targets large-scale learning settings that benefit from data-parallelism or
> model-parallelism to store and operate on data or models." (p. 2).

> "show a 3× speedup on average across all algorithms" (p. 5) — ganho entre versões da biblioteca, não
> speedup por número de nós.

**Limitações ou ressalvas:**
- Artigo de apresentação de software: nenhum detalhe do k-means; a Fig. 2(b) mostra o k-means só como
  uma barra (2,5×) de ganho entre versões do MLlib.
- O "speedup" do artigo é entre **versões** (1.0 → 1.1) e não entre números de nós; o *benchmark* de
  escala (Fig. 2a) é de ALS, não de k-means.
- **Divergências com o referencial original, corrigidas aqui:** (i) o referencial dizia que o artigo
  "descreve a implementação distribuída de k-means sobre RDDs (particionamento dos dados, *broadcast*
  dos centróides a cada iteração, agregação distribuída)" — **nada disso está no artigo**; o
  *broadcast* citado é uma afirmação geral sobre modelos (p. 4); (ii) o referencial dizia, na entrada
  de Bahmani et al., que o k-means‖ é a inicialização usada pelo MLlib — **o artigo não menciona o
  k-means‖** nem a inicialização do k-means.

---

## Revisão de consistência dos slots KM3 e KM4

*(22/10/2026. Reler as cinco fichas em sequência.)*

- **Mesmo modelo de paralelização, três vocabulários.** Dhillon & Modha (`MPI_Allreduce`), Zhao et al.
  (*map* + *combine* + *reduce*) e o *Parallel-Lloyd* de Ene et al. descrevem a **mesma decomposição**:
  blocos fixos de pontos por trabalhador; centros conhecidos por todos; atribuição local ao centro mais
  próximo com somas e contagens parciais por centro; combinação global; novo centro = soma/contagem.
  Ene et al. afirmam que o resultado coincide com o do Lloyd sequencial (p. 16); Dhillon & Modha fixam
  os centróides iniciais para isolar o efeito do paralelismo (p. 9). Não há contradição entre eles.
- **Quem mediu escalabilidade, e como.** **Só o Dhillon & Modha** reporta *speedup* e *scaleup*
  numéricos por número de processadores (6,22 e 15,62 em 16 processadores, conforme n) e a condição
  analítica de que o ganho melhora com n. Zhao et al. mostram curvas de 1 a 4 computadores sem
  valores no texto, e o conjunto de dados nem é descrito. Ene et al. medem em **simulação numa única
  máquina e sem comunicação**. Bahmani et al. (KM2) afirmam escala linear com o nº de máquinas sem
  variar o nº de nós. Ou seja: **o único resultado quantitativo de *speedup* do k-means em
  paralelo nos artigos lidos é o de Dhillon & Modha**, em MPI.
- **Memória compartilhada.** Dhillon & Modha dizem que a adaptação é direta e usa *threads*
  (p. 14–15), sem testá-la; nenhum outro artigo do slot mede *speedup* por *threads* de k-means.
  Essa é a **lacuna** mais relevante para o nosso cenário (uma máquina, `ExecutorService`/`ForkJoin`).
- **Exato × aproximado.** Há duas famílias: paralelização **exata** (Dhillon & Modha; Zhao et al.;
  *Parallel-Lloyd*) e **aproximada** (Ene et al., por amostragem; Sculley, por mini-lotes). Nas
  tabelas de Ene et al., o *Sampling-Lloyd* custa 5% a 16,5% mais que o Lloyd paralelo; em Sculley, a
  perda é "quase ótima", só em curva. Ambos trocam qualidade por tempo, e o ganho depende do tamanho do
  conjunto.
- **Resoluções de inconsistências internas (critério: vale o dado observado em figura/tabela).**
  Dhillon & Modha: menor conjunto = 2¹³ (não 2¹¹ do texto). Ene et al.: ganhos sobre o Lloyd
  paralelo de 18× a 73× nas tabelas (não "cerca de 20×") e perda de qualidade de 5% a 16,5% (não
  "desprezível"). O tempo do *Parallel-Lloyd* é não monótono em n (702,3 s com 10 milhões, 1.333,3 s
  com 5 milhões), sem explicação.
- **Redundância e peso.** Ene et al. e Meng et al. são **periféricos** ao k-means: o primeiro é sobre
  k-median/k-center, e o segundo é descrição de biblioteca. A evidência central do slot é Dhillon &
  Modha (modelo e métricas), com Zhao et al. como a tradução para MapReduce.
