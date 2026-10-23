# Fichamentos da revisão de literatura: k-means e DBSCAN

> Uma ficha completa por artigo lido por inteiro. Os artigos só coletados ficam na fila de
> `revisao_literatura.md` §4. O molde e as regras estão em `fichamento_literatura_template.md`. Ao
> terminar uma ficha, a linha da fila passa para "Artigos lidos" (`revisao_literatura.md` §5) e a
> entrada vai para `referencias.bib`.
>
> As fichas seguem a ordem dos slots de `revisao_literatura.md` §2. A revisão foi escrita em duas
> etapas nos mesmos arquivos: primeiro o k-means (KM1 a KM4), depois o DBSCAN (DB1 a DB4), que vem
> mais abaixo neste arquivo.
>
> As divergências internas dos artigos (texto contra figura ou tabela) e o valor adotado estão em
> `revisao_literatura.md` §9.
>
> Convenção de página: quando o PDF traz a paginação do veículo, cito a página impressa. Quando não
> traz (Arthur & Vassilvitskii, Patwary et al., Götz et al., He et al. 2011), cito "p. N do PDF".
> Toda citação literal foi conferida no texto extraído do PDF.

---

## Slot KM1: Formulação original do k-means

*(2 fichas, 22/10/2026: MacQueen 1967 e Lloyd 1982. Lidos por inteiro.)*

### [KM1] MacQueen, 1967: Alguns métodos para classificação e análise de observações multivariadas

**Arquivo:** `MACQUEEN_1967_some_methods_classification_analysis_multivariate_observations.pdf`

**Referência completa:**
MacQueen, J. Some methods for classification and analysis of multivariate observations. In:
**Proceedings of the Fifth Berkeley Symposium on Mathematical Statistics and Probability**, v. 1,
p. 281–297, Berkeley: University of California Press, 1967.

**Chave BibTeX:** `macqueen1967some`

**Tipo:** Artigo de conferência (clássico de referência); sem DOI.

**Versão lida:** reprodução digital do artigo publicado, com 17 páginas. A paginação impressa
(281–297) foi conferida no cabeçalho de cada página e vale página do PDF + 280.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Autor, título, veículo e páginas
conferidos no próprio PDF e nas listas de referências de Patwary et al. (2012) e Zhao et al. (2009),
que citam "vol. 1, pp. 281–297".

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, desenvolvimento, experimentos e
conclusão; as demonstrações foram apenas percorridas). Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 281–297):
- Objetivo: descrever "um processo para particionar uma população N-dimensional em k conjuntos a
  partir de uma amostra", chamado *k-means* (p. 281). O critério é a variância intra-classe W²(S).
  O texto diz que o processo "tende a" baixá-la, com base em razões intuitivas, análise matemática
  parcial e experiência computacional.
- O procedimento definido no artigo é sequencial (*online*) e não em lote (§2.1, p. 283). As
  primeiras k observações viram as k médias iniciais, e cada observação seguinte entra no grupo de
  média mais próxima, que é atualizada com peso (a contagem de pontos do grupo).
- Resultados assintóticos (§2, p. 283–288), com amostragem i.i.d. de uma medida absolutamente
  contínua num conjunto convexo limitado. O Teorema 1 mostra que W(xⁿ) converge quase certamente e
  que o limite coincide com V(x) para algum k-ponto "não enviesado" (cada ponto igual à média
  condicional da sua região). O Teorema 2 dá uma lei fraca para a distância entre as médias
  correntes e as condicionais. As provas usam martingales (Lema 3, p. 285).
- Não garante o ótimo. "Em geral o procedimento não converge para uma partição ótima" (p. 282). No
  exemplo do retângulo levemente alongado (§2.3, p. 288), o procedimento converge, com
  probabilidade positiva, para uma partição que "claramente não tem variância intra-classe mínima".
- Aplicações (§3, p. 288–294): agrupamento por similaridade, com parâmetros de *coarsening* C e
  *refinement* R que fundem médias próximas e criam novas (p. 289); classificação relevante;
  aproximação de uma distribuição por mistura de normais; teste de independência por embaralhamento
  de dimensões; árvores de classificação por distância. Na §3.6 (p. 294) aparece o procedimento de
  dois passos: calcular as médias de uma partição e reclassificar cada ponto para a média mais
  próxima. O autor o atribui a Forgy e Jennrich (comunicado em 1963; p. 282) e afirma que ele gera
  partições com variância intra-classe decrescente e "converge em um número finito de passos".
- Experimentos de pequena escala (p. 289–290): amostras reais de 70 faculdades (5 dimensões), 360
  palavras (20 dimensões), 760 documentos (15 dimensões) e 560 sujeitos (15 dimensões), avaliadas
  por inspeção. Para a estabilidade à ordem dos dados, usou 250 pontos em 5 dimensões, k = 18 e
  três ordens aleatórias; a variação intra-classe mudou no máximo 7% (p. 290). No IBM 7094, o
  processo levou cerca de 1 minuto para 200 pontos de 20 dimensões com C e R ativos. O k-means
  "sem enfeites" com k = 20 processou 500 pontos de 20 dimensões em cerca de 10 segundos (p. 290).
- Extensão a espaços métricos gerais (§4, p. 294–296) por meio do "centroide de ordem r" (r = 2 dá a
  média e r = 1 dá a mediana).

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means): origem do termo e do objetivo de variância intra-classe. Ao
  dizer qual variante foi implementada, distinguir a sequencial de MacQueen da de dois passos em lote.
- Relatório §1 (Introdução): citação histórica ao lado de Lloyd.

**Aplicabilidade ao nosso caso:** alta como referência de origem e de terminologia, nenhuma em
paralelismo ou escala (a maior amostra tem 760 pontos). O procedimento sequencial depende da ordem
de chegada dos pontos (p. 290). A variante em lote (§3.6) é a que os artigos posteriores (Arthur &
Vassilvitskii; Bahmani et al.) chamam de iteração de Lloyd; essa é uma inferência nossa, feita
comparando as descrições.

**Dado/afirmação específica a usar:**
> "The main purpose of this paper is to describe a process for partitioning an N-dimensional
> population into k sets on the basis of a sample." (p. 281). Fixa o que "k-means" significa na
> fonte original.

> "Stated informally, the k-means procedure consists of simply starting with k groups each of which
> consists of a single random point, and thereafter adding each new point to the group whose mean
> the new point is nearest." (p. 283). É a versão sequencial, que não é o laço atribuição/atualização
> em lote.

> "In general, the k-means procedure will not converge to an optimal partition, although there are
> special cases where it will." (p. 282). É o limite de qualidade que Arthur & Vassilvitskii (2007)
> quantificam décadas depois.

> "Starting with an arbitrary partition into k sets, the means of the points in each set are first
> computed. Then a new partition of the points is formed by the rule of putting the points into
> groups of the basis of nearness to the first set of means." (p. 294). É o procedimento de dois
> passos em lote (a grafia "of the basis" é do original).

> "This can be iterated and the series of the partitions thus produced have decreasing within-class
> variances and will converge in a finite number of steps." (p. 294).

> "When the sample points are rearranged in a new random order, there is some variation in the
> grouping which is obtained." (p. 290). Mostra a sensibilidade à ordem do procedimento sequencial.

**Limitações ou ressalvas:**
- A convergência demonstrada vale para o procedimento sequencial sob amostragem i.i.d. Para o laço
  em lote iterado até estabilizar num conjunto finito, a única garantia é a de terminação em número
  finito de passos (p. 294).
- A avaliação empírica é qualitativa, com amostras de centenas de pontos e sem medida de
  escalabilidade.
- A atribuição a Forgy e Jennrich cita um resumo da *Biometrics* de 1965 (referência [7], p. 296–297)
  que não está no repositório. Só foi possível verificar a atribuição pela palavra de MacQueen.
- O artigo é anterior a qualquer discussão de paralelismo, e nada nele sustenta uma estratégia de
  paralelização.

### [KM1] Lloyd, 1982: Quantização por mínimos quadrados em PCM

**Arquivo:** `LLOYD_1982_least_squares_quantization_in_pcm.pdf`

**Referência completa:**
Lloyd, S. P. Least squares quantization in PCM. **IEEE Transactions on Information Theory**, v. IT-28,
n. 2, p. 129–137, mar. 1982. DOI: 10.1109/TIT.1982.1056489. (Rascunho de 1957, circulado nos Bell
Laboratories; publicado em 1982.)

**Chave BibTeX:** `lloyd1982least`

**Tipo:** Artigo original (clássico de referência), revisado por pares; texto de 1957 publicado em
1982.

**Versão lida:** PDF digitalizado da *IEEE Trans. Inf. Theory*, com 9 páginas e paginação impressa
129–137 conferida no cabeçalho. A última página (137) traz o início de outro artigo, "The Hexagon
Theorem" de D. J. Newman, que não é de Lloyd e não foi fichado aqui.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Autor, título, volume, páginas e DOI
conferidos no cabeçalho do PDF e no Crossref (22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, desenvolvimento, experimentos e
conclusão; as demonstrações foram apenas percorridas). Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 129–137):
- Problema: em modulação por código de pulso (PCM), dado um número fixo e finito v de "quanta"
  (valores representativos) e as propriedades estatísticas do sinal, achar os subconjuntos do eixo
  de tensão e os quanta que minimizam a potência média do ruído de quantização (p. 129–130). O
  problema é unidimensional (uma tensão real x com distribuição F) e não envolve um conjunto de
  pontos em R^d.
- Duas condições necessárias (§IV–V, p. 130–131): (i) dado o particionamento, o melhor quantum de
  cada conjunto é o seu centro de massa (eq. 14); (ii) dados os quanta, o melhor particionamento tem
  fronteiras no ponto médio entre quanta sucessivos (eq. 15–17).
- As condições não bastam (§VI, p. 131). Os pontos que satisfazem as duas são "estacionários" e
  podem ser mínimos locais (vários) ou pontos de sela, e o autor "não conseguiu determinar
  condições suficientes para um mínimo absoluto". O Apêndice B (p. 136) dá exemplos com v − 1
  mínimos locais distintos.
- Método I (§VI, p. 131–132): impor as condições alternadamente, partindo de pontos de fronteira
  arbitrários. A cada passo o ruído não aumenta (N(p⁽ᵐ⁾) é não crescente) e, como o ruído é não
  negativo, o limite existe. Se N tem um único ponto estacionário, a sequência converge para ele, a
  menos que divirja. O Método II (p. 132–133) varre valores iniciais de q₁ e encadeia as condições,
  e serve para localizar todos os pontos estacionários.
- Exemplos numéricos (§VII, p. 133): para a distribuição uniforme, o ótimo são v intervalos iguais
  (N = 1/v²). Há tabelas para a gaussiana e a laplaciana com v = 2ᵇ, b = 1…7. Algumas tabelas
  "nunca foram completadas" e não estão no artigo (nota 3, p. 133).
- Comportamento assintótico (§VIII, p. 133–134): a densidade fracional de quanta é proporcional a
  f^(1/3) e N ≈ (1/12v²)(∫f^(1/3))³, o que recupera o resultado de Panter e Dite.
- Nota do autor de 1981 (p. 136–137): o texto é "quase uma reprodução literal" de um rascunho de
  31/07/1957. Não foi publicado antes porque os cálculos numéricos nunca foram completados. Uma
  condição suficiente para o mínimo único veio depois, de Fleischer (1964).

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means): origem histórica do par de condições "centro de massa" e
  "fronteira no ponto médio", que sustenta o passo de atualização e o de atribuição.
- Relatório §1 (Introdução): citação junto a MacQueen (1967).

**Aplicabilidade ao nosso caso:** média, como referência histórica. O artigo não trata de
agrupamento de pontos em R^d, de k-means, de critério de parada nem de complexidade; é quantização
escalar de um sinal. A associação "algoritmo de Lloyd = laço atribuição/atualização do k-means" vem
de trabalhos posteriores (Arthur & Vassilvitskii 2007, §1: "Lloyd [20] proposed a local search
solution"), e MacQueen (1967, p. 282) atribui o procedimento de dois passos sobre pontos a Forgy e
Jennrich.

**Dado/afirmação específica a usar:**
> "The number v of quanta is to be regarded throughout as a fixed finite preassigned number."
> (p. 129). É o análogo do k fixo do k-means.

> "We continue in this way, imposing conditions (14) and (17) alternately." (p. 132). É o esquema
> alternado do Método I.

> "This step does not increase the noise, in view of the discussion in Section V" (p. 132). Garante
> a monotonicidade do ruído a cada passo.

> "As it turns out, however, they are not sufficient conditions for a minimum of N(p)." (p. 131).
> Convergir a um ponto estacionário não implica ótimo global.

> "The author has not been able to determine sufficient conditions for an absolute minimum."
> (p. 131).

> "This is nearly a verbatim reproduction of a draft manuscript, which was circulated for comments
> at Bell Laboratories" (p. 136). Dá a data real da ideia (1957), anterior a MacQueen (1967).

**Limitações ou ressalvas:**
- É unidimensional, com distribuição F conhecida e v pequeno. Não há experimento em dados
  multivariados nem análise de custo computacional.
- Citar "Lloyd (1982)" para o algoritmo em R^d é uma atribuição por tradição e não conteúdo do
  artigo. No relatório convém dizer "iteração de Lloyd" e ancorar a descrição em Arthur &
  Vassilvitskii (2007, §2.1) ou em MacQueen (1967, §3.6).
- Arthur & Vassilvitskii (2007) citam o artigo com páginas "129–136" (lista de referências, p. 9 do
  PDF deles). O correto é 129–137, conforme o cabeçalho do PDF e o Crossref.

---

## Slot KM2: Inicialização do k-means

*(2 fichas, 22/10/2026: Arthur & Vassilvitskii 2007 e Bahmani et al. 2012. Lidos por inteiro.)*

### [KM2] Arthur & Vassilvitskii, 2007: k-means++, as vantagens de uma semeadura cuidadosa

**Arquivo:** `ARTHUR_2007_k_means_plusplus_advantages_of_careful_seeding.pdf`

**Referência completa:**
Arthur, D.; Vassilvitskii, S. k-means++: the advantages of careful seeding. In: **Proceedings of the
Eighteenth Annual ACM-SIAM Symposium on Discrete Algorithms (SODA '07)**, p. 1027–1035,
Philadelphia: SIAM, 2007. DOI: 10.5555/1283383.1283494 (a conferir: o DOI veio de uma listagem
secundária; as páginas foram conferidas na lista de referências de Ene et al. 2011 e em listagens
bibliográficas).

**Chave BibTeX:** `arthur2007kmeanspp`

**Tipo:** Artigo de conferência, revisado por pares.

**Versão lida:** manuscrito do autor (o metadado de criação do PDF é de 02/10/2006, e o texto não traz
veículo nem paginação do SODA). São 9 páginas, e as páginas citadas abaixo são do PDF.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Título e autores conferidos no PDF. O
veículo e as páginas vêm de fontes externas e das referências cruzadas de outros artigos do repo.

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, desenvolvimento, experimentos e
conclusão; as demonstrações foram apenas percorridas). Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 1–9 do PDF):
- Problema: o k-means (iteração de Lloyd) é simples e rápido, mas "o custo da velocidade empírica é
  a precisão". Há exemplos em que gera agrupamentos arbitrariamente ruins (φ/φ_OPT ilimitado),
  inclusive com a semeadura aleatória padrão (p. 1). Resolver o problema de forma exata é NP-difícil,
  mesmo com dois grupos (p. 1).
- Algoritmo k-means++ (§2.2, p. 2–3): o primeiro centro é escolhido uniformemente. Cada centro
  seguinte x′ é escolhido com probabilidade D(x′)²/Σ D(x)² ("ponderação D²"), em que D(x) é a
  distância ao centro já escolhido mais próximo. Depois segue o k-means padrão (passos 2–4).
- Garantia (Teorema 1.1/3.1, p. 1 e 3–5): após a semeadura, E[φ] ≤ 8(ln k + 2) φ_OPT, ou seja, o
  algoritmo é O(log k)-competitivo em esperança e para qualquer conjunto de dados. A garantia é
  provada para o passo de semeadura; as iterações seguintes "só podem diminuir φ" (p. 3).
- A análise é justa a menos de constante (§4, p. 5–6): existe uma instância (k simplexes regulares
  em dimensões ortogonais) em que a semeadura D² não é melhor que 2(ln k)-competitiva (Teorema 4.1).
- Generalização (§5, p. 6): a semeadura Dℓ vale em espaços métricos arbitrários e para o potencial
  φ[ℓ] (ℓ = 1 é o k-median), com E[φ[ℓ]] ≤ 2²ℓ (ln k + 2) φ[ℓ]_OPT (Teorema 5.1).
- Trabalho relacionado (§1.1, p. 1–2): Ostrovsky et al. propuseram "essencialmente o mesmo
  algoritmo" de forma independente, com garantia O(1) sob uma condição de separação
  (φ_OPT,k / φ_OPT,k−1 ≤ ε²). Combinando os dois, o k-means++ nunca é pior que O(log k) e melhora
  para O(1) em dados bem formados.
- Experimentos "preliminares" (§6, p. 6–8), com implementação em C++ "padrão, sem otimizações
  especiais", 20 execuções por caso e k = 10, 25 e 50. Os conjuntos são Norm25 (sintético: 25
  centros num hipercubo 15-D de lado 500, gaussianas de variância 1; n = 10.000), Cloud (n = 1.024,
  d = 10), Intrusion (n = 494.019, d = 35) e Spam (n = 4.601, d = 58). O k-means++ foi melhor em
  potencial e em tempo médio na maioria dos casos. No Intrusion, o k-means leva em média 63,94 s
  (k = 10), 257,34 s (k = 25) e 917,00 s (k = 50), e o k-means++ reduz o tempo em 44,49%, 49,19% e
  66,70% (Tabela 3, p. 7). Nos conjuntos Spam e Intrusion os potenciais ficaram de 20 a 1.000 vezes
  menores (p. 8). Há uma exceção: no Norm25 com k = 50, o tempo médio do k-means++ foi 1,62% pior
  (Tabela 1, p. 7).
- Em dados sintéticos bem separados, a semeadura uniforme "inevitavelmente funde" alguns
  agrupamentos verdadeiros, que a busca local nunca consegue separar. O k-means++ "quase sempre"
  atingiu o agrupamento ótimo (p. 8).
- Questões em aberto (§7, p. 8): se várias execuções melhoram o limite assintótico. Uma variante
  que sorteia vários candidatos por iteração e escolhe o que mais reduz φ "geralmente teve melhor
  desempenho" nos experimentos, sem prova.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means): declarar a estratégia de inicialização adotada e justificá-la
  com a literatura (aleatória ou D²). O artigo é a referência de k-means++.
- Relatório §3 (Setup Experimental): relação entre o tipo de dataset (gaussianas bem separadas ou
  densidades diferentes) e o risco de semeadura ruim.
- Relatório §4 (Análise de Escalabilidade): o tempo total depende do número de iterações de Lloyd,
  que a semeadura altera (p. 8).

**Aplicabilidade ao nosso caso:** média a alta para a decisão de inicialização. O artigo é
inteiramente sequencial e não discute paralelismo. O `dataset1` do repositório (blobs gaussianos bem
separados, 150.000 pontos) se parece com o Norm25 (inferência nossa, a partir da descrição em
`data/README.md`), cenário em que o artigo relata o maior ganho do k-means++.

**Dado/afirmação específica a usar:**
> "Specifically, we choose a point p as a center with probability proportional to p's contribution to
> the overall potential." (p. 1). É a regra da ponderação D².

> "Unfortunately, it is guaranteed only to find a local optimum, which can often be quite poor."
> (p. 2). É o limite do k-means padrão.

> "Solving this problem exactly is NP-hard, even with just two clusters [10]" (p. 1).

> "The D2 seeding is slightly slower than uniform seeding, but it still leads to a faster algorithm
> since it helps the local search converge after fewer iterations." (p. 8). O custo da semeadura é
> compensado por menos iterações.

> "Indeed, on the Spam and Intrusion datasets, k-means++ achieved potentials 20 to 1000 times
> smaller than those achieved by standard k-means." (p. 8).

> "Also, experiments showed that k-means++ generally performed better if it selected several new
> centers during each iteration, and then greedily chose the one that decreased φ as much as
> possible." (p. 8). É uma variante gulosa, sem prova no artigo.

**Limitações ou ressalvas:**
- O próprio resumo chama os experimentos de "preliminares". Eles não informam o hardware, medem só
  o tempo total (não o número de iterações de Lloyd, que Bahmani et al. 2012 medem na Tabela 6) e
  comparam apenas com a semeadura uniforme.
- A garantia é em esperança, não com alta probabilidade, e vale para o potencial logo após a
  semeadura (p. 3).
- Por ser o manuscrito do autor, pode diferir em detalhes da versão do SODA. Ao citar, usar números
  de seção e de teorema além da página do PDF.
- Não trata de paralelismo. O custo de k passes sobre os dados para escolher k centros é apontado
  por Bahmani et al. (2012, p. 623) e não por este artigo.

### [KM2] Bahmani, Moseley, Vattani, Kumar & Vassilvitskii, 2012: k-means++ escalável (k-means‖)

**Arquivo:** `BAHMANI_2012_scalable_k_means_plusplus.pdf`

**Referência completa:**
Bahmani, B.; Moseley, B.; Vattani, A.; Kumar, R.; Vassilvitskii, S. Scalable k-means++. **Proceedings
of the VLDB Endowment**, v. 5, n. 7, p. 622–633, 2012. DOI: 10.14778/2180912.2180915.

**Chave BibTeX:** `bahmani2012scalable`

**Tipo:** Artigo de conferência (VLDB 2012), revisado por pares.

**Versão lida:** arXiv:1203.6402v1 [cs.DB], de 29/03/2012, com o aviso de copyright da VLDB
Endowment (Vol. 5, No. 7). A paginação impressa 622–633 aparece no texto (página do PDF + 621) e é a
citada abaixo.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Título, autores, volume, número, páginas
e DOI conferidos no Crossref (22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, desenvolvimento, experimentos e
conclusão; as demonstrações foram apenas percorridas). Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 622–633):
- Problema: o k-means++ tem custo O(nkd), igual ao de uma iteração de Lloyd, mas é "inerentemente
  sequencial". A probabilidade do i-ésimo centro depende dos i−1 anteriores, e uma implementação
  ingênua faz k passes sobre os dados (p. 623). Já as iterações de Lloyd são fáceis de paralelizar:
  cada ponto escolhe independentemente o centro mais próximo, e o novo centro é a média dos pontos
  atribuídos (p. 622).
- Algoritmo k-means‖ (Algoritmo 2, §3.3, p. 625): começa com um centro uniforme e calcula o custo
  inicial ψ. Em O(log ψ) rodadas, amostra cada ponto independentemente com probabilidade
  ℓ·d²(x,C)/φ_X(C) (ℓ = Ω(k), com ℓ pontos esperados por rodada) e os acrescenta a C. Ao final, pesa
  cada candidato pelo número de pontos que lhe são mais próximos e reagrupa os cerca de ℓ·log ψ
  candidatos ponderados em k centros com um algoritmo de aproximação (por exemplo, k-means++).
- Garantias: o Teorema 1 (p. 625) diz que, se o reagrupamento é uma α-aproximação, o k-means‖ é uma
  O(α)-aproximação; com k-means++ no passo 8, é O(log k). O Teorema 2 (p. 628) diz que, em cada
  rodada, E[φ_X(C∪C′)] ≤ 8φ* + ((1+α)/2)·φ_X(C), com α ≈ e^(−ℓ/2k), e o Corolário 3 leva a um custo
  de ordem O(φ*) após O(log ψ) rodadas. A análise usa dualidade de programação linear (Lemas 4–6,
  p. 630–631).
- Realização em MapReduce (§3.5, p. 625–626): cada *mapper* amostra de forma independente, e φ_X(C)
  é calculado somando, no *reducer*, os custos parciais φ_X′(C) das partições. Pressupõe-se que o
  conjunto C cabe na memória ou é distribuído a todos os *mappers* (p. 626).
- Experimentos (§4–5, p. 626–628). Os *datasets* são GaussMixture (n = 10.000, 15-D, centros de
  gaussiana esférica com variância R ∈ {1, 10, 100}), Spam (4.601 × 58) e KDDCup1999 (4,8 milhões ×
  42; amostra de 10% para parâmetros), com k ∈ {20, 50, 100} nos dois primeiros e {500, 1000} no
  KDD. A versão sequencial rodou numa estação com processadores quad-core de 2,5 GHz e 16 GB, e a
  paralela num cluster Hadoop de 1.968 nós (dois quad-core de 2,5 GHz e 16 GB cada) (p. 626). Os
  baselines são Random, k-means++ e Partition (Ailon et al.). As tabelas de custo trazem medianas de
  11 execuções.
- Resultados: o custo final do k-means‖ é comparável ao do k-means++ e muito menor que o do Random
  (Tabelas 1–2, p. 627; por exemplo, Spam com k = 50: 65–66 contra 68 do k-means++ e 1.488 do
  Random, em unidades de 10⁵). O tempo no KDD, em minutos, para k = 500 / 1000 (Tabela 4, p. 627) é
  300,0 / 489,4 no Random, 420,2 / 1.021,7 no Partition e, no k-means‖, 69,0 / 46,2 com ℓ = 0,5k,
  75,6 / 89,1 com ℓ = k, 69,8 / 86,7 com ℓ = 2k e 75,7 / 101,0 com ℓ = 10k. O k-means‖ escolhe só
  10–40% dos centros intermediários do Partition (Tabela 5, p. 628). As iterações de Lloyd até
  convergir, no Spam, em média de 10 execuções e para k = 20 / 50 / 100 (Tabela 6, p. 628), são
  176,4 / 166,8 / 60,4 no Random, 38,3 / 42,2 / 36,6 no k-means++ e 23,3 / 28,1 / 29,7 no k-means‖
  (ℓ = 2k, r = 5).
- Rodadas e superamostragem (§5.3, p. 628): com r·ℓ < k o resultado é bem pior que o do k-means++, e
  com r·ℓ ≥ k fica equivalente. O ponto ótimo experimental é r ≈ 8, e a superamostragem ajuda para
  r ≤ 8.

**Onde usar no trabalho:**
- Relatório §1 (Introdução) e §2 (Algoritmo 1): a observação de que a fase de atribuição/atualização
  é naturalmente paralelizável e de que o problema que sobra, a inicialização, é sequencial.
- Relatório §4 (Análise de Escalabilidade): contexto de como a literatura mede a escalabilidade do
  k-means em larga escala (tempo, passes, iterações até convergir).
- Relatório §2: se a inicialização adotada for k-means++ ou aleatória, este artigo registra o que
  cada uma custa em paralelo.

**Aplicabilidade ao nosso caso:** média, como contexto. O artigo trata de MapReduce em cluster, com
k grande (500–1000) e 4,8 milhões de pontos, enquanto o nosso cenário é memória compartilhada, uma
máquina e até 150.000 pontos. O valor está na observação sobre onde o paralelismo do k-means é fácil
e onde não é, e nas medidas de iterações até a convergência.

**Dado/afirmação específica a usar:**
> "Scaling k-means to massive data is relatively easy due to its simple iterative nature." (p. 622).

> "Given a set of cluster centers, each point can independently decide which center is closest to it
> and, given an assignment of points to clusters, computing the optimum center can be done by simply
> averaging the points." (p. 622). Justifica o paralelismo de dados do passo de Lloyd.

> "A naive implementation of k-means++ initialization will make k passes over the data in order to
> produce the initial centers." (p. 623).

> "the running time can be exponential in the worst case [32, 4] and even though the final solution
> is locally optimal, it can be very far away from the global optimum (even under repeated random
> initializations)." (p. 622). É o limite do k-means padrão, citado como estado da arte em 2012.

> "Note that we have tacitly assumed that the set C of centers is small enough to be held in memory
> or be distributed among all the mappers." (p. 626).

> "In the sequential setting we ran Random until convergence, while in the parallel version, we
> bounded the number of iterations to 20." (p. 626). É uma condição do experimento paralelo.

**Limitações ou ressalvas:**
- A escalabilidade com o número de máquinas é afirmada e não medida. O texto diz que o tempo do
  k-means‖ "melhora linearmente com o número de máquinas disponíveis" (p. 627), mas nenhum
  experimento varia o número de nós; os experimentos comparam algoritmos num cluster fixo.
- No experimento paralelo, o Random foi limitado a 20 iterações de Lloyd (p. 626), e o restante da
  comparação de tempo não declara limite equivalente. Os tempos da Tabela 4 podem não ser
  comparáveis termo a termo.
- O foco é a inicialização. O artigo declara que o esforço anterior em paralelizar o k-means "se
  concentrou nas fases pós-inicialização" (resumo, p. 622) e não propõe nada novo para elas.
- O PDF é a versão do arXiv com o rodapé da VLDB. A paginação 622–633 foi conferida no texto, mas as
  figuras 5.1–5.3 (p. 629–631) só trazem as curvas, sem os valores.
- Divergência com o `referencial_teorico.md` original: ele dizia que o k-means‖ "é a técnica de
  inicialização usada internamente pelo MLlib do Spark". A afirmação não está neste artigo, que cita
  só Hadoop e Mahout; ela foi verificada na ficha de Meng et al. (2016).

---

## Revisão de consistência dos slots KM1 e KM2

*(22/10/2026. Feita relendo as quatro fichas em sequência.)*

- A literatura mistura três versões do "k-means". MacQueen (1967) descreve um procedimento
  sequencial, uma observação por vez. MacQueen §3.6, atribuindo a Forgy e Jennrich, traz o laço de
  dois passos em lote. Lloyd (1957/1982) trata de quantização escalar, com as mesmas duas condições
  necessárias. Arthur & Vassilvitskii (2007) e Bahmani et al. (2012) chamam a segunda versão de
  "algoritmo" ou "iteração de Lloyd". O relatório deve dizer qual variante foi implementada e citar a
  fonte certa para ela.
- Os quatro artigos concordam que convergir não implica ótimo global: MacQueen (p. 282 e o exemplo
  da p. 288), Lloyd (p. 131 e Apêndice B, p. 136), Arthur & Vassilvitskii (p. 2) e Bahmani et al.
  (p. 622). Arthur & Vassilvitskii é o primeiro a quantificar isso para a semeadura (8(ln k + 2)).
- Sobre a terminação, MacQueen (p. 294) e Arthur & Vassilvitskii (p. 1, "no máximo kⁿ agrupamentos
  possíveis") dão terminação em número finito de passos, e Bahmani et al. (p. 622) lembram que o
  tempo pode ser exponencial no pior caso. Não há contradição, porque uma é afirmação de finitude e
  a outra é uma cota de tempo.
- Sobre a sensibilidade ao ponto de partida, MacQueen vê dependência da ordem dos dados (7%, p. 290)
  e da partição inicial no laço em lote (p. 294). Arthur & Vassilvitskii e Bahmani et al. tornam isso
  quantitativo, com a inicialização como o principal lugar onde se ganha qualidade e se reduzem
  iterações (Bahmani, Tabela 6).
- Nenhum dos quatro mede a escalabilidade do passo de Lloyd com o número de processadores. Isso fica
  para o slot KM3 (Dhillon & Modha 2000; Zhao et al. 2009; Ene et al. 2011). O resumo de Forgy (1965)
  citado por MacQueen não está no repositório.
- Lloyd e MacQueen não se substituem: um dá as condições de otimalidade (unidimensionais) e o outro
  o procedimento sobre amostras multivariadas.

---

## Slot KM3: Paralelização do k-means em memória distribuída e MapReduce

*(3 fichas, 22/10/2026: Dhillon & Modha 2000, Zhao et al. 2009 e Ene et al. 2011. Lidos por inteiro.)*

### [KM3] Dhillon & Modha, 2000: Um algoritmo de agrupamento de dados em multiprocessadores de memória distribuída

**Arquivo:** `DHILLON_2000_data_clustering_algorithm_distributed_memory_multiprocessors.pdf`

**Referência completa:**
Dhillon, I. S.; Modha, D. S. A data-clustering algorithm on distributed memory multiprocessors. In:
**Large-Scale Parallel Data Mining** (Lecture Notes in Computer Science, v. 1759, a conferir),
p. 245–260, Berlin: Springer, 2000. DOI: 10.1007/3-540-46502-2_13. Origem: workshop do KDD'99. O
Crossref data o capítulo de 2002 (publicação online), e adotamos 2000 (volume impresso).

**Chave BibTeX:** `dhillon2000data`

**Tipo:** Capítulo de livro (anais de workshop), revisado por pares.

**Versão lida:** manuscrito no formato LNCS (metadado do PDF: `pkmeans.dvi`), com 17 páginas e
numeração própria (texto de 1 a 15, depois as referências). A paginação do LNCS (245–260) não
aparece. As páginas citadas são as do manuscrito, iguais às do PDF.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Dados conferidos no Crossref e em busca
bibliográfica (22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–5, figuras e legendas;
referências percorridas). Citações literais conferidas com o texto do PDF, e os dados da Fig. 3
conferidos também nos traços vetoriais da figura.

**O que o artigo diz** (p. 1–15):
- Contribuição: uma versão paralela do k-means clássico em lote "direto", para máquinas de memória
  distribuída (*shared-nothing*), no modelo de troca de mensagens (SPMD com MPI) (p. 2–4). Os autores
  dizem não conhecer implementação paralela de k-means na literatura (p. 2).
- Algoritmo (Fig. 1 e 2, p. 5): o processo 0 escolhe os centróides iniciais e os difunde
  (`MPI_Bcast`). Cada um dos P processos fica com um bloco de n/P pontos, calcula as distâncias aos
  k centróides e acumula somas parciais por centróide (vetor de somas, contagens e erro quadrático).
  Essas somas são combinadas com `MPI_Allreduce` a cada iteração, e todos recalculam os centróides e
  decidem a parada de forma independente pelo MSE. Na implementação real, as 2k+1 reduções viram uma
  só, sobre um bloco contíguo de memória (p. 7).
- Custo (§3.2, p. 6–8): o custo sequencial por iteração é ≈ (3nkd + nk + nd + kd) flops, dominado por
  3nkd quando n é grande (T₁ ∼ 3nkd·I·T_flop). Em paralelo, T_P ∼ 3nkd·I·T_flop/P + d·k·I·T_reduce(P),
  com T_reduce(P) = O(log P). A comunicação é desprezível frente à computação se
  P·T_reduce(P)/(3·T_flop) ≪ n (condição (7), p. 8): quanto maior n, mais perto do speedup linear. A
  condição não depende de d nem de k.
- Definições (p. 9 e 11–12): *speedup* relativo é o tempo com 1 processador dividido pelo tempo com P
  para o mesmo conjunto. *Scaleup* relativo é o tempo por iteração com n pontos em 1 processador
  dividido pelo tempo por iteração com n·P pontos em P processadores (usa-se o tempo por iteração
  porque o número de iterações I varia com o conjunto).
- Experimentos (§4, p. 8–14). A máquina é um IBM SP2 com até 16 nós (Thin Node 2, POWER2 a 160 MHz,
  256 MB cada), com C e MPI. Os tempos ignoram a leitura do conjunto do disco, cada ponto é a média
  de 5 medidas e os centróides iniciais são idênticos para qualquer P (p. 8–9). Os dados são
  sintéticos, com 8 agrupamentos, gerados por um algoritmo de terceiros (p. 8).
  - Variando n (d = k = 8; P = 1, 2, 4, 8, 16): cinco conjuntos de 2¹³, 2¹⁵, 2¹⁷, 2¹⁹ e 2²¹ pontos,
    com I = 3, 10, 8, 164 e 50 iterações (legenda da Fig. 3, p. 10). O speedup em 16 processadores é
    15,62 para o maior conjunto e 6,22 para o menor (p. 9).
  - Variando d (2, 4, 8; n = 2²¹, k = 8): speedups de 15,42 a 15,53 em 16 processadores. Variando k
    (2, 4, 8, 16): 15,08 a 15,65 (p. 10). Ou seja, o speedup não depende de d nem de k.
  - Scaleup (p. 11–14): o tempo por iteração fica praticamente constante ao escalar n, k ou d
    proporcionalmente a P. O maior conjunto (n = 2²⁵ pontos, ≈ 2 GB) não cabe na memória de um nó
    (256 MB) e roda a ≈ 1,2 Gflops; com d = 128 (também ≈ 2 GB) chega a ≈ 1,8 Gflops. Ao escalar d, o
    tempo por iteração fica melhor que constante, o que os autores atribuem a uma conjectura sobre
    menor custo de laço (p. 14).
- Trabalho futuro (§5, p. 14–15): o algoritmo "é facilmente adaptado a multiprocessadores de memória
  compartilhada": dividir os n pontos em P blocos, calcular as distâncias de cada bloco em um
  processador e manter uma cópia separada dos centróides para cada um, o que "pode ser implementado
  com *threads*". Isso não foi implementado nem medido. Os autores propõem a mesma estratégia para EM.
- Inconsistência interna resolvida: o texto da p. 9 chama de "menor conjunto" o de 2¹¹ pontos (e fala
  em crescer de 2¹¹ a 2²¹), mas a legenda da Fig. 3, as curvas da própria figura e a frase "cinco
  conjuntos" da p. 9 listam 2¹³ a 2²¹. Adotamos 2¹³ como o menor conjunto, por ser o que foi
  observado. Nos traços da Fig. 3, os tempos com P = 1 diferem entre os cinco conjuntos como n·I (o
  segundo leva ≈ 13 vezes mais que o primeiro, razão de 2¹⁵·10 / 2¹³·3), o que não ocorreria com
  2¹¹, e as razões tempo(1)/tempo(16) lidas na figura reproduzem 15,6 e 6,2 (verificação nossa). O
  "2¹¹" do texto é erro de grafia.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means): a decomposição por dados (blocos de pontos, somas parciais por
  centróide, redução global a cada iteração) como modelo-base de paralelização do k-means.
- Relatório §4 (Análise de Escalabilidade): definições de *speedup* e *scaleup* e a condição de que o
  ganho melhora com n. Também como a literatura mede (tempo por iteração, centróides iniciais fixos,
  sem I/O).
- Relatório §1 (Introdução): motivação, com dados que não cabem na memória de uma máquina.

**Aplicabilidade ao nosso caso** (Java, `ExecutorService`/`ForkJoin`, uma máquina, até 150.000
pontos): alta como referência conceitual e metodológica, média como referência de resultado. O
modelo de execução do artigo é memória distribuída com MPI em máquinas de 1990, e a adaptação a
memória compartilhada é só descrita (p. 14–15). O intervalo de n do artigo (2¹³ a 2²¹, ou seja, de
8 mil a 2 milhões) contém o nosso (150.000 ≈ 2¹⁷), mas o texto dá só os extremos de speedup (6,22 e
15,62), e os valores intermediários estão apenas na Fig. 4. Descritivo; não é recomendação de
implementação.

**Dado/afirmação específica a usar:**
> "We analytically show that the speedup and the scaleup of our algorithm approach the optimal as the
> number of data points increases." (p. 1). É a tese central do artigo.

> "a simple, but effective, parallelization strategy is to divide the n data points into P blocks
> (each of size roughly n/P) and compute lines 14-21 for each of these blocks in parallel on a
> different processor." (p. 6). É a decomposição por dados.

> "each iteration of our parallel k-means algorithm consists of an asynchronous computation phase
> followed by a synchronous communication phase." (p. 7).

> "Relative speedup is defined as the ratio of the execution time for clustering a data set into k
> clusters on 1 processor to the execution time for identically clustering the same data set on P
> processors." (p. 9). É a definição de speedup relativo.

> "for a fixed data set, identical starting cluster centroids are used–irrespective of the number of
> processors used." (p. 9). É o controle experimental que impede o número de iterações de variar com P.

> "we observe a relative speedup of 15.62 on 16 processors." (p. 9).

> "that relative speedup flattens at 6.22 on 16 processors." (p. 9). É o speedup do menor conjunto.

> "Our algorithm is also easily adapted to shared memory multiprocessors where all processors have
> access to the same memory space." (p. 14).

> "Such an algorithm can be implemented on a shared memory machine using threads [42]." (p. 15).

**Limitações ou ressalvas:**
- O ambiente é de memória distribuída (SP2, rede de troca de alto desempenho), com dados sintéticos
  de 8 grupos e k = 8 na maioria dos testes e d ≤ 128. Os tempos não incluem I/O (p. 8) e os
  centróides iniciais são idênticos (p. 9), condições que facilitam o speedup.
- O número de iterações varia muito entre os conjuntos (de 3 a 164, Fig. 3), então os tempos totais
  entre tamanhos não são comparáveis. Por isso o artigo usa o tempo por iteração no *scaleup*.
- Os valores intermediários de speedup só existem como curva (Fig. 4–6), e o texto cita apenas os
  extremos.
- A adaptação a *threads* é uma proposta de uma frase, sem experimento. Não há, neste artigo,
  nenhum resultado de speedup em memória compartilhada.
- A afirmação de que não havia implementação paralela do k-means na literatura é dos autores, e este
  levantamento não a verifica.
- Concordância com o referencial original: a descrição "atribuição local + soma global dos
  centróides" se confirma, mas o mecanismo é `MPI_Allreduce` e não *map*/*reduce*.

### [KM3] Zhao, Ma & He, 2009: K-means paralelo baseado em MapReduce (PKMeans)

**Arquivo:** `ZHAO_2009_parallel_k_means_clustering_based_on_mapreduce.pdf`

**Referência completa:**
Zhao, W.; Ma, H.; He, Q. Parallel k-means clustering based on MapReduce. In: Jaatun, M. G.; Zhao, G.;
Rong, C. (eds.) **Cloud Computing (CloudCom 2009)**, LNCS 5931, p. 674–679, Springer, 2009.
DOI: 10.1007/978-3-642-10665-1_71.

**Chave BibTeX:** `zhao2009parallel`

**Tipo:** Artigo de conferência (LNCS), revisado por pares.

**Versão lida:** PDF da editora (6 páginas), com paginação impressa 674–679 conferida no cabeçalho.

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Dados conferidos no cabeçalho do PDF e
no Crossref (22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–4, pseudocódigos e
figura). Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 674–679):
- Contribuição: o PKMeans, que é o k-means de MacQueen [10] sobre MapReduce/Hadoop, em um único tipo
  de *job* (p. 675–677). Os autores justificam dizendo que os algoritmos paralelos anteriores (refs.
  1–3) supõem todos os objetos na memória e usam modelos de programação restritos (p. 674).
- Análise (§2.1, p. 675): o cálculo mais intenso é o das distâncias (nk por iteração). As distâncias
  de um objeto aos centros são independentes das dos outros e podem ser paralelizadas. Como os novos
  centros são necessários na iteração seguinte, "os procedimentos iterativos devem ser executados em
  série".
- Projeto (§2.2, p. 676–677): a função *map* atribui cada amostra ao centro mais próximo (entrada:
  *offset* e registro; variável global `centers`; saída: índice do centro e a amostra). A função
  *combine*, executada ao fim de cada tarefa *map*, soma parcialmente as amostras do mesmo centro e
  conta quantas são, para reduzir o tráfego de rede. A função *reduce* soma as parciais de todos os
  nós e divide pela contagem total para obter os novos centros (Algoritmos 1–3).
- Experimentos (§3, p. 677–678): cluster com computadores de dois núcleos de 2,8 GHz e 4 GB cada,
  Hadoop 0.17.0 e Java 1.5.0. Medem *speedup* (mesmo conjunto, 1 a 4 computadores, conjuntos de 1, 2,
  4 e 8 GB), *scaleup* (1, 2, 3 e 4 GB em 1, 2, 3 e 4 computadores) e *sizeup* (número de
  computadores fixo, conjunto crescente) (Fig. 1). A conclusão é que o PKMeans tem "muito bom"
  speedup, que melhora quando o conjunto cresce.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means): como o mesmo particionamento de pontos e a mesma agregação de
  somas parciais se expressam em *map*/*combine*/*reduce*, com o *combiner* como agregação local.
- Relatório §4 (Análise de Escalabilidade): uso das três métricas (*speedup*, *scaleup*, *sizeup*),
  com a ressalva de que aqui aparecem só como gráficos.

**Aplicabilidade ao nosso caso:** média. O que mais interessa é a estrutura: atribuição independente
por ponto, agregação local antes da global e novos centros calculados por redução. O cenário
experimental (Hadoop, HDFS, 1–4 computadores, 1–8 GB) é diferente de memória compartilhada em uma
máquina. Descritivo.

**Dado/afirmação específica a usar:**
> "In k-means algorithm, the most intensive calculation to occur is the calculation of distances."
> (p. 675).

> "distance computations between different objects with centers can be parallel executed." (p. 675).

> "Hence the iterative procedures must be executed serially." (p. 675). As iterações em si não são
> paralelizáveis.

> "The map function performs the procedure of assigning each sample to the closest center while the
> reduce function performs the procedure of updating the new centers." (p. 676).

> "In order to decrease the cost of network communication, a combiner function is developed to deal
> with partial combination of the intermediate values with the same key within the same map task."
> (p. 676).

> "To measure the speedup, we kept the dataset constant and increase the number of computers in the
> system." (p. 678).

**Limitações ou ressalvas:**
- O conjunto de dados não é descrito. O texto não informa número de pontos, dimensões, k,
  distribuição nem origem, só o tamanho em GB (1 a 8 GB) (p. 678). Os resultados de *speedup*,
  *scaleup* e *sizeup* aparecem só como curvas (Fig. 1), qualificados como "muito bom" e "escala
  muito bem", e nenhum valor numérico está no texto.
- São só 1 a 4 computadores, então o efeito de mais nós não é medido.
- Não descreve o laço externo (como cada iteração dispara um novo *job*, como os novos centros chegam
  à iteração seguinte, qual o critério de parada) nem a inicialização além de "k objetos aleatórios"
  (§2.1). O custo de iniciar *jobs* do Hadoop a cada iteração não é discutido.
- A frase "o conjunto é dividido e globalmente difundido a todos os *mappers*" (p. 676) é ambígua. Os
  pseudocódigos mostram que o que todos os *mappers* conhecem é o vetor `centers`, não o conjunto.
- Não cita Dhillon & Modha (2000), que já propunha a mesma decomposição em MPI. A crítica aos
  trabalhos anteriores (refs. 1–3, sobre agrupamento hierárquico) não trata de k-means paralelo.
- Concordância com o referencial original: a descrição *map* (atribuição) + *reduce* (novos centros)
  se confirma, mas o referencial omitia o *combiner*, que é parte central do projeto.

### [KM3] Ene, Im & Moseley, 2011: Agrupamento rápido com MapReduce

**Arquivo:** `ENE_2011_fast_clustering_mapreduce.pdf`

**Referência completa:**
Ene, A.; Im, S.; Moseley, B. Fast clustering using MapReduce. In: **Proceedings of the 17th ACM SIGKDD
International Conference on Knowledge Discovery and Data Mining (KDD '11)**, p. 681–689, 2011.
DOI: 10.1145/2020408.2020515.

**Chave BibTeX:** `ene2011fast`

**Tipo:** Artigo de conferência (KDD '11), revisado por pares. O PDF é o *preprint* arXiv.

**Versão lida:** arXiv:1109.1579v1 [cs.DC], de 7 set. 2011, com 20 páginas (a data "29 de outubro de
2018" na capa é a da compilação do PDF). A versão do KDD tem 9 páginas. As páginas citadas abaixo são
as do arXiv, e o conteúdo pode diferir do artigo publicado.

**Origem:** trazido por Adriano (commit `2f4599a`, 21/10/2026). Dados do KDD conferidos no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1, 2, 4 e 5, tabelas das
Fig. 1–2; as demonstrações da seção 3 foram apenas percorridas). Citações literais conferidas com o
texto do PDF.

**O que o artigo diz** (p. 1–20):
- Problema: agrupamento métrico k-center e k-median em MapReduce (p. 1–2). Não é um artigo sobre
  k-means. O k-means só aparece como algoritmo de Lloyd, usado como sub-rotina e como comparação, e
  numa frase da conclusão (p. 18). A entrada é um grafo completo com as Θ(n²) distâncias dadas
  explicitamente (p. 2).
- Algoritmo: o *Iterative-Sample* (p. 5–7) amostra pontos aleatoriamente, descarta os já "bem
  representados" pela amostra (comparando com um ponto-pivô) e repete até restar pouco. O resultado
  (a amostra mais o que sobrou) é agrupado numa só máquina. Vêm em seguida o *MapReduce-kCenter* e o
  *MapReduce-kMedian* (Alg. 4–5, p. 8); no k-median, cada ponto da amostra recebe um peso (o número
  de pontos que o escolheram como mais próximo). Não há menção a *coresets*: a técnica é amostragem.
- Garantias (Teoremas 1.1 e 1.2, p. 3): aproximação de fator constante, com alta probabilidade, em
  O(1/δ) rodadas de MapReduce e memória O(k²nᵟ) por máquina. Mostram que os problemas estão em MRC⁰
  (constante de rodadas, memória e máquinas sublineares, p. 3 e 5).
- Baselines (§4.1, p. 15–17): Lloyd paralelo (*Parallel-Lloyd*), busca local e um esquema de partição
  (*Divide*, de Guha et al.). O *Parallel-Lloyd* particiona os pontos uniformemente entre as
  máquinas e envia os k centros a cada uma, que atribui localmente os pontos e calcula médias e
  contagens por centro. Estas são agregadas para atualizar os centros. "A solução é a mesma da versão
  sequencial do Lloyd" (p. 16).
- Experimentos (§4.2–4.3, p. 17–18). Os dados são pontos aleatórios em R³ ao redor de k = 25 centros
  num cubo unitário (σ = 0,1; tamanhos de grupo por Zipf com α = 0), de 10.000 a 10 milhões de
  pontos. Todos os experimentos rodaram em uma única máquina (Intel Core i7 870 a 2,93 GHz, 8 GB).
  As 100 máquinas do MapReduce foram simuladas, somando por rodada o tempo da máquina mais lenta e
  ignorando o custo de comunicação. Cada configuração foi repetida 3 vezes (p. 17).
- Resultados observados nas tabelas (Fig. 1, p. 14, de 10 mil a 1 milhão de pontos; Fig. 2, p. 14, de
  2 a 10 milhões). Os tempos estão em segundos e o custo é relativo ao do *Parallel-Lloyd*:

  | Pontos | Parallel-Lloyd | Sampling-Lloyd | Divide-Lloyd | Sampling-LocalSearch | Custo Sampling-Lloyd |
  |---|---|---|---|---|---|
  | 1.000.000 | 205,7 | 4,0 | 2,7 | 11,0 | 1,132 |
  | 2.000.000 | 458,0 | 8,0 | 8,3 | 16,3 | 1,064 |
  | 5.000.000 | 1.333,3 | 18,3 | 24,7 | 29,3 | 1,106 |
  | 10.000.000 | 702,3 | 38,0 | 50,7 | 86,3 | 1,073 |

  Para 10 mil a 40 mil pontos, o *LocalSearch* leva 666,7 a 2.569,3 s, contra 1,7 a 3,0 s do
  *Sampling-LocalSearch*. O custo do *Sampling-Lloyd* relativo ao do *Parallel-Lloyd* varia de 1,051
  a 1,165 em todos os tamanhos da Fig. 1.
- Inconsistências entre texto e tabelas, resolvidas com as tabelas por serem os dados observados.
  (i) O texto resume o ganho sobre o *Parallel-Lloyd* como "cerca de 20×" (p. 3 e 17), mas as
  tabelas dão razões de ≈ 51× (1 milhão), ≈ 57× (2 milhões), ≈ 73× (5 milhões) e ≈ 18× (10
  milhões), cálculo nosso a partir dos tempos acima. O "20×" corresponde ao menor ganho, o de 10
  milhões. (ii) O texto fala em perda "pequena" ou "desprezível" de qualidade, mas as tabelas mostram
  custo de 5% a 16,5% maior que o do Lloyd paralelo (13,2% com 1 milhão de pontos e 7,3% com 10
  milhões). (iii) O tempo do *Parallel-Lloyd* com 10 milhões (702,3 s) é menor que com 5 milhões
  (1.333,3 s), sem explicação no texto. Como o número de iterações do Lloyd não é informado, não é
  possível atribuir a diferença.
- k-center: o método amostral piora o objetivo em até um fator 4 em alguns casos, "porque o objetivo
  do k-center é sensível à amostragem" (p. 3 e 14). Por isso a seção de experimentos trata só do
  k-median.
- k-means: os autores têm "evidência preliminar" de que a análise se estende ao k-means euclidiano
  (p. 18), sem teorema nem experimento.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 1: k-means): a descrição do Lloyd paralelo (partição fixa dos pontos, envio
  dos k centros, médias e contagens locais, agregação) e a nota de que o resultado é idêntico ao
  sequencial. Também o contraste entre paralelização exata e paralelização por amostragem.
- Relatório §4 (Análise de Escalabilidade): como medir tempo em simulação e o que isso deixa de fora
  (a comunicação), e a prática de reportar tempo e custo normalizado.

**Aplicabilidade ao nosso caso:** só como contexto. O objeto do artigo é k-median/k-center, em
MapReduce simulado, com garantia de aproximação e entrada de n² distâncias. O k-means sobre pontos
euclidianos em R^d é tratado apenas de passagem. A descrição do Lloyd paralelo (p. 15–16) é, entre
as três fichas do slot, a única em que o autor afirma explicitamente a equivalência com o
sequencial.

**Dado/afirmação específica a usar:**
> "We focus on the practical and popular clustering problems, k-center and k-median." (p. 1). Corrige
> o escopo atribuído ao artigo.

> "Our algorithms use sampling to decrease the data size and they run a time consuming clustering
> algorithm such as local search or Lloyd's algorithm on the resulting data set." (p. 1).

> "It is important to note that the solution computed by the algorithm is the same as the sequential
> version of Lloyd's algorithm." (p. 16). É a equivalência do Lloyd paralelo com o sequencial.

> "All experiments were performed on a single machine. When running MapReduce algorithms, we
> simulated each machine used by the algorithm." (p. 17).

> "In these experiments, the communication cost was ignored." (p. 17).

> "A careful reader may note that Lloyd's algorithm is generally used for the k-means objective and
> not for k-median." (p. 15). O próprio artigo admite o desvio de objetivo.

> "We note that we have preliminary evidence that the analysis used for the k-median problem can be
> extended to the k-means problem in Euclidean space" (p. 18).

**Limitações ou ressalvas:**
- Não é k-means. O objetivo é k-median/k-center, métrico, com as n² distâncias na entrada (p. 2).
- Os tempos vêm de simulação numa máquina, sem comunicação (p. 17). Não medem escalabilidade real, e
  os "100 nós" são um parâmetro do modelo.
- Os dados sintéticos são só em R³, com k = 25. As variações de α, k e σ foram "omitidas" por espaço
  (p. 18).
- É um *preprint* estendido. A versão do KDD tem 9 páginas e pode não conter as mesmas tabelas.
- Divergências com o referencial original: (i) a palavra "*coresets*" não aparece no texto; (ii) o
  artigo trata de k-median e k-center, e não de "k-means/k-median/k-center"; (iii) o referencial o
  apresentava como contraponto às estratégias de Dhillon & Modha e de Zhao et al., mas o artigo não
  cita nenhum dos dois. O Lloyd paralelo vem das refs. [28, 7, 1] do próprio artigo (Lloyd, um blog e
  uma página do Google sobre MapReduce).

---

## Slot KM4: Escala prática, mini-lote e plataforma

*(2 fichas, 22/10/2026: Sculley 2010 e Meng et al. 2016. Lidos por inteiro.)*

### [KM4] Sculley, 2010: Agrupamento k-means em escala web (mini-lote)

**Arquivo:** `SCULLEY_2010_webscale_kmeans_clustering.pdf`

**Referência completa:**
Sculley, D. Web-scale k-means clustering. In: **Proceedings of the 19th International Conference on
World Wide Web (WWW '10)**, p. 1177–1178, Raleigh, NC, 2010. DOI: 10.1145/1772690.1772862.

**Chave BibTeX:** `sculley2010webscale`

**Tipo:** Artigo curto (*poster*), 2 páginas, revisado por pares.

**Versão lida:** PDF do pôster, com paginação impressa 1177–1178 conferida.

**Origem:** trazido por Adriano (commit `2f4599a`, 21/10/2026). Dados conferidos no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (as duas páginas). Citações literais
conferidas com o texto do PDF.

**O que o artigo diz** (p. 1177–1178):
- Problema: o k-means em lote é caro para conjuntos grandes (O(kns), com s o máximo de não-zeros por
  exemplo). A variante *online* de Bottou & Bengio (gradiente descendente estocástico, um exemplo por
  vez) converge rápido, mas encontra soluções piores por causa do ruído estocástico (p. 1177).
- Proposta 1, k-means em mini-lotes (Algoritmo 1, p. 1177): a cada uma de t iterações, sorteia b
  exemplos, guarda o centro mais próximo de cada um e depois atualiza cada centro com taxa de
  aprendizado por centro η = 1/v[c] (v[c] é a contagem acumulada de exemplos do centro). Os centros
  iniciais são k exemplos sorteados. A convergência "segue de perto" o resultado de Bottou & Bengio
  (p. 1177), sem prova no pôster.
- Experimentos (p. 1177–1178): coleção RCV1, com 781.265 exemplos de treino e 23.149 de teste.
  Todos os métodos usam os mesmos centros iniciais aleatórios. O erro é a fração acima do melhor
  valor do objetivo encontrado pelo k-means em lote rodado até convergir, avaliado no conjunto de
  teste. O tamanho b = 1000 foi "escolhido em testes iniciais, com resultados robustos para uma
  faixa de b", e k = 3, 10 e 50 (Fig. 1). O mini-lote chegou a um valor quase ótimo várias ordens
  de grandeza mais rápido que o lote e com soluções significativamente melhores que o SGD. Testes
  omitidos por espaço indicam ser "várias vezes mais rápido" que o k-means em lote com desigualdade
  triangular. Para k pequeno, produziu centros quase ótimos para quase um milhão de documentos em uma
  fração de segundo de CPU numa máquina comum de 2,4 GHz.
- Proposta 2, centros esparsos (§3, p. 1178): projeção na bola L1 após cada mini-lote, com um
  algoritmo de bisseção (ε-L1, Alg. 2) comparado ao de Duchi et al. (LTL1P). Com k = 10, b = 1000 e
  t = 16, o lote tem 200.319 não-zeros e 133,96 s de CPU. Com λ = 5,0, o LTL1P tem 46.446 não-zeros
  e 0,51 s (objetivo de teste +0,4%), e o ε-L1 tem 44.060 e 0,27 s (+0,7%). Com λ = 1,0, o LTL1P tem
  3.181 e 0,48 s (+1,8%), e o ε-L1 tem 2.547 e 0,19 s (+2,8%).

**Onde usar no trabalho:**
- Relatório §2 e Discussão (Algoritmo 1: k-means): alternativa que troca qualidade por custo por
  iteração, que ajuda a situar o k-means "clássico" (lote completo) escolhido para paralelizar.
- Relatório §4 (Análise de Escalabilidade): o trade-off entre tempo e qualidade quando o conjunto
  cresce.

**Aplicabilidade ao nosso caso:** só como contexto. O pôster é sequencial, para dados esparsos de
texto (RCV1) e altas dimensões, e modifica o algoritmo (estocástico), então não reproduz o
resultado do k-means em lote. Descritivo.

**Dado/afirmação específica a usar:**
> "This reduces computation cost by orders of magnitude compared to the classic batch algorithm while
> yielding significantly better solutions than online stochastic gradient descent." (p. 1177).

> "The classic batch k-means algorithm is expensive for large data sets, requiring O(kns) computation
> time where n is the number of examples and s is the maximum number of non-zero elements in any
> example vector." (p. 1177). É o custo por iteração do lote.

> "While SGD converges quickly on large data sets, it finds lower quality solutions than the batch
> algorithm due to stochastic noise [1]." (p. 1177).

> "The mini-batch method converged to a near optimal value several orders of magnitude faster than the
> full batch method, and also achieved significantly better solutions than SGD." (p. 1178).

> "We set the mini-batch b to 1000 based on separate initial tests; results were robust for a range of
> b." (p. 1178).

**Limitações ou ressalvas:**
- É um pôster de duas páginas, com um único conjunto (RCV1, texto esparso) e três valores de k. Os
  resultados são curvas (Fig. 1), sem tabela de tempos do mini-lote, e os testes contra o lote com
  desigualdade triangular foram omitidos.
- O algoritmo é estocástico, então o resultado é "quase ótimo" e não idêntico ao do lote. O pôster
  não quantifica a perda além das curvas de erro (na escala de ≈ 0,03 a 0,1 do objetivo, conforme k).
- A execução é sequencial numa máquina de 2,4 GHz, sem análise de paralelismo.
- A segunda contribuição (esparsidade por projeção L1) não é relevante para dados densos de baixa
  dimensão. O referencial original não a mencionava.
- Concordância com o referencial original: a descrição do mini-lote e da troca de tempo por
  qualidade se confirma.

### [KM4] Meng et al., 2016: MLlib, aprendizado de máquina no Apache Spark

**Arquivo:** `MENG_2016_mllib_machine_learning_in_apache_spark.pdf`

**Referência completa:**
Meng, X.; Bradley, J.; Yavuz, B.; Sparks, E.; Venkataraman, S.; Liu, D.; Freeman, J.; Tsai, D. B.;
Amde, M.; Owen, S.; Xin, D.; Xin, R.; Franklin, M. J.; Zadeh, R.; Zaharia, M.; Talwalkar, A. MLlib:
machine learning in Apache Spark. **Journal of Machine Learning Research**, v. 17, n. 34 (a conferir),
p. 1–7, 2016.

**Chave BibTeX:** `meng2016mllib`

**Tipo:** Artigo curto de descrição de software (*software paper*), revisado por pares.

**Versão lida:** PDF da JMLR ("Submitted 5/15; Published 4/16"), com 7 páginas e paginação impressa
1–7 conferida (página do PDF = página impressa).

**Origem:** trazido por Herik (commit `a301449`, 21/10/2026). Dados do cabeçalho conferidos.

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (7 páginas, incluindo a Fig. 2
renderizada). Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 1–7):
- O que é: MLlib é a biblioteca de aprendizado de máquina distribuída do Apache Spark. Cobre
  classificação, regressão, filtragem colaborativa, agrupamento e redução de dimensionalidade
  (p. 2). É escrita em Scala, com bibliotecas de álgebra linear nativas, e tem API em Java, Scala e
  Python (p. 2).
- Histórico e comunidade (§2, p. 3): desenvolvimento desde 2012 e código aberto desde set. de 2013.
  Foi de 11 contribuidores iniciais a mais de 140 no Spark 1.4.
- Funcionalidades (§3, p. 3–4): o k-means aparece uma única vez, numa lista de métodos ("k-means
  clustering and principal component analysis for clustering and dimensionality reduction"). As
  otimizações citadas são de outros algoritmos (ALS com blocos, árvores de decisão, modelos
  lineares). Em geral, a agregação em árvore evita que o *driver* seja gargalo, e o *broadcast* do
  Spark distribui rapidamente modelos grandes aos trabalhadores (p. 4).
- Desempenho e escala (§4, p. 5): o *benchmark* de ALS usa cópias ampliadas do conjunto de
  avaliações da Amazon, 5 iterações e um cluster EC2 de 16 nós (m3.2xlarge), com MLlib 1.1 e 1.4
  contra o Mahout 0.9 (Hadoop MapReduce) (Fig. 2a). Quanto ao ganho entre versões, nos dados
  sintéticos do `spark-perf` com 16 trabalhadores, a Fig. 2(b) mostra o ganho de MLlib 1.0 para 1.1
  por algoritmo: regressão logística 5,6×, *ridge* 1,2×, árvores de decisão 3,2×, ALS 2,8× e
  KMeans 2,5×. O texto resume "3× em média".
- O que não está no artigo: a descrição do algoritmo de k-means do MLlib (inicialização,
  paralelização, tratamento dos centróides), o tamanho e o número de dimensões dos dados do KMeans,
  o valor de k e qualquer medida de escalabilidade do k-means. A palavra "centroid" não aparece.

**Onde usar no trabalho:**
- Relatório §1 (Introdução) e §2: contexto de que bibliotecas de plataforma (Spark/MLlib) incluem
  k-means distribuído como método padrão. O artigo não serve como fonte do algoritmo.
- Relatório §4: só como referência de que o MLlib é o contexto de "k-means em Spark".

**Aplicabilidade ao nosso caso:** só contexto, e baixa. É um artigo de apresentação de biblioteca,
com 7 páginas, que não descreve o k-means nem sua paralelização.

**Dado/afirmação específica a usar:**
> "k-means clustering and principal component analysis for clustering and dimensionality reduction"
> (p. 3). É a única menção ao k-means no artigo.

> "Many algorithms benefit from efficient communication primitives; in particular tree-structured
> aggregation prevents the driver from being a bottleneck, and Spark broadcast quickly distributes
> large models to workers." (p. 4). É uma afirmação geral, que não nomeia o k-means.

> "The library targets large-scale learning settings that benefit from data-parallelism or
> model-parallelism to store and operate on data or models." (p. 2).

> "show a 3× speedup on average across all algorithms" (p. 5). É o ganho entre versões da
> biblioteca, não um speedup por número de nós.

**Limitações ou ressalvas:**
- É um artigo de apresentação de software e não traz nenhum detalhe do k-means. A Fig. 2(b) mostra o
  k-means só como uma barra (2,5×) de ganho entre versões do MLlib.
- O "speedup" do artigo é entre versões (1.0 para 1.1) e não entre números de nós. O *benchmark* de
  escala (Fig. 2a) é de ALS e não de k-means.
- Divergências com o referencial original, corrigidas aqui. (i) O referencial dizia que o artigo
  "descreve a implementação distribuída de k-means sobre RDDs (particionamento dos dados,
  *broadcast* dos centróides a cada iteração, agregação distribuída)", mas nada disso está no
  artigo. O *broadcast* citado é uma afirmação geral sobre modelos (p. 4). (ii) O referencial dizia,
  na entrada de Bahmani et al., que o k-means‖ é a inicialização usada pelo MLlib, mas o artigo não
  menciona o k-means‖ nem a inicialização do k-means.

---

## Revisão de consistência dos slots KM3 e KM4

*(22/10/2026. Feita relendo as cinco fichas em sequência.)*

- Dhillon & Modha (`MPI_Allreduce`), Zhao et al. (*map* + *combine* + *reduce*) e o *Parallel-Lloyd*
  de Ene et al. descrevem a mesma decomposição com três vocabulários: blocos fixos de pontos por
  trabalhador, centros conhecidos por todos, atribuição local ao centro mais próximo com somas e
  contagens parciais por centro, combinação global e novo centro igual a soma sobre contagem. Ene et
  al. afirmam que o resultado coincide com o do Lloyd sequencial (p. 16), e Dhillon & Modha fixam os
  centróides iniciais para isolar o efeito do paralelismo (p. 9). Não há contradição entre eles.
- Só Dhillon & Modha reportam *speedup* e *scaleup* numéricos por número de processadores (6,22 e
  15,62 em 16 processadores, conforme n) e a condição analítica de que o ganho melhora com n. Zhao
  et al. mostram curvas de 1 a 4 computadores sem valores no texto, e o conjunto de dados nem é
  descrito. Ene et al. medem em simulação numa única máquina e sem comunicação. Bahmani et al. (KM2)
  afirmam escala linear com o número de máquinas sem variar o número de nós. Assim, o único
  resultado quantitativo de *speedup* do k-means em paralelo nos artigos lidos é o de Dhillon &
  Modha, em MPI.
- Dhillon & Modha dizem que a adaptação a memória compartilhada é direta e usa *threads* (p. 14–15),
  sem testá-la, e nenhum outro artigo do slot mede *speedup* por *threads* de k-means. Essa é a
  lacuna mais relevante para o nosso cenário (uma máquina, `ExecutorService`/`ForkJoin`).
- Há duas famílias: a paralelização exata (Dhillon & Modha; Zhao et al.; *Parallel-Lloyd*) e a
  aproximada (Ene et al., por amostragem; Sculley, por mini-lotes). Nas tabelas de Ene et al., o
  *Sampling-Lloyd* custa 5% a 16,5% mais que o Lloyd paralelo; em Sculley, a perda é "quase ótima",
  só em curva. Ambos trocam qualidade por tempo, e o ganho depende do tamanho do conjunto.
- Inconsistências internas resolvidas, com o critério de que vale o dado observado em figura ou
  tabela. Em Dhillon & Modha, o menor conjunto é 2¹³ (não o 2¹¹ do texto). Em Ene et al., os ganhos
  sobre o Lloyd paralelo são de 18× a 73× nas tabelas (não "cerca de 20×") e a perda de qualidade é
  de 5% a 16,5% (não "desprezível"). O tempo do *Parallel-Lloyd* não é monótono em n (702,3 s com 10
  milhões, 1.333,3 s com 5 milhões), sem explicação.
- Ene et al. e Meng et al. são periféricos ao k-means: o primeiro é sobre k-median/k-center, e o
  segundo é uma descrição de biblioteca. A evidência central do slot é Dhillon & Modha (modelo e
  métricas), com Zhao et al. como a tradução para MapReduce.

---

# 2ª etapa: DBSCAN

## Slot DB1: Formulação original do DBSCAN e complexidade

*(2 fichas, 22/10/2026: Ester et al. 1996 e Schubert et al. 2017. Lidos por inteiro.)*

### [DB1] Ester, Kriegel, Sander & Xu, 1996: Um algoritmo baseado em densidade para descobrir agrupamentos em grandes bancos de dados espaciais com ruído

**Arquivo:** `ESTER_1996_density_based_algorithm_discovering_clusters_noise.pdf`

**Referência completa:**
Ester, M.; Kriegel, H.-P.; Sander, J.; Xu, X. A density-based algorithm for discovering clusters in
large spatial databases with noise. In: **Proceedings of the Second International Conference on
Knowledge Discovery and Data Mining (KDD-96)**, p. 226–231, Portland, OR: AAAI Press, 1996. Sem DOI.

**Chave BibTeX:** `ester1996density`

**Tipo:** Artigo de conferência (clássico de referência), revisado por pares.

**Versão lida:** PDF dos anais do KDD-96 (6 páginas; direitos da AAAI), com paginação impressa
226–231 conferida no cabeçalho (página do PDF + 225). A digitalização tem ruído de OCR em figuras e
símbolos.

**Origem:** trazido por Julia (commit `a12394d`, 21/10/2026). Autores, título e veículo conferidos no
próprio PDF e nas referências de Schubert et al. (2017).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–6, algoritmo, tabela 1).
Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 226–231):
- Problema e requisitos (p. 226): agrupar grandes bancos espaciais exigindo (1) pouco conhecimento de
  domínio para os parâmetros, (2) agrupamentos de forma arbitrária e (3) eficiência. Critica os
  particionais (k-means, k-medoides, CLARANS: formas convexas, k conhecido; p. 226–227) e os
  hierárquicos (parâmetro de parada difícil, p. 227).
- Modelo de agrupamento (§3, p. 227–228): vizinhança-Eps de um ponto; ponto núcleo (vizinhança com
  pelo menos MinPts pontos), ponto de borda e ruído; as relações *diretamente alcançável por
  densidade* (assimétrica quando há ponto de borda), *alcançável por densidade* (transitiva) e
  *conectado por densidade* (simétrica). Um agrupamento é um conjunto maximal quanto à
  alcançabilidade e conectado por densidade (Def. 5), e ruído é o que não pertence a nenhum
  agrupamento (Def. 6). O Lema 1 diz que o conjunto alcançável a partir de um núcleo é um
  agrupamento, e o Lema 2 que o agrupamento fica unicamente determinado por qualquer um de seus
  pontos núcleo (p. 228).
- Algoritmo (§4.1, p. 229): varre os pontos. Para um ponto ainda não classificado, o `ExpandCluster`
  consulta sua vizinhança (*region query*). Se ela tem menos de MinPts pontos, marca o ponto como
  ruído (que pode ser reclassificado depois como borda). Se não, expande o agrupamento com uma lista
  de sementes, com uma *region query* por ponto núcleo encontrado. Eps e MinPts são globais para
  todos os grupos, por isso dois grupos próximos e de densidades diferentes podem ser fundidos
  (p. 229).
- Complexidade declarada (p. 229): a *region query* pode usar uma R*-tree. O artigo supõe que
  consultas pequenas custam O(log n) em média e conclui que a complexidade média do DBSCAN é
  O(n · log n), com no máximo uma consulta por ponto. Essa conclusão é informal (ver a ficha de
  Schubert et al. 2017).
- Pontos de borda e ordem (p. 229–230): um ponto de borda alcançável de dois grupos é atribuído ao que
  o descobrir primeiro. Fora essas "situações raras", o resultado não depende da ordem de visita.
- Parâmetros (§4.2, p. 230): heurística do gráfico de *k*-dist ordenado (distância ao *k*-ésimo
  vizinho mais próximo), com o "primeiro vale" como limiar para Eps. MinPts é fixado em 4 para dados
  bidimensionais, porque os gráficos para k > 4 "não diferem significativamente".
- Experimentos (§5, p. 230–231): C++ sobre uma R*-tree, em estações HP 735/100. Três bancos sintéticos
  (formas esféricas, não convexas e com ruído) foram avaliados por inspeção visual, contra o CLARANS
  (com k = 4 dado a ele). A eficiência foi medida no benchmark SEQUOIA 2000 (62.584 nomes de locais
  da Califórnia), com subconjuntos de 1.252 a 12.512 pontos (Tabela 1, em segundos):

  | Pontos | 1.252 | 2.503 | 3.910 | 5.213 | 6.256 | 7.820 | 8.937 | 10.426 | 12.512 |
  |---|---|---|---|---|---|---|---|---|---|
  | DBSCAN | 3,1 | 6,7 | 11,3 | 16,0 | 17,8 | 24,5 | 28,2 | 32,7 | 41,7 |
  | CLARANS | 758 | 3.026 | 6.845 | 11.745 | 18.029 | 29.826 | 39.265 | 60.540 | 80.638 |

  O tempo do DBSCAN é "ligeiramente maior que linear" e o do CLARANS "próximo de quadrático"
  (p. 231).
- Inconsistência interna resolvida (resumo contra tabela): o resumo diz que o DBSCAN supera o CLARANS
  "por um fator de mais de 100", e o texto da p. 231 diz "entre 250 e 1900". Adotamos a faixa que
  vem da Tabela 1: razões de ≈ 245× (1.252 pontos) a ≈ 1.934× (12.512 pontos), cálculo nosso,
  crescentes com n. O "250" do texto arredonda o mínimo para cima, e o "mais de 100" do resumo é um
  limite inferior frouxo.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): definições (vizinhança-Eps, núcleo, borda, ruído,
  alcançabilidade), o algoritmo de expansão de agrupamento e o papel da consulta de vizinhança.
- Relatório §3 (Setup Experimental): escolha de MinPts e de ε pela heurística do gráfico *k*-dist.
- Relatório §1 (Introdução): origem do método.

**Aplicabilidade ao nosso caso:** alta como referência de origem e de definições, nenhuma em
paralelismo (o algoritmo é sequencial e a única escala testada é de 12.512 pontos). A afirmação
O(n · log n) deve ser citada com a ressalva de Schubert et al. (2017). Descritivo.

**Dado/afirmação específica a usar:**
> "DBSCAN requires only one input parameter and supports the user in determining an appropriate value
> for it." (p. 226). É a promessa de poucos parâmetros (na prática, Eps e MinPts, este fixado em 4).

> "a cluster is defined to be a set of density-connected points which is maximal wrt.
> density-reachability." (p. 228).

> "Region queries can be supported efficiently by spatial access methods such as R*-trees" (p. 229).

> "Thus, the average run time complexity of DBSCAN is O(n * log n)." (p. 229). Schubert et al. (2017)
> qualificam a afirmação como informal e sem garantia.

> "In this case, point p will be assigned to the cluster discovered first." (p. 230). Vale para pontos
> de borda compartilhados.

> "the result of DBSCAN is independent of the order in which the points of the database are visited due
> to Lemma 2" (p. 230). Com a exceção dos pontos de borda compartilhados.

> "we eliminate the parameter MinPts by setting it to 4 for all databases (for 2-dimensional data)."
> (p. 230).

> "the run time of DBSCAN is slightly higher than linear in the number of points." (p. 231).

**Limitações ou ressalvas:**
- A complexidade O(n · log n) é derivada informalmente de uma suposição sobre R*-trees (ver Schubert
  et al., 19:5–19:7) e só vale em média, para consultas "pequenas".
- A avaliação de qualidade é só visual e contra um único concorrente (CLARANS). A escala máxima é de
  12.512 pontos, em estações de 1996. Os próprios autores deixam a comparação com métodos de
  densidade clássicos para trabalho futuro (p. 230).
- A fusão de grupos próximos de densidades diferentes é uma limitação reconhecida (p. 229).
- O referencial original concorda quanto às definições e ao algoritmo, mas não mencionava que o
  O(n · log n) do artigo é uma derivação informal (ver a ficha de Schubert et al.).

### [DB1] Schubert, Sander, Ester, Kriegel & Xu, 2017: DBSCAN revisitado, revisitado, por que e como você deve (ainda) usar o DBSCAN

**Arquivo:** `SCHUBERT_2017_dbscan_revisited_revisited.pdf`

**Referência completa:**
Schubert, E.; Sander, J.; Ester, M.; Kriegel, H.-P.; Xu, X. DBSCAN revisited, revisited: why and how you
should (still) use DBSCAN. **ACM Transactions on Database Systems**, v. 42, n. 3, art. 19, p. 19:1–19:21,
jul. 2017. DOI: 10.1145/3068335.

**Chave BibTeX:** `schubert2017dbscan`

**Tipo:** Correspondência técnica (*technical correspondence*), revisada por pares, com experimentos
novos.

**Versão lida:** PDF da ACM (21 páginas), com paginação impressa "19:N" conferida (N = página do PDF).

**Origem:** trazido por Adriano (commit `2f4599a`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–5, figuras e legendas;
lista de referências percorrida). Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (19:1–19:19):
- Contexto (19:1–19:2): é uma resposta ao artigo do SIGMOD 2015 de Gan & Tao ("DBSCAN revisited:
  mis-claim, un-fixability, and approximation"), que mostrou um limite inferior para o DBSCAN
  euclidiano e propôs um algoritmo aproximado em grade (ρ-aproximado). Os autores aceitam as
  contribuições centrais desse artigo e criticam quatro afirmações imprecisas (19:5–19:7).
- Modelo e algoritmo (§2, 19:2–19:5): o DBSCAN tem duas contribuições, o modelo de agrupamento por
  densidade e o algoritmo orientado a bancos de dados. O artigo traz o pseudocódigo do algoritmo
  sequencial original (Alg. 1, 19:4) e uma versão abstrata em três passos: calcular vizinhanças e
  núcleos, unir núcleos vizinhos em agrupamentos, e atribuir pontos de borda a um núcleo vizinho ou
  a ruído (Alg. 2, 19:5).
- Complexidade (19:3): executa-se exatamente uma consulta de vizinhança por ponto, então o custo é
  O(C + n·Q + Σ rᵢ), com C o custo de construir o índice, Q o custo de uma consulta e rᵢ o tamanho
  do resultado da i-ésima. Com varredura linear, Q ∈ Θ(n·D) e o custo total é Θ(n²·D), com D o
  custo de uma distância. Em espaço euclidiano de dimensão d, é Θ(n²·d).
- Determinismo (19:4): o resultado é determinístico, mas pode mudar se o conjunto for permutado. Os
  rótulos dos grupos mudam com a ordem, e os pontos de borda alcançáveis de mais de um grupo vão
  para o primeiro. Em casos raros, isso pode deixar um grupo com menos de minPts pontos (nota 1).
  Todo grupo tem ao menos um ponto núcleo.
- O que a crítica estabelece sobre O(n log n) (19:5–19:9): o artigo original (Ester et al.) chegou
  "de forma frouxa" a O(n log n) supondo consultas O(log n) na R*-tree, o que "não é uma análise
  formal" e "é formalmente incorreto" como garantia. Mas o O(n²) no pior caso já era conhecido
  (Sander et al.; Gunawan), e o artigo do SIGMOD não o "descobriu". O limite inferior de Gan & Tao,
  Ω(n^(4/3)) para d ≥ 3 em espaço euclidiano, vale para todo algoritmo exato. Os autores mostram
  (Teoremas 3.1 e 3.2) que ele implica que não existe índice universal com consulta O(log n) e
  construção O(n log n) para dados arbitrários. Ainda assim, "intratável na prática" é exagero,
  porque Θ(n²) pode ser útil e muitos concorrentes são Θ(n²) ou Θ(n³).
- Limitações do método em grade (19:10–19:11): o número de células cresce exponencialmente com a
  dimensão, e os resultados valem só para distância euclidiana e dados pontuais, enquanto o DBSCAN
  com índices (R*-tree, k-d tree, cover tree) aceita outras distâncias.
- Recomendações de parâmetros (§4.1–4.2, 19:11–19:13): minPts = 4 para dados bidimensionais ou
  "2·dim" (Sander et al.), e ε tão pequeno quanto possível, escolhido pelo gráfico *k*-dist. Os
  "sinais de alerta" para resultados degenerados são ruído entre 1% e 30% e o maior componente com no
  máximo 20% a 50% dos pontos agrupados.
- Novos experimentos (§4.4, 19:14–19:18): *hardware* Debian com Intel i7-3770 a 3,4 GHz e 32 GB. Os
  conjuntos são Household (UCI, normalizado para inteiros em [0; 10⁵], 5% de duplicatas, 7
  atributos), Farm e PAMAP2, fornecidos por Gan & Tao junto com o binário C++ deles. Comparam as
  implementações de Gan & Tao (incluindo GriDBSCAN e R-tree), o ELKI 0.7.0 (Java; R*-tree, k-d tree,
  cover tree) e um C++ próprio com cover tree e ANN (k-d tree). Gan & Tao usaram ε ≥ 5.000, faixa em
  que mais de 99% do Household forma um único grupo. Para ε < 500, todos os índices dão speedup de
  10× a 1.000× sobre a varredura linear, e até ε = 2.000 o método ρ-aproximado é superado pelo
  GriDBSCAN e pela R-tree do próprio Gan & Tao.
- Conclusão (19:18–19:19): o DBSCAN com índices eficazes e ε razoável "é competitivo". O ρ-DBSCAN tem
  a vantagem teórica de garantia de aproximação, mas a vantagem prática para ε pequenos "ainda não
  foi demonstrada de forma convincente".

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): a formulação do custo (n consultas de vizinhança) e a
  caracterização correta da complexidade (depende do custo da consulta; Θ(n²) sem índice; sem
  garantia O(n log n)).
- Relatório §3 (Setup Experimental): escolha de ε e minPts (heurísticas e sinais de alerta) e a
  crítica à escolha de parâmetros em benchmarks.
- Relatório §4 (Análise de Escalabilidade): por que o tempo do DBSCAN depende do índice e de ε, e não
  só de n.

**Aplicabilidade ao nosso caso:** alta, como referência de complexidade e de parâmetros. É sequencial
e não trata de paralelismo. Descritivo; não é recomendação de implementação.

**Dado/afirmação específica a usar:**
> "Thus, we execute exactly one neighborhood query for every point in the database." (19:3). É a base
> da análise de custo.

> "The result of this DBSCAN algorithm is deterministic, but may change if the dataset is permuted."
> (19:4).

> "The original DBSCAN algorithm simply assigns border points to the first cluster they are reachable
> from" (19:4).

> "These statements about the runtime of DBSCAN in the original paper are clearly not a formal complexity
> analysis, but are informal and are not part of the main contribution of the paper." (19:6).

> "This runtime complexity is formally incorrect since there is no theoretical guarantee for the assumed
> runtime complexity of range queries, and it should not be repeated in this form." (19:7). Ou seja, não
> repetir "O(n log n)" como garantia.

> "there exists no universal index which can guarantee O(log n) time for nearest-neighbor search or
> ε-range neighbor search for arbitrary data and which can be constructed in O(n log n)." (19:9).

> "for many datasets it can be kept at the default value of minPts = 4 (for two-dimensional data)"
> (19:11).

> "The desirable amount of noise will usually be between 1% and 30%." (19:12).

> "For ε < 500, all the indexes provide a 10- to 1000-fold speedup over the linear scan." (19:14).

> "the original DBSCAN algorithm with appropriate (i.e., reasonably small ε) parameters and indexes is
> efficient" (19:18).

**Limitações ou ressalvas:**
- É uma resposta a um artigo específico. Os experimentos usam o código de Gan & Tao e três conjuntos
  de dimensão até 7 e muitos pontos duplicados, e as conclusões sobre "ε razoável" dependem deles.
  Os próprios autores levantam a hipótese de que os conjuntos "talvez não contenham agrupamentos
  significativos" (19:19).
- Os tempos comparam implementações de linguagens e otimizações diferentes (C++ de propósito único,
  com inteiros, contra o ELKI em Java com `double`). Os autores reconhecem que a comparação com o
  ELKI "não é justa" (19:14).
- A nota 7 informa que Gan & Tao desenvolveram, por comunicação pessoal, uma versão melhorada do
  ρ-DBSCAN para ε pequenos, que não foi avaliada (19:19).
- Divergência com o referencial original: ele dizia que o artigo esclarece "a complexidade real do
  DBSCAN sequencial (O(n log n) com índice espacial, O(n²) sem índice)". O artigo diz o contrário
  quanto ao O(n log n): não há garantia, e o custo é O(C + n·Q + Σrᵢ), dependendo do índice e dos
  dados. O restante (a correção de afirmações do SIGMOD 2015) se confirma.

---

## Revisão de consistência do slot DB1

*(22/10/2026.)*

- Complexidade: Ester et al. (p. 229) derivam O(n · log n) em média com R*-tree, e Schubert et al.
  (19:6–19:7) mostram que essa derivação é informal e que o O(n²) no pior caso já era conhecido. Para
  o relatório, o custo é O(C + n·Q + Σrᵢ), Θ(n²) sem índice, e O(n log n) fica como observação
  empírica ou média, sem garantia.
- Pontos de borda: Ester et al. tratam o caso como "raro", e Schubert et al. dão a consequência
  (resultado dependente da ordem; um grupo pode ficar com menos de minPts pontos). Isso importa em
  qualquer paralelização, que pode atribuir os pontos de borda de forma diferente da do sequencial.
- Parâmetros: os dois recomendam minPts = 4 (2D) e ε pelo gráfico *k*-dist, e Schubert et al.
  acrescentam "2·dim" e os sinais de alerta.
- Escala: Ester et al. testam até 12.512 pontos, e Schubert et al. testam conjuntos maiores, mas
  sequenciais. Nenhum dos dois fala de paralelismo, que é assunto dos slots DB2 a DB4.

---

## Slot DB2: Paralelização em memória compartilhada e com conjuntos disjuntos

*(2 fichas, 22/10/2026: Patwary et al. 2012 e Wang, Gu & Shun 2020. Lidos por inteiro.)*

### [DB2] Patwary, Palsetia, Agrawal, Liao, Manne & Choudhary, 2012: Um novo algoritmo DBSCAN paralelo escalável com a estrutura de dados de conjuntos disjuntos

**Arquivo:** `PATWARY_2012_scalable_parallel_dbscan_disjoint_set.pdf`

**Referência completa:**
Patwary, M. M. A.; Palsetia, D.; Agrawal, A.; Liao, W.-K.; Manne, F.; Choudhary, A. A new scalable
parallel DBSCAN algorithm using the disjoint-set data structure. In: **Proceedings of the International
Conference for High Performance Computing, Networking, Storage and Analysis (SC '12)**, art. 62,
p. 1–11, Salt Lake City, UT, 2012. DOI: 10.1109/SC.2012.9.

**Chave BibTeX:** `patwary2012new`

**Tipo:** Artigo de conferência (SC '12), revisado por pares.

**Versão lida:** PDF da IEEE (11 páginas), sem paginação impressa por página. As páginas citadas são
as do PDF.

**Origem:** trazido por Julia (commit `a12394d`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções I–VI; as provas foram
percorridas e as figuras de speedup lidas pelos valores citados no texto). Citações literais
conferidas com o texto do PDF.

**O que o artigo diz** (p. 1–10 do PDF):
- Problema (p. 1–2): o DBSCAN é difícil de paralelizar porque a expansão de um agrupamento é uma
  busca em largura ou profundidade, inerentemente sequencial. As paralelizações anteriores usam o
  modelo mestre-escravo (dados divididos entre escravos, agrupamentos locais enviados ao mestre, que
  os funde sequencialmente), com alto custo de comunicação e baixa eficiência paralela na fusão.
- Ideia (§III, p. 2–4): ver o crescimento de região como componentes conexos de um grafo. Cada ponto
  começa como uma árvore de um nó, e as árvores do mesmo agrupamento são fundidas com *union-find*
  (estrutura de conjuntos disjuntos, com o algoritmo de Rem e compressão por *splicing*) em ordem
  arbitrária, o que quebra a dependência de ordem. No **DSDBSCAN** (Alg. 2), cada ponto consulta a
  vizinhança. Se for núcleo, une-se (`UNION`) aos vizinhos núcleo e, para vizinhos que não são
  núcleo e ainda não têm agrupamento, une-os ao seu (uma borda só entra em um agrupamento). A
  complexidade declarada é O(n log n), igual à do DBSCAN com kd-tree (p. 2–3). O Teorema 3.1 diz que
  a solução satisfaz maximalidade, conectividade e ruído (p. 4).
- Memória compartilhada, PDSDBSCAN-S (§IV-A, Alg. 3 e 4, p. 4–5): os n pontos são divididos em p
  partições iguais, uma por *thread*. Cada *thread* roda o DSDBSCAN sobre os seus pontos, consultando
  vizinhos em toda a memória compartilhada (o único acesso a dados de outra partição é a leitura
  para obter vizinhos). Para vizinhos de outra partição, o par (x, x′) é guardado num conjunto Yₜ e
  resolvido depois, numa segunda fase de fusão em paralelo com `UNION` protegido por travas (uma
  trava por ponto, adquirida para alterar o pai de uma raiz). Os autores dizem que as esperas por
  travas "não aconteceram com muita frequência" e que só uma fração pequena dos pares resulta em
  fusão de fato: em média 0,27% (reais), 0,09% (sintéticos agrupados) e 4,25% (sintéticos
  aleatórios, exceto r50k e r100k, com 39,38%) (p. 8).
- Memória distribuída, PDSDBSCAN-D (§IV-B, Alg. 5 e 6, p. 5–7): cada processador tem uma partição
  igual. Um passo de pré-processamento (*gather-neighbors*) estende a caixa delimitadora da partição
  de ε em cada dimensão e copia os pontos remotos da região de sobreposição, de modo que a fase
  local roda sem comunicação. A fusão usa mensagens `UNIONQUERY` e `PARALLELUNION`, agrupadas em
  pacotes e comprimidas. Para particionar os dados, usam uma kd-tree (paralela na versão
  distribuída) que divide os pontos igualmente entre os núcleos (p. 7 e 9).
- Pontos de borda: o resultado paralelo satisfaz as mesmas três condições, e "a única diferença é que
  pontos de borda podem ser atribuídos a agrupamentos diferentes" (p. 5).
- Experimentos em memória compartilhada (§V-A/B, p. 7–9): máquina Dell com 4 Xeon E7-4850 a 2,0 GHz
  (40 núcleos, 128 GB), C++ com OpenMP (gcc 4.6.3, -O3). São 18 conjuntos em três categorias de
  seis: reais (Chameleon t4.8k a t8.8k; CUCIS *edge* e *texture*) e sintéticos (gerador IBM), de até
  1,9 milhão de pontos e até 20 dimensões. Todas as implementações usam kd-tree, e o tempo de
  construção da kd-tree não é paralelizado nem contado nos speedups (p. 8). O DSDBSCAN sequencial,
  comparado ao DBSCAN clássico, melhora de 1,24% a 10,08% (reais), de −0,14% a 13,84% (aleatórios) e
  de −10% a 8,70% (agrupados). O speedup máximo em 40 núcleos é 21,20 (*texture*), 25,97 (c1.9m) e
  23,86 (r500k). As faixas são de 3,63 a 21,20 (média 9,53) nos reais, de 12,02 a 25,97 (média 20,01)
  nos agrupados e de 7,01 a 23,86 (média 13,96) nos aleatórios. Os conjuntos reais pequenos saturam
  em torno de 8 núcleos. A versão mestre-escravo reimplementada pelos autores (sem acesso aos
  códigos originais) atinge speedup máximo de 3,35 até 8 núcleos e 4,12 com 40 (p. 9).
- Experimentos em memória distribuída (§V-C, p. 9–10): o *Hopper*, um Cray XE6 do NERSC (nós com dois
  AMD MagnyCours de 12 núcleos a 2,1 GHz e 32 GB), com C/C++ e MPI. São 10 conjuntos: sintéticos
  "estendidos" (c61m, c91.5m, c115.9m; r61m, r91.5m, r115.9m, com dados replicados três vezes por
  dimensão, de 61 a 115,9 milhões de pontos, d = 10) e quatro conjuntos da simulação Millennium (10%
  de amostra; 72,3 a 100,4 milhões de pontos, d = 8 ou 9). Os tempos com 1 núcleo (Tabela II) vão de
  219,33 s (db) a 8.511,87 s (c115.9m). O tempo de particionamento da kd-tree não entra no cálculo do
  speedup (p. 9). Para a maioria dos conjuntos usam até 4.096 núcleos, porque o speedup deixa de
  crescer. Para o conjunto *mm* (72,3 milhões de pontos, d = 9), o speedup é 5.765 com 8.192 núcleos
  e 5.124 com 16.384, e acima de ≈ 10.000 núcleos a comunicação domina (p. 10). Os speedups nos
  conjuntos agrupados e nos Millennium (db, mb, md) são bem menores que nos aleatórios, o que os
  autores associam ao maior número de operações `UNION` e de vizinhos locais. O *gather-neighbors*
  custa em média 0,27% (agrupados), 1,25% (Millennium) e 3,76% (aleatórios) do tempo total, com
  máximo de 9,82%. A memória ocupada pelos pontos remotos, em 64 e 512 núcleos, é 0,68× e 1,27×
  (agrupados), 1,57× e 2,94× (aleatórios) e 1,02× e 3,18× (Millennium) a dos pontos locais (p. 10).
- Inconsistências internas resolvidas, adotando o que está nos experimentos descritos:
  1. O resumo e a conclusão associam o speedup de 25,97 com 40 núcleos a "conjuntos de até centenas de
     milhões de pontos". Os experimentos em memória compartilhada usam até 1,9 milhão de pontos
     (p. 7) e o 25,97 é do c1.9m. Os conjuntos de dezenas a centenas de milhões de pontos (máximo de
     115,9 milhões, Tabela II) só aparecem nos experimentos distribuídos. Adotado: 25,97 em 40
     núcleos para ≈ 1,9 milhão de pontos, e 5.765 em 8.192 núcleos para 72,3 milhões.
  2. O resumo apresenta "5.765 com 8.192 núcleos" como resultado geral, mas é o melhor caso (*mm*).
     Nos demais conjuntos o speedup é menor (Fig. 6). Adotado: citar o 5.765 sempre com o conjunto.
  3. O texto (p. 7–8) dá a fração do tempo da construção da kd-tree "0,91% a 1,88%" para os
     conjuntos sintéticos agrupados e, na frase seguinte, "resultados semelhantes para os sintéticos
     agrupados (0,23% a 1,23%)", com o mesmo rótulo. Como a Fig. 3(a) se refere aos agrupados,
     adotamos 0,91%–1,88% para os agrupados e tratamos a segunda faixa como a dos aleatórios (rótulo
     trocado; inferência nossa). Para os reais, é de 0,06% a 18,28%.
  4. A Tabela II traz o mesmo tempo de 1 núcleo (4.121,75 s) para c61m e para r115.9m, sem outro
     sinal de erro. Fica registrado como aparece, e esses dois valores não são usados em nenhum
     cálculo.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): a estratégia de fusão com *union-find* (união de árvores) como
  forma de evitar o gargalo de fusão sequencial, e a divisão em fase local e fase de fusão.
- Relatório §2 e §4: contraponto entre mestre-escravo e totalmente distribuído, com números.
- Relatório §3 (Setup Experimental): descrição de *testbed* (conjuntos reais e sintéticos, kd-tree).

**Aplicabilidade ao nosso caso** (Java, `ExecutorService`/`ForkJoin`, uma máquina, até 150.000
pontos): alta para o PDSDBSCAN-S (memória compartilhada, *threads*, kd-tree, 40 núcleos), de onde
saem os números mais próximos do nosso cenário. O PDSDBSCAN-D (MPI, milhares de núcleos) é só
contexto. O artigo usa C++/OpenMP e conjuntos de até 1,9 milhão de pontos em memória compartilhada.
Descritivo.

**Dado/afirmação específica a usar:**
> "The DBSCAN algorithm is challenging to parallelize as its data access pattern is inherently
> sequential." (p. 1).

> "Note that the merging can be performed in an arbitrary order." (p. 1). É a base do uso de
> *union-find*.

> "The algorithm initially creates a singleton tree for each point of the dataset." (p. 2).

> "The only non-local data access by any thread is the reading to obtain the neighbors using the
> GETNEIGHBORS function." (p. 5). Explica por que a fase local do PDSDBSCAN-S não precisa de
> comunicação.

> "The only difference being that border points might be assigned to different clusters." (p. 5). O
> resultado paralelo pode diferir do sequencial só nos pontos de borda.

> "The testbed contains up to 1.9 million data points and each data point is a vector of up to 20
> dimensions." (p. 7). É a escala dos experimentos de memória compartilhada.

> "we have been able to obtain a maximum speedup of 21.20, 25.97, and 23.86 for" (p. 8). São os
> speedups máximos em 40 núcleos para *texture*, c1.9m e r500k.

> "performs reasonably well up to 8 cores (maximum speedup 3.35)" (p. 9). É o mestre-escravo.

> "The maximum speedup we obtained using the master-slave based parallel DBSCAN algorithm is 4.12 using
> 40 cores" (p. 9).

> "we do not include the partitioning time while computing the speedup by the PDSDBSCAN-D." (p. 9).

> "we achieved a speedup of 5,765 using 8,192 process cores whereas the speedup is 5,124 using 16,384
> process cores." (p. 10).

**Limitações ou ressalvas:**
- O artigo reporta speedup e não o tempo absoluto (só o de 1 núcleo, nas Tabelas I e II). O tempo de
  construção e de particionamento da kd-tree fica de fora dos speedups (p. 8–9), o que favorece os
  números.
- A comparação com o mestre-escravo usa uma reimplementação dos próprios autores (p. 9), sem os
  códigos originais.
- Os speedups variam muito com o tipo de dado (3,63 a 25,97 em memória compartilhada), e os
  conjuntos reais, pequenos, saturam cedo.
- O resultado em pontos de borda pode divergir do sequencial (p. 5), e o artigo não quantifica
  quanto.
- A complexidade O(n log n) repetida do DBSCAN com índice (p. 2) herda a ressalva de Schubert et al.
  (2017, 19:7): não é uma garantia.
- Os valores dos speedups intermediários só existem em figuras, e o texto cita os máximos.
- Concordância com o referencial original: *union-find* e fusão sem comunicação excessiva se
  confirmam. O referencial dizia "evitando sincronização excessiva", mas a versão de memória
  compartilhada usa travas na fase de fusão (Alg. 4).

### [DB2] Wang, Gu & Shun, 2020: DBSCAN paralelo teoricamente eficiente e prático

**Arquivo:** `WANG_2020_theoretically_efficient_parallel_dbscan.pdf`

**Referência completa:**
Wang, Y.; Gu, Y.; Shun, J. Theoretically-efficient and practical parallel DBSCAN. In: **Proceedings of
the 2020 ACM SIGMOD International Conference on Management of Data (SIGMOD '20)**, p. 2555–2571,
Portland, OR, 2020. DOI: 10.1145/3318464.3380582.

**Chave BibTeX:** `wang2020theoretically`

**Tipo:** Artigo de conferência (SIGMOD '20), revisado por pares.

**Versão lida:** PDF da ACM (17 páginas), com paginação impressa 2555–2571 conferida (página impressa
= página do PDF + 2554). As provas e o detalhe dos algoritmos de conectividade (BCP em dimensões
altas) estão na versão completa citada pelo artigo [83], que não está no repositório.

**Origem:** trazido por Sofia (commit `e6c8608`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–8, teoremas e a seção de
experimentos). As descrições dos algoritmos de conectividade e as provas foram percorridas, não
reproduzidas, e as figuras de speedup foram lidas pelos números citados no texto. Citações literais
conferidas com o texto do PDF.

**O que o artigo diz** (p. 2555–2568):
- Problema e contribuição (p. 2555–2556): os algoritmos paralelos de DBSCAN existentes escalam, mas
  "requerem trabalho quadrático no pior caso". Os autores propõem algoritmos paralelos para o DBSCAN
  euclidiano exato (2D e dimensões maiores) e aproximado (ρ-aproximado de Gan & Tao), com trabalho
  igual ao dos melhores algoritmos sequenciais e profundidade polilogarítmica, além de implementações
  otimizadas. Os algoritmos exatos produzem o mesmo resultado da definição padrão (p. 2556).
- Definição usada (p. 2556): pontos núcleo; dois núcleos estão no mesmo agrupamento se ligados por uma
  cadeia de núcleos a distância ≤ ε; um ponto não núcleo pode pertencer a vários agrupamentos (ponto
  de borda) e, "para um dado conjunto e parâmetros, os agrupamentos retornados são únicos".
- Modelo de custo (p. 2556): modelo trabalho-profundidade, com tempo ≈ W/P + D (teorema de Brent).
  A implementação usa Cilk Plus (escalonador *work-stealing*, p. 2564) e primitivas do PBBS (soma de
  prefixos, *filter*, ordenação, ordenação de inteiros, *semisort*, tabela *hash* concorrente,
  triangulação de Delaunay 2D).
- Estrutura comum (§3, Alg. 1, p. 2558): (1) dividir os pontos em células disjuntas de lado ε/√d
  (todos os pontos de uma célula ficam a distância ≤ ε entre si), pelo método de grade ou, em 2D, de
  caixas; (2) marcar os núcleos (uma célula com ≥ minPts pontos é toda núcleo; senão cada ponto conta
  vizinhos nas células vizinhas); (3) montar o grafo de células, com uma aresta entre duas células
  núcleo se o par de pontos mais próximo (BCP) está a distância ≤ ε, e achar seus componentes
  conexos, que dão os agrupamentos; (4) atribuir os pontos de borda aos agrupamentos de núcleos a
  distância ≤ ε.
- Paralelização (§4–5, p. 2558–2561): a distribuição dos pontos às células usa *semisort* (trabalho
  linear) e uma tabela *hash* de células não vazias. A grade em caixas usa *pointer jumping*, e a
  conectividade em 2D usa BCP, USEC com separação por reta ou Delaunay. Em dimensões maiores, as
  células vizinhas são achadas com uma k-d tree (construída em paralelo, com cache do resultado) e a
  contagem em faixa (*RangeCount*) usa uma quadtree por célula (p. 2561). A k-d tree não indexa os
  pontos, e sim as células. O método de aproximação usa quadtrees com l = 1 + ⌈log₂(1/ρ)⌉ níveis.
- Garantias (§6, p. 2562–2563): o 2D exato custa O(n log n) de trabalho e O(log n) de profundidade com
  alta probabilidade (Teorema 6.1). O exato em d = 3 custa O((n log n)^(4/3)) e, em d > 3,
  O(n^(2−2/(⌈d/2⌉+1)+δ)) de trabalho esperado, com profundidade polilogarítmica (Teorema 6.2). O
  aproximado custa O(n) de trabalho e O(log n) de profundidade (Teorema 6.3). A implementação para
  d ≥ 3 usa o método de BCP de custo quadrático, porque o algoritmo teórico de BCP em dimensões
  altas "não é prático" (p. 2563).
- Experimentos (§7, p. 2563–2567). Os dados sintéticos têm 10 milhões de pontos (*seed spreader* de
  Gan & Tao, com densidade similar ou variável; *UniformFill*) em d = 2, 3, 5 e 7. Os reais são
  Household (7-D, 2.049.280 pontos), GeoLife (3-D, 24.876.978; muito enviesado), Cosmo50 (3-D,
  321.065.547), OpenStreetMap (2-D, 2.770.238.904) e TeraClickLog (13-D, 4.373.472.329, "o maior já
  usado na literatura para DBSCAN exato"). ε e minPts foram escolhidos por busca até obter o
  agrupamento correto, e minPts "no máximo 100 costuma dar os agrupamentos corretos" (p. 2565),
  citando a recomendação de Schubert et al. As máquinas são instâncias Amazon EC2: c5.18xlarge (2
  Intel Xeon Platinum 8124M a 3,0 GHz, 36 núcleos com *hyper-threading* de duas vias, 144 GB) e
  r5.24xlarge (48 núcleos, 768 GB) para os três conjuntos maiores, com g++ 7.4 -O3 e Cilk Plus
  (p. 2564). As implementações comparadas são a serial Gan&Tao-v2 (a melhor serial conhecida), o
  `pdsdbscan` (Patwary et al., com OpenMP), o `hpdbscan` (Götz et al., com OpenMP) e o `rpdbscan`
  (distribuído, Spark), cujo resultado "não é o mesmo do DBSCAN" (p. 2564).
- Resultados (p. 2564–2567):
  - Serial: as implementações seriais dos autores são em média 5,18× (exato) e 1,52× (aproximado)
    mais rápidas que a Gan&Tao-v2 (p. 2565).
  - Paralelo, 36 núcleos com HT: speedup de 5 a 33× (média 16×) sobre as melhores seriais, e de 2 a
    89× (média 24×) self-relative (exato) e de 14 a 44× (aproximado). O speedup no GeoLife é baixo
    por causa do enviesamento (p. 2565).
  - Contra `pdsdbscan` e `hpdbscan`: ficam de 16× a 6.102× mais lentos que os do artigo. Os dois têm
    bom speedup self-relative (22–31× e 7–20×, respectivamente), mas "falham em superar a
    implementação serial na maioria dos conjuntos" (p. 2565). Eles ficam mais lentos com ε maior,
    porque usam consultas de vizinhança por ponto (p. 2564). Uma linha de base própria, com k-d tree
    e consultas por ponto, foi mais de 10× mais lenta que a melhor implementação deles.
  - Exato contra aproximado: com parâmetros adequados, o exato é 1,24× (paralelo) e 1,53× (serial)
    mais rápido que o aproximado.
  - Grandes conjuntos (Tabela 2, p. 2566): de 18 a 577× mais rápido que o `rpdbscan` com o mesmo
    número ou menos núcleos. Os próprios autores ressalvam que, no TeraClickLog, com os parâmetros
    usados, "todos os pontos caem numa só célula" e são trivialmente núcleo de um único agrupamento.
  - 2D (§7.3): a implementação mais rápida em geral foi a `our-2d-grid-bcp`. A grade vence a caixa, e
    Delaunay é o mais lento.
- Conclusão (p. 2568): algoritmos "eficientes em trabalho e de profundidade polilogarítmica", com
  "excelente speedup paralelo", superando as soluções existentes. O trabalho futuro é em variantes e
  no DBSCAN hierárquico.
- Inconsistências resolvidas, adotando o que as implementações e experimentos mostram:
  1. O resumo diz que o artigo apresenta algoritmos que "igualam os limites de trabalho" dos
     melhores sequenciais e as respectivas implementações. Para o d ≥ 3 exato, o limite teórico não
     é implementado: a implementação usa BCP de custo quadrático (p. 2563). Adotado: os limites
     teóricos valem para os algoritmos, e as medidas de d ≥ 3 valem para a implementação quadrática
     (com *bucketing* e quadtrees como otimizações práticas).
  2. Os speedups "de até 33×" (sobre a melhor serial) e "de até 89×" (*self-relative*) têm baselines
     diferentes e não devem ser misturados. Adotado: citar sempre qual baseline, e que o 89× inclui
     *hyper-threading* (72 *threads* em 36 núcleos).

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): estrutura em células de lado ε/√d, grafo de células e
  componentes conexos como alternativa à expansão por consulta de vizinhança, com separação em fases
  (células, núcleos, conectividade, borda).
- Relatório §4 (Análise de Escalabilidade): o método de reportar speedup sobre a melhor serial além
  do self-relative, e a evidência de que o speedup self-relative isolado pode não superar uma boa
  implementação serial (`pdsdbscan`, `hpdbscan`).
- Relatório §3 (Setup Experimental): uso de parâmetros que geram o agrupamento "correto".

**Aplicabilidade ao nosso caso** (Java, `ExecutorService`/`ForkJoin`, uma máquina, até 150.000
pontos): alta, por ser memória compartilhada em uma máquina e por descrever como medir speedup sobre
a melhor serial. A base de código é C++/Cilk Plus, com 36 núcleos e 10 milhões de pontos em 2D–7D, e
os algoritmos de BCP/Delaunay têm grande complexidade de implementação. A estrutura em células (grade
de lado ε/√d) vale para qualquer d, e a eficiência teórica é para distância euclidiana. Descritivo.

**Dado/afirmação específica a usar:**
> "However, existing parallel DBSCAN algorithms require quadratic work in the worst case." (p. 2555).

> "Unlike many existing parallel algorithms, our exact algorithms produce the same results according to
> the standard definition of DBSCAN, and so we do not sacrifice clustering quality." (p. 2556).

> "Note that a non-core point can belong to multiple clusters." (p. 2556).

> "For a given set of points and parameters ϵ and minPts, the clusters returned are unique." (p. 2556).
> A unicidade vale com pontos de borda em vários agrupamentos, diferente da atribuição única de
> Ester e Schubert.

> "The cells have the property that all points inside a cell are within a distance of ϵ from each other."
> (p. 2558).

> "we first insert all cells into a k-d tree [6], which enables us to perform range queries to obtain
> just the non-empty neighboring cells." (p. 2561).

> "The theoretically-efficient BCP algorithm for higher dimensions is not practical, and so our
> implementation of DBSCAN uses the approach described in Section 4.4 in higher dimensions, which takes
> quadratic work." (p. 2563).

> "In general, both pdsdbscan and hpdbscan becomes slower with increasing ϵ." (p. 2564).

> "Our implementations obtain very good speedups on most datasets, achieving speedups of 5–33x (16x on
> average) over the best serial baselines." (p. 2565).

> "Although hpdbscan and pdsdbscan achieve good self-relative speedup (22–31x and 7–20x, respectively),
> they fail to outperform the serial implementation on most of the datasets." (p. 2565).

> "a minPts value of at most 100 usually gives the correct clusters." (p. 2565).

> "The purpose of this experiment is to show that we are able to efficiently process large datasets using
> just a multicore machine." (p. 2566).

**Limitações ou ressalvas:**
- Os algoritmos e as provas valem só para distância euclidiana e dados pontuais (p. 2556), limitação
  que Schubert et al. (2017) apontam para os métodos em grade.
- Os melhores resultados dependem de ε e minPts "corretos" escolhidos por busca (p. 2564). Com ε
  grande demais, a estrutura em células perde a vantagem, e a mesma crítica de Schubert et al. se
  aplica à escolha de parâmetros em *benchmarks*.
- A comparação com o `rpdbscan` mistura um sistema distribuído em Spark com um de memória
  compartilhada. No TeraClickLog "todos os pontos caem numa só célula" (p. 2566), o que torna o caso
  trivial.
- Os baselines `pdsdbscan` e `hpdbscan` são código de terceiros compilado e, no caso do segundo,
  modificado pelos autores (remoção do código de saída em arquivo) (p. 2564). Os autores não afirmam
  que reproduzem as condições de Patwary et al.
- Os detalhes dos algoritmos de conectividade e as provas estão na versão completa [83], fora do
  repositório.
- Divergência com o referencial original: ele dizia "usando k-d trees para as *range queries*", mas
  a k-d tree só acha células vizinhas em d ≥ 3, e a contagem em faixa usa quadtrees por célula. Em 2D
  usa-se grade ou caixas, sem k-d tree (p. 2561). O restante (referência para memória compartilhada,
  garantias teóricas, desempenho prático) se confirma.

---

## Revisão de consistência do slot DB2

*(22/10/2026.)*

- Há duas filosofias de paralelização em memória compartilhada. Patwary et al. mantêm a expansão por
  consulta de vizinhança por ponto e paralelizam dividindo os pontos entre *threads*, resolvendo as
  ligações entre partições com *union-find* e travas. Wang et al. mudam a estrutura do algoritmo
  (células, grafo de células, componentes conexos), de modo que o trabalho já cai no custo dos
  melhores sequenciais.
- Patwary et al. reportam speedup de até 25,97 em 40 núcleos (sobre a própria versão de 1 núcleo, até
  1,9 milhão de pontos) e superioridade sobre um mestre-escravo que eles mesmos reimplementaram
  (≈ 4,12). Wang et al. mediram o `pdsdbscan` (de Patwary et al.) em outro testbed e encontram
  speedup self-relative de 7 a 20× em 36 núcleos com HT, mas sem superar a melhor serial na maioria
  dos conjuntos. Os dois resultados não se contradizem (hardware, dados e baselines diferentes) e
  mostram que o speedup self-relative não basta: o relatório deve dizer qual baseline foi usado.
- Sobre os pontos de borda, Patwary et al. dizem que o resultado pode atribuir pontos de borda a
  agrupamentos diferentes dos do sequencial. Wang et al. admitem que um ponto de borda esteja em vários
  agrupamentos (resultado único como conjuntos), e Ester et al. e Schubert et al. (DB1) o atribuem ao
  primeiro agrupamento. O relatório deve definir qual convenção a implementação usa e como compara
  com o sequencial.
- Quanto à complexidade, Patwary et al. repetem O(n log n) para o DBSCAN com índice (herdado de Ester
  et al.), com a ressalva de Schubert et al. (DB1). Wang et al. dão limites formais (O(n log n) em 2D
  euclidiano) e declaram que, para d ≥ 3, o limite teórico exato não é o implementado.
- Na escala, Patwary et al. (memória compartilhada) testaram até 1,9 milhão de pontos, e Wang et al.,
  de 10 milhões (sintéticos) a 4,4 bilhões (TeraClickLog), com máquinas de 36–48 núcleos. O nosso
  intervalo (até 150.000 pontos) é menor que o de ambos.
- As inconsistências internas resolvidas estão no §9: Patwary et al. (conjuntos "de centenas de
  milhões" contra até 1,9 milhão em memória compartilhada; 5.765 como melhor caso; rótulo da faixa de
  kd-tree; tempo repetido na Tabela II) e Wang et al. (limite teórico contra implementação quadrática;
  baselines de speedup).

---

## Slot DB3: Paralelização em memória distribuída (mestre-escravo, MPI, MapReduce, Spark)

*(7 fichas, 22/10/2026: Xu et al. 1999, Götz et al. 2015, He et al. 2011, He et al. 2014, Dai & Lin 2012, Han et al. 2016 e Cordova & Moh 2015. Lidos por inteiro.)*

### [DB3] Xu, Jäger & Kriegel, 1999: Um algoritmo de agrupamento paralelo rápido para grandes bancos de dados espaciais (PDBSCAN)

**Arquivo:** `XU_1999_fast_parallel_clustering_large_spatial_databases.pdf`

**Referência completa:**
Xu, X.; Jäger, J.; Kriegel, H.-P. A fast parallel clustering algorithm for large spatial databases.
**Data Mining and Knowledge Discovery**, v. 3, n. 3, p. 263–290, 1999. DOI:
10.1023/A:1009884809343 (conferido no Crossref, que lista também a reedição em capítulo de livro,
*High Performance Data Mining*, Springer, 2002).

**Chave BibTeX:** `xu1999fast`

**Tipo:** Artigo de periódico, revisado por pares.

**Versão lida:** PDF da editora (28 páginas), com paginação impressa 263–290 conferida (página
impressa = página do PDF + 262). As figuras de resultado (Fig. 16 a 20) são imagens, sem valores
numéricos extraíveis, e só se usa o que o texto afirma.

**Origem:** trazido por Julia (commit `a12394d`, 21/10/2026). Cabeçalho do PDF conferido (volume 3,
p. 263–290); volume 3, número 3 e DOI conferidos no Crossref (22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–6 e apêndice; as
demonstrações dos Lemas 2, 6, 7 e 8 foram percorridas). Citações literais conferidas com o texto do
PDF.

**O que o artigo diz** (p. 263–290):
- Contribuição (p. 263–264): o **PDBSCAN**, uma paralelização do DBSCAN para arquitetura
  *shared-nothing* (vários computadores em rede), com um índice espacial distribuído, a dR\*-tree
  (dados espalhados entre as máquinas e o diretório do índice replicado em todas). O programa
  principal (mestre) inicia um escravo em cada máquina, distribui o conjunto, gerencia o
  balanceamento de carga dinâmico e funde os resultados. Cada escravo agrupa só os dados locais, com
  comunicação por troca de mensagens (p. 264).
- Crítica aos trabalhos anteriores (p. 264–265): os algoritmos paralelos de agrupamento anteriores
  (hierárquico e k-means) supõem todos os objetos em memória e exigem muitos processadores.
- Método em três passos (§4.1, p. 268–269): (1) dividir o conjunto em N partições disjuntas (*data
  placement*); (2) rodar o DBSCAN em cada partição concorrentemente; (3) fundir os resultados. O
  posicionamento dos dados deve garantir balanceamento de carga (partições de tamanho próximo, pois
  o tempo do DBSCAN depende só do tamanho), mínimo custo de comunicação (objetos próximos na mesma
  máquina) e acesso distribuído eficiente. Sem índice espacial, o DBSCAN em cada partição custa
  O(|Sᵢ|²), e com R\*-tree custa O(|Sᵢ| log |Sᵢ|) (p. 269). O artigo mostra que, sem acesso a dados
  remotos, um ponto na fronteira de partições pode deixar de ser núcleo e o resultado fica
  incorreto, por isso é necessária uma "visão" além da borda da partição (p. 270).
- dR\*-tree (§4.2, p. 270–273): as páginas de dados (folhas) de uma R\*-tree são ordenadas pelo valor
  de curva de Hilbert do centro de gravidade e divididas em N blocos de tamanho igual entre as
  máquinas, e o diretório é replicado. A consulta em página remota usa mensagens ponto a ponto. O
  Lema 1 dá altura O(log n), e o Lema 2 diz que o custo de uma consulta em páginas e mensagens tem a
  mesma ordem que o da R\*-tree centralizada (duas mensagens por página de dados acessada).
- PDBSCAN e PartDBSCAN (§4.3, p. 274–282): a arquitetura é mestre-escravo, e cada escravo roda o
  **PartDBSCAN**, o DBSCAN adaptado a uma restrição de espaço S (a partição). Os pontos núcleo devem
  estar em S, mas os vizinhos acessíveis podem estar fora (Def. 6–9; Lemas 3–6). Quanto aos
  candidatos à fusão, um agrupamento que se estende para fora da partição entra numa lista L, e para
  reduzir o tráfego só se envia o subconjunto MC(C, S) dos pontos junto à fronteira, a distância
  ≤ Eps dela (Lema 7). O mestre funde dois agrupamentos de partições vizinhas se seus MC se
  intersectam, sem precisar testar se a interseção contém um núcleo (Lema 8). O tempo do PartDBSCAN é
  O(|S| · custo da consulta), ou O(|S| log n) em média com consulta O(log n) (p. 282). O Lema 10 diz
  que o resultado do PDBSCAN é o mesmo do DBSCAN.
- Experimentos (§5, p. 282–287). O hardware são 8 estações HP C160 (150 MHz PA8000) interligadas por
  LAN, em C++ com PVM. Os dados são um sintético de 10.000 a 1 milhão de pontos (cópias concatenadas
  de um arquivo de dois grupos de 10.000 pontos), três conjuntos "birch" (2-D, 100 mil a 800 mil
  pontos, 100 grupos) e o raster SEQUOIA 2000 (5-D, 534 mil pontos sem duplicatas). Os parâmetros
  vêm da heurística de Sander et al. (1998). Avaliam só a eficiência, pois "o PDBSCAN produz sempre
  o mesmo resultado do DBSCAN (Lema 10)". As definições são: *speedup(m)* é o tempo em 1 computador
  dividido pelo tempo em m; *scaleup* é o tempo de DB em 1 dividido pelo tempo de m·DB em m; *sizeup*
  é o tempo de m·DB dividido pelo tempo de DB. O texto descreve "muito bom speedup", quase igual para
  conjuntos de tamanhos e formas diferentes. O birch3 tem speedup menor, por haver muitos grupos de
  densidades variadas e sobrepostos, o que aumenta o desbalanceamento (*skew*). O scaleup é "muito
  bom", e "um problema 8 vezes maior leva de 8 a 10 vezes mais tempo" (sizeup, 8 computadores). O
  custo de E/S fica quase constante com o número de máquinas e o de comunicação (número de
  mensagens) cresce linearmente com ele, e os autores propõem redes mais rápidas (FDDI, HiPPI)
  (p. 286–287). Nenhum valor numérico de speedup está no texto, porque as curvas são imagens.
- Inconsistência interna resolvida: o resumo e a §5.2 descrevem a rede como Ethernet de 10 Mbit/s,
  enquanto a §5 diz "10 MB LAN" (p. 282). Adotado: Ethernet de 10 Mbit/s, que é a unidade usual
  dessa tecnologia e a que o artigo repete duas vezes. O "10 MB" é erro de unidade.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): a primeira paralelização do DBSCAN e o esquema "dividir,
  agrupar localmente, fundir", com a noção de candidatos à fusão junto à fronteira de partição e a
  necessidade de enxergar os vizinhos além da borda.
- Relatório §4 (Análise de Escalabilidade): as definições de *speedup*, *scaleup* e *sizeup*, que os
  trabalhos seguintes (por exemplo, Zhao et al., para o k-means) reutilizam.
- Relatório §1 (Introdução): contexto histórico.

**Aplicabilidade ao nosso caso:** média, como referência conceitual (partição, vizinhos de fronteira,
fusão por interseção). O cenário (8 estações em rede, PVM, índice distribuído) difere de memória
compartilhada em uma máquina. Descritivo.

**Dado/afirmação específica a usar:**
> "A performance evaluation shows that PDBSCAN offers nearly linear speedup and has excellent scaleup and
> sizeup behavior." (p. 263).

> "The main program of PDBSCAN, the master, starts a clustering slave on each available computer in the
> network and distributes the whole data set onto the slaves." (p. 264).

> "They assume that all objects can reside in main memory at the same time." (p. 265).

> "Since the run-time of DBSCAN only depends on the size of the input data, the partitions should be
> almost of equal size" (p. 269).

> "Therefore, to obtain correct clustering results, a "view" over the border of partitions is necessary,
> i.e. the access to remote data should be supported." (p. 270).

> "we propose to use space filling Hilbert curves to achieve good clustering." (p. 271).

> "Obviously, the run-time of PDBSCAN is determined by the slowest slave." (p. 274).

> "We call C a merging candidate." (p. 278). É o conceito de candidato à fusão.

> "The clustering result obtained by PDBSCAN is the same as the clustering result obtained by DBSCAN."
> (p. 282). É o Lema 10.

> "An 8 times larger problem needs about 8 to 10 times more time." (p. 286).

**Limitações ou ressalvas:**
- O hardware é de 1999 (8 estações, rede de 10 Mbit/s). Os resultados de speedup e de scaleup estão
  só em figuras raster e não são reproduzidos aqui, e o texto os qualifica ("muito bom", "quase
  linear").
- O Lema 10 é afirmado como "óbvio a partir dos Lemas 5–8" e não trata a dependência de ordem dos
  pontos de borda compartilhados que Ester et al. (p. 230) e Schubert et al. (19:4) descrevem
  (observação nossa).
- O custo O(|S| log n) repete a suposição informal de consultas O(log n) (ver Schubert et al.,
  19:6).
- O mestre funde os resultados de forma sequencial, o que Patwary et al. (2012, p. 1) criticam como
  gargalo.
- Divergência com o referencial original: ele dizia "dR\*-tree", e a leitura confirma dR\*-tree (a
  verificação inicial usou um texto extraído sem o símbolo ∗; ver o changelog, §8). Ele também dizia
  "esquema mestre/escravo para distribuir as *region queries*", mas o mestre distribui os dados e
  funde os resultados, e cada escravo faz as próprias consultas.

### [DB3] Götz, Bodenstein & Riedel, 2015: HPDBSCAN, DBSCAN altamente paralelo

**Arquivo:** `GOTZ_2015_hpdbscan_highly_parallel.pdf`

**Referência completa:**
Götz, M.; Bodenstein, C.; Riedel, M. HPDBSCAN: highly parallel DBSCAN. In: **Proceedings of the Workshop
on Machine Learning in High-Performance Computing Environments (MLHPC '15)**, art. 2, p. 1–10, Austin,
TX: ACM, 2015. DOI: 10.1145/2834892.2834894.

**Chave BibTeX:** `gotz2015hpdbscan`

**Tipo:** Artigo de workshop (MLHPC, junto à SC15), revisado por pares.

**Versão lida:** PDF da ACM (10 páginas), sem paginação impressa. As páginas citadas são as do PDF. A
extração do texto tem ruído de diagramação (listagens de código e figuras), mas o texto corrido está
completo.

**Origem:** trazido por Sofia (commit `e6c8608`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–7; as listagens de código
e as figuras foram percorridas). Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 1–10 do PDF):
- Contribuição (p. 1): o **HPDBSCAN**, com três técnicas: (i) uma heurística de custo para a
  decomposição do domínio, (ii) um passo de pré-processamento com índice de dados e (iii) um esquema
  de fusão de agrupamentos baseado em regras. É implementado como aplicação híbrida OpenMP/MPI em
  C++, de código aberto, para memória compartilhada e distribuída (p. 10).
- Ideia (p. 1–2): sobrepor ao conjunto uma hipergrade regular de células, usada (a) como índice
  espacial para reduzir a busca de vizinhos e (b) para dividir o espaço ao longo das bordas das
  células entre os nós de computação. Os pontos são redistribuídos com áreas de halo (células
  fantasma), de modo que não há comunicação intermediária durante a fase local. Em dados
  espacialmente enviesados, cortes regulares do espaço desbalanceiam a carga, e por isso propõem a
  heurística de custo (p. 1–2).
- Indexação (§4.1, p. 3–4): o *bounding box* do conjunto é coberto por uma hipergrade com células de
  lado ε. Cada célula é um *bucket* acessado por um mapa *hash* com deslocamento e ponteiros para a
  memória, e a vizinhança de células são as células à distância de Chebyshev 0 ou 1 (em 2D, 9
  células). Os pontos são ordenados por célula e redistribuídos. A consulta tem custo amortizado O(1)
  sob a suposição de granularidade ótima das células (p. 4), e os autores a consideram superior a
  R-trees e k-d trees nessas condições. O índice é reconstruído após a redistribuição.
- Heurística de custo (§4.2, p. 4–5): o custo de uma célula é o número de avaliações de distância que
  suas consultas exigem (aproximadamente o número de pontos da célula vezes o da vizinhança). O custo
  total é dividido pelo número de núcleos, e os pontos de corte são achados num histograma global
  (MPI) para que cada processador receba uma fração parecida do custo (Fig. 4).
- DBSCAN local (§4.3, p. 5–6): o DBSCAN é convertido para uma versão iterativa, com uma lista de
  vizinhanças por célula (cada vizinhança de célula é calculada no máximo uma vez por *thread*), o
  que permite paralelizar o laço com *threads*. Cada nó produz subagrupamentos e um conjunto de
  regras de equivalência de rótulos.
- Fusão por regras (§4.4, p. 6): ao fim da fase local, cada zona de halo é enviada ao nó dono do
  bloco vizinho. Compara-se o rótulo local e o do halo, gerando regras entre blocos vizinhas que
  podem formar cadeias. As regras são serializadas e difundidas a todos os nós, que as minimizam e
  recebem uma lista de mapeamento direto de cada subagrupamento para um único rótulo global. Cada nó
  re-rotula seus pontos e os devolve ao nó que os leu originalmente.
- Implementação (§5, p. 6–7): leitura paralela (HDF5/pnetCDF) em blocos de mesmo tamanho. A rotulagem
  é sem trava, por `atomicMin` em um inteiro com sinal que codifica o rótulo do agrupamento, o *flag*
  de núcleo e o índice do menor ponto núcleo. O `atomicMin` é implementado com *spinlock* sobre
  leitura e escrita atômica (Listagem 3).
- Experimentos (§6, p. 7–9). A máquina é o cluster JUDGE (Jülich), com nós de dois Intel Xeon X5650
  (Westmere) de 6 núcleos a 2,66 GHz e 96 GB. Os autores conseguiram no máximo 64 nós (768 núcleos),
  com *hyper-threading* desativado ("pode falsear ou desestabilizar a medição"), até 12 *threads* por
  nó e gcc 4.9.2. Usaram dados reais de propósito, porque a maioria dos trabalhos usava dados
  sintéticos (p. 7): *tweets* geolocalizados do Reino Unido em junho de 2014 (≈ 16,6 milhões, com um
  subconjunto menor da primeira semana) e a nuvem de pontos 3-D do centro histórico de Bremen (≈ 81,3
  milhões de pontos, com uma versão menor). Os conjuntos têm dimensão baixa. O speedup chega a um
  pico de 460,0 para Bremen em 768 núcleos (híbrido) e de 263,8 para o Twitter. No Bremen só com
  MPI, são 232,7 em 768 núcleos, "aproximadamente metade" do híbrido (p. 8). No *scaleup* (eficiência
  com o conjunto crescendo proporcionalmente aos núcleos), a eficiência é quase constante no MPI e no
  híbrido, e no OpenMP puro há um pico com 4 ou mais núcleos, que atribuem à contenção do *spinlock*
  do `atomicMin` (p. 9). A memória cresce linearmente com o número de núcleos, por causa dos pontos
  replicados no halo.
- Comparação com o PDSDBSCAN-D (§6.6, p. 9): usaram a implementação de Patwary et al. (a única com
  código aberto). Como ela carrega o arquivo inteiro em cada processo MPI, só rodaram no conjunto
  Twitter menor (os maiores deram falta de memória). O HPDBSCAN tem speedup quase linear, e o
  PDSDBSCAN-D "começa de forma semelhante, mas logo achata, estabilizando em torno de 3,5" e usa
  memória que cresce de forma exponencial. Os autores escrevem: "não conseguimos reproduzir a
  capacidade de desempenho do PDSDBSCAN-D".
- Inconsistências internas resolvidas, adotando o que as definições e as medidas mostram:
  1. A Definição 1 (p. 2) diz que a vizinhança-ε contém os pontos a distância "menor ou igual a ε" e,
     na fórmula, escreve `dist(p, q) < ε` (estrito). Adotado: ≤ ε, que está na prosa e na definição
     de Ester et al. (p. 227). A fórmula estrita é erro de grafia.
  2. O resumo diz que há "melhoria de uma ordem de grandeza em tempo de computação e consumo de
     memória" em relação às tentativas anteriores, mas a comparação feita (§6.6) é só com o
     PDSDBSCAN-D, só no conjunto Twitter menor e com uma implementação que os próprios autores dizem
     não ter conseguido reproduzir no desempenho publicado. Adotado: citar a comparação como "com o
     PDSDBSCAN-D, no conjunto Twitter pequeno" e não como resultado geral.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): decomposição por grade de células de lado ε, halo (células
  fantasma) para evitar comunicação e fusão por regras de equivalência de rótulos.
- Relatório §3 (Setup Experimental): uso de dados reais com enviesamento espacial e de
  *hyper-threading* desativado nas medições.
- Relatório §4 (Análise de Escalabilidade): balanceamento de carga em dados enviesados e custo de
  memória do halo.

**Aplicabilidade ao nosso caso:** média a alta para as ideias (grade com lado ε, halo, fusão por
regras, balanceamento por custo), que independem de MPI. O cenário experimental (até 768 núcleos, 81,3
milhões de pontos, C++) é muito maior que o nosso. A versão OpenMP pura aparece, com ressalva de
contenção, na Fig. 6. Descritivo.

**Dado/afirmação específica a usar:**
> "In our approach we break the interdependency by adopting core ideas of grid-based clustering
> algorithms." (p. 1).

> "so that there is no intermediate communication required during the parallel computation step" (p. 1).

> "Therefore, we employ a cost heuristic to determine a more balanced data space subdivision." (p. 4).

> "Thus each compute node is equipped with a list of direct mappings from each existing subcluster label
> to a single global cluster label." (p. 6). É o resultado da fusão por regras.

> "its parallelization attempts were mainly evaluated using synthetic datasets" (p. 7).

> "we were only able to acquire a maximum of 64 nodes (768 cores)" (p. 7).

> "we disabled this feature as it can falsify or destabilize measurement correctness" (p. 7).

> "It peaks for Bremen at 460.0 using 768 cores" (p. 8).

> "each MPI process always loads the entire datafile into main memory" (p. 9). É a razão do consumo de
> memória do PDSDBSCAN-D.

> "we have not been able to reproduce the performance capabilities of PDSDBSCAN-D" (p. 9).

> "which can be deployed in shared-memory as well as distributed-memory computing environments" (p. 10).

**Limitações ou ressalvas:**
- Não há resultados de tempo absoluto nem de speedup sobre uma implementação serial otimizada. O
  speedup é calculado "em comparação com o mesmo programa em um núcleo" (p. 7–8). Wang et al. (2020,
  p. 2565) medem o `hpdbscan` com speedup self-relative de 22 a 31×, mas sem superar a melhor serial
  na maioria dos conjuntos.
- Os conjuntos têm dimensão baixa (Twitter 2-D e Bremen 3-D), e os autores reconhecem que dados de
  dimensão alta seriam de grande interesse (p. 7). A grade de lado ε cresce exponencialmente com a
  dimensão (crítica de Schubert et al. aos métodos em grade).
- O custo de memória do halo cresce com o número de núcleos e com ε, e o pico de contenção do
  `atomicMin` no OpenMP puro limita o uso só de *threads* (p. 9).
- A comparação com o PDSDBSCAN-D usa só um conjunto pequeno e uma implementação que os autores não
  reproduziram. Não há comparação com o PDSDBSCAN de memória compartilhada (Patwary et al. oferecem
  as duas versões; p. 9).
- O custo O(1) por consulta vale "sob a suposição de granularidade ótima" (p. 4).
- Divergência com o referencial original: ele descrevia "grade com balanceamento de carga" e "OpenMP
  + MPI", o que se confirma. Acrescentamos a heurística de custo para dados enviesados e a fusão por
  regras, que o referencial não detalhava.

### [DB3] He, Tan, Luo, Mao, Ma, Feng & Fan, 2011: MR-DBSCAN, um algoritmo paralelo eficiente de agrupamento por densidade com MapReduce

**Arquivo:** `HE_2011_mr_dbscan_parallel_density_based_clustering_mapreduce.pdf`

**Referência completa:**
He, Y.; Tan, H.; Luo, W.; Mao, H.; Ma, D.; Feng, S.; Fan, J. MR-DBSCAN: an efficient parallel
density-based clustering algorithm using MapReduce. In: **Proceedings of the 17th IEEE International
Conference on Parallel and Distributed Systems (ICPADS 2011)**, p. 473–480, 2011.
DOI: 10.1109/ICPADS.2011.83.

**Chave BibTeX:** `he2011mrdbscan`

**Tipo:** Artigo de conferência (ICPADS 2011), revisado por pares.

**Versão lida:** PDF da IEEE (8 páginas), sem página impressa. As páginas citadas são as do PDF.

**Origem:** trazido por Julia (commit `a12394d`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções I–V; figuras percorridas,
com os resultados de speedup e scaleup só em gráficos). Citações literais conferidas com o texto do
PDF.

**O que o artigo diz** (p. 1–8 do PDF):
- Contribuição (p. 1–2): o **MR-DBSCAN**, um DBSCAN em quatro etapas de MapReduce, com uma estratégia
  de particionamento rápida para dados não indexados, uma análise do critério de fusão entre
  partições vizinhas e um conjunto de otimizações. Os autores dizem ser o primeiro artigo a
  implementar um DBSCAN eficiente num paradigma de MapReduce de 4 etapas (p. 1). Apontam três
  desafios: o MapReduce desencoraja troca de dados entre nós, não oferece índice espacial (R-tree,
  k-d tree) e só paraleliza bem com dados balanceados (p. 1).
- Etapas (§III, p. 3–6):
  1. Pré-processamento: um job de MapReduce que gera o perfil do conjunto (tamanho e distribuição
     espacial) e, a partir dele, um particionamento em grade (adaptado do *grid file*). Cada
     dimensão é dividida em faixas, com duas estratégias (número de *slots* N dado, com N = a·b, ou
     tamanho máximo m de partição).
  2. DBSCAN local em cada partição, usando uma versão modificada do PDBSCAN de Xu et al. A partição
     recebe uma réplica de largura Eps dos vizinhos das partições adjacentes (halo), e o ponto pode
     estar no centro, nas bordas ou no halo interno. Os pontos que extrapolam a partição entram num
     conjunto de candidatos à fusão (MC).
  3. Descoberta dos pares de agrupamentos a fundir, por um job de MapReduce que junta os MC de
     partições vizinhas.
  4. Fusão: a etapa 4.1 constrói o mapeamento global (local para global) num programa de uma só
     *thread*, e a 4.2 reescreve os rótulos num job de MapReduce. Pontos de ruído não são emitidos
     por padrão.
- Fusão paralela (§III-D, p. 4–6): o PDBSCAN original coleta todos os MC numa lista e compara em laço
  aninhado (O(m²), com m o número de pontos em MC), o que é um gargalo. O artigo mostra (Teorema 1)
  que o conjunto de pontos de fusão é (AP₁ ∩ BP₂) ∪ (AP₂ ∩ BP₁) (núcleos de uma partição que são de
  borda na vizinha), e assim a fusão se decompõe em dois jobs de MapReduce e um programa leve de uma
  thread.
- Experimentos (§IV, p. 6–8). O cluster tem 13 nós (um é *jobtracker*/*namenode*), cada um com um
  Intel Core i7 950 de 3,0 GHz (4 núcleos com HT), 8 GB e Ethernet de 1 Gbit, com Hadoop 0.20.2
  (Cloudera) e até 48 tarefas *map* e 48 *reduce* simultâneas (p. 6). Os dados reais são 1,9 bilhão de
  registros de GPS de táxis de Xangai (≈ 6.000 táxis), em quatro conjuntos DS-1 a DS-4 (de 50,4 GB a
  8,4 GB), com coordenadas normalizadas para [0, 1). Há duas cargas: WL-1 (Eps = 0,002; MinPts =
  1000, para achar *outliers* de GPS) e WL-2 (Eps = 0,0002; MinPts = 100, para regiões "quentes"). O
  tempo é o total de todas as etapas. O número ótimo de faixas por dimensão (SPD) foi 80 (WL-1) e 160
  (WL-2), refletindo o compromisso entre balanceamento de carga e pontos duplicados (p. 7). O tempo
  cresce com Eps, por haver mais pontos duplicados e partições desiguais (p. 7). O speedup foi medido
  só no DS-4, "o menor", porque os maiores "levam tempo demais", e o artigo diz que é "quase linear"
  nas duas cargas (p. 8; curvas sem valores no texto). No scaleup, com 2, 4, 8 e 12 nós sobre DS-4,
  DS-3, DS-2 e DS-1, o tempo "cresce a uma taxa lenta" e é insensível a Eps e MinPts (p. 8).
- Observação dos autores (p. 8): os dados de GPS de veículos se concentram em estradas e estão muito
  enviesados, e uma via principal sobre a fronteira de uma partição aumenta muito a replicação.
- Inconsistências internas resolvidas, adotando o que a seção de avaliação descreve:
  1. A introdução (p. 2) descreve os dados como "mais de 1,9 bilhão de registros de GPS de táxis de
     Xangai de um mês", e a seção de avaliação (p. 6) diz que foram coletados "ao longo de dois anos"
     de cerca de 6.000 táxis. Adotado: dois anos, a descrição mais detalhada, da seção em que os
     dados são usados. O "um mês" da introdução não é confirmado em nenhum outro ponto.
  2. A definição de "diretamente alcançável por densidade" (p. 2) usa `Card(N_Eps(o)) ≤ MinPts`,
     enquanto a definição de ponto núcleo na linha seguinte usa `≥ MinPts`. Adotado: ≥ MinPts
     (condição de núcleo de Ester et al.). O "≤" é erro de grafia.
  3. A introdução (p. 1) afirma que o DBSCAN é "insensível à ordem dos pontos", o que só vale para
     núcleos e ruído, pois pontos de borda compartilhados dependem da ordem (Ester et al., p. 230;
     Schubert et al., 19:4). Adotado: citar a insensibilidade à ordem com essa ressalva.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): a estratégia de replicação de largura Eps nas bordas e a fusão
  por pontos de fusão (núcleo de um lado, de borda do outro), que independem de MapReduce.
- Relatório §4 (Análise de Escalabilidade): o compromisso entre a granularidade da partição e os
  pontos duplicados (SPD), e os efeitos de Eps e de dados enviesados.

**Aplicabilidade ao nosso caso:** média. As ideias de halo de largura Eps e de fusão por interseção de
bordas valem para qualquer paralelismo, mas o cenário (Hadoop, 13 nós, 1,9 bilhão de pontos) é muito
diferente. Descritivo.

**Dado/afirmação específica a usar:**
> "Third, it is insensitive to the order of the points in the dataset." (p. 1). Vale com a ressalva
> dos pontos de borda.

> "it is the first paper to implement an efficient DBSCAN algorithm in a 4-stage MapReduce paradigm"
> (p. 1).

> "data transferring between map nodes or reduce nodes is not encouraged, making the parallelism of DBSCAN
> nontrivial." (p. 1).

> "Our partition algorithm is adjusted from the grid file [16]." (p. 3).

> "The original merge algorithm, therefore, could be decomposed and paralleled by two map-reduce phases
> (Stage 3 and Stage 4.2) and a lightweight single-thread program (Stage 4.1)." (p. 5).

> "We conduct all the experiments on a 13-node cluster." (p. 6).

> "It contains 1.9 billion GPS location records collected over two years from about 6,000 taxies in
> Shanghai." (p. 6).

> "This result essentially reflects the tradeoff between load balancing and duplicate points." (p. 7).

> "We only use DS-4, the smallest dataset, because in case of larger datasets the experiments take too
> much time to finish." (p. 8).

> "It can be seen that our algorithm achieves near-linear speedup for both workloads." (p. 8).

**Limitações ou ressalvas:**
- O speedup foi medido só no menor conjunto (DS-4) e é qualificado como "quase linear", sem valores
  numéricos no texto.
- O particionamento é uma grade que não considera a densidade. Os próprios autores observam o
  enviesamento (p. 8), que a versão estendida (He et al., 2014) resolve com um modelo de custo.
- A etapa 4.1 (mapeamento global) roda em uma só *thread*.
- Os dois trabalhos paralelos mais próximos (o PDBSCAN de Xu et al. e uma implementação com ambiente
  de programação paralela) são discutidos só qualitativamente (p. 2).
- Divergência com o referencial original: ele dizia "particionamento espacial balanceado dos dados
  (via amostragem e KD-tree)", mas o artigo não usa k-d tree nem amostragem. Usa uma grade (*grid
  file*) com perfil de contagem por MapReduce, e diz que índices como R-tree e k-d tree são
  "difíceis" para dados grandes (p. 3). A "fusão de clusters entre partições vizinhas" se confirma.

### [DB3] He, Tan, Luo, Feng & Fan, 2014: MR-DBSCAN, um algoritmo DBSCAN escalável com MapReduce para dados muito enviesados

**Arquivo:** `HE_2014_mr_dbscan_extended.pdf`

**Referência completa:**
He, Y.; Tan, H.; Luo, W.; Feng, S.; Fan, J. MR-DBSCAN: a scalable MapReduce-based DBSCAN algorithm for
heavily skewed data. **Frontiers of Computer Science**, v. 8, n. 1, p. 83–99, 2014.
DOI: 10.1007/s11704-013-3158-3.

**Chave BibTeX:** `he2014mrdbscan`

**Tipo:** Artigo de periódico, revisado por pares; versão estendida do He et al. (2011).

**Versão lida:** PDF da Springer (17 páginas), com paginação impressa 83–99 conferida (página impressa
= página do PDF + 82).

**Origem:** trazido por Adriano (commit `2f4599a`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–8). Os teoremas foram
lidos e as demonstrações percorridas, e as tabelas de resultado foram lidas pelos valores. Citações
literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 83–99):
- Problema e contribuições (p. 83–84): três limitações dos DBSCAN paralelos: (1) não balanceiam a
  carga com dados muito enviesados; (2) nem todos os subprocedimentos críticos são paralelos; (3) não
  foram projetados para ambientes *shared-nothing*. Os autores propõem o MR-DBSCAN em 3 etapas
  (particionamento de dados, agrupamento local, fusão global) e, "o mais importante", um método de
  particionamento por estimativa de custo de computação (*cost-based partitioning*, CBP). Dizem ser o
  primeiro trabalho a considerar a densidade dos pontos no DBSCAN paralelo (p. 84).
- Modelo de custo (§4, p. 86–88): o DBSCAN custa O(N·m), com m o custo de uma consulta de vizinhança.
  Com R-tree (empacotamento STR), o custo de uma consulta é medido em acessos a disco (DA) pelo modelo
  de Theodoridis & Sellis, e o custo de um retângulo S é W(S, ε) = N_S · DA(N_S, ε). Para dados
  enviesados, dividem o espaço em células de lado 2ε e supõem que a consulta de cada ponto toca
  "aproximadamente" os N_c pontos da célula. Pela Eq. 9, se N_c < f (fanout) em cada célula, o custo é
  ≈ O(N_S log_f N_S), e se os dados se concentram em poucas células, o custo se aproxima de O(N_S²). O
  particionamento usa BSP (cortes recursivos), com cada corte escolhido para equilibrar o custo
  estimado entre os dois lados (Alg. 2–4). As células tornam barata a busca do corte.
- Particionamento correto (§5.1, p. 90): cada partição inclui uma margem de ε (retângulo externo e
  interno). O Teorema 1 trata da decidibilidade local de núcleos, a Definição 3 do MCPS (conjunto
  mínimo completo de partições), e o Teorema 2 diz que cada núcleo é identificado em exatamente uma
  partição, sem redundância.
- Implementação (§5, p. 90–93): a Etapa 1 é um job de MapReduce que conta pontos por célula e gera o
  índice de partições. A Etapa 2 é outro job: mapeia cada ponto às partições que o contêm, e cada
  *reducer* roda um DBSCAN sequencial com R-tree e grava separadamente os núcleos na margem interna
  (AP) e os pontos alcançáveis na margem externa (BP). Na Etapa 3 (fusão), a 3.1 acha os pares de
  agrupamentos a fundir pelo Teorema 5, MPS = (AP₁ ∩ BP₂) ∪ (AP₂ ∩ BP₁), e constrói o mapeamento
  global por componentes conexos de um grafo de rótulos num único *reducer* (a saída do *map* usa
  chave fixa; p. 92). A 3.2 re-rotula os dados num novo job e resolve os pontos duplicados nas
  margens, que podem ter recebido tipos diferentes em partições diferentes.
- Experimentos (§6, p. 93–97). O cluster tem 128 máquinas (cada uma com um Intel Core i7-950 de 4
  núcleos a 3,0 GHz, 8 GB, 2 discos SATA2 e Ethernet de 1 Gbit), com Hadoop 0.20.2 (Cloudera) e até
  512 tarefas de MapReduce simultâneas (p. 93). Os dados são reais, 2-D, normalizados em [0, 1)²: GPS
  de táxis de Xangai (≈ 1,2 bilhão de registros, 35,76 GB) e TIGER/Line 2010, categoria EDGES (≈ 0,8
  bilhão, 22,60 GB), mais amostras de 1/2, 1/4, 1/8 e assim por diante (p. 93–94). A R-tree tem fanout
  100 e o tamanho máximo de partição é de 7 milhões de pontos. Comparam o CBP com o ESP (divisão em
  partes iguais de pontos) e o RBP (partição com menos pontos na fronteira; θ = 0,3).
  - Estudo de caso de balanceamento (TIGER-0, ε = 0,001, MinPts = 8.000, 512 partições; Tabela 2): o
    tempo máximo de agrupamento local entre as tarefas, que define o tempo do job, é 9.558 no CBP,
    32.435 no ESP e 80.944 no RBP. O coeficiente de variação é 0,154, 0,627 e 1,315 (p. 94–95).
  - Speedup (TIGER-5, ε = 0,001, MinPts = 500; Tabela 4; base: o tempo do CBP com 4 tarefas, T₄): o
    tempo total do CBP, em segundos, com 4, 8, 16, 32 e 64 tarefas é 785, 452, 287, 193 e 142,6 (ESP:
    850, 553, 351, 236 e 157,4; RBP: 3.508, 825, 584, 437 e 321,4). Só a etapa 2 (DBSCAN) do CBP leva
    655, 342, 191, 100 e 58 s, com eficiência de 71% do ideal com 64 tarefas (p. 96). O próprio texto
    explica que o speedup do tempo total é limitado pelas demais etapas e pelo desbalanceamento de
    partições indivisíveis.
  - Scaleup (Tabelas 5 e 6): o CBP tem sempre a menor inclinação, e o pior método é "quase 18 vezes
    mais lento" que o CBP (p. 96–97).
- Inconsistências internas resolvidas, adotando o que as tabelas mostram:
  1. O resumo diz que "todos os subprocedimentos críticos são totalmente paralelizados" e que não há
     gargalo de processamento sequencial, mas a etapa 3.1 constrói o mapeamento global num único
     *reducer* (§5). Adotado: todas as etapas que dependem do tamanho dos dados são paralelas, e o
     mapeamento global é sequencial, sobre uma entrada pequena (pares de rótulos de agrupamentos).
  2. O resumo e a conclusão dizem que o método é "mais de uma ordem de grandeza mais rápido" que os
     existentes. Pelas tabelas, o ganho é de até ≈ 18× sobre o RBP (Tabela 6, T1: 79.816 s contra
     4.334 s), mas de até ≈ 3,4× sobre o ESP (T0: 32.860 s contra 9.765 s). No speedup (tempo total)
     da TIGER-5, é de ≈ 2,3× a 4,5× sobre o RBP. Adotado: "até ≈ 18× sobre o RBP e ≈ 3,4× sobre o
     ESP", e não "uma ordem de grandeza" em geral.
  3. A síntese (§6.7) diz que o CBP "tem o melhor desempenho em todos os experimentos", mas, na
     Tabela 5 (GPS), no menor conjunto (G4), o ESP (338 s) é mais rápido que o CBP (365 s). O próprio
     texto reconhece que, em dados esparsos, o CBP "recua para o ESP". Adotado: CBP melhor em geral,
     e ESP ligeiramente melhor em dados menos enviesados.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): por que dividir por quantidade de pontos pode desbalancear a
  carga e como estimar o custo por célula (dados enviesados), além da margem ε, da decidibilidade
  local dos núcleos e da fusão pelos pontos de fusão.
- Relatório §4 (Análise de Escalabilidade): como medir o desbalanceamento (tempo máximo entre
  tarefas, coeficiente de variação) e como a escolha do particionamento afeta o speedup.

**Aplicabilidade ao nosso caso:** média. O modelo de custo por célula e a lição de que partições
iguais em número de pontos podem desbalancear valem também para *threads*, mas o cenário (128
máquinas, MapReduce, 1,2 bilhão de pontos 2-D) é muito diferente. Descritivo.

**Dado/afirmação específica a usar:**
> "Most importantly, we propose a novel data partitioning method based on computation cost estimation."
> (p. 83).

> "In our algorithm, all the critical sub-procedures are fully parallelized." (p. 83). Vale com a
> ressalva do mapeamento global num único *reducer*.

> "this is the first work that takes the density of points into consideration in parallel DBSCAN" (p. 84).

> "Equation (9) reveals the fact that the density of data is an important matter of the computation cost."
> (p. 88).

> "We conduct the experiments on a shared-nothing cluster with 128 machines." (p. 93).

> "Therefore, the primary evaluation criterion is about the maximal process time among all tasks" (p. 94).

> "choose the time solving TIGER-5 in four tasks by CBP, the fastest method, as the base time" (p. 96).
> O speedup é relativo a 4 tarefas, não a 1.

> "whose efficiency can achieve 71% of that of the ideal case when there are 64 tasks" (p. 96).

> "the worst one is near 18 times slower than our method" (p. 96).

> "In general, CBP has the best performance in all the experiments." (p. 97).

> "We reveal an obscure truth that spatial data partitioning simply by the amount of data is very
> dangerous for load balancing." (p. 98).

**Limitações ou ressalvas:**
- O speedup é relativo a 4 tarefas (não a 1) e só em um conjunto. A eficiência de 71% é da etapa 2 e
  não do tempo total.
- Os dados são 2-D, e o modelo de custo é derivado para 2-D (a dimensão maior fica para extensão).
- O modelo supõe que a consulta de um ponto toca "aproximadamente" os pontos da célula e uma R-tree
  com fanout fixo. Os autores reconhecem que o erro de estimativa causa desbalanceamento (p. 96).
- A margem de ε e a duplicação de pontos crescem com ε, aumentando o custo de *shuffle* e de fusão
  (p. 95).
- Divergência com o referencial original: ele dizia que a versão de 2014 traz a mesma ideia central
  "com análise mais detalhada do desbalanceamento". A versão de 2014 acrescenta muito mais: o modelo
  de custo e o particionamento CBP (que não existem na de 2011), três etapas em vez de quatro, os
  teoremas de partição completa mínima (MCPS) e de fusão rápida, e experimentos com 128 máquinas e
  1,2 bilhão de pontos, contra 13 máquinas e 1,9 bilhão. O referencial recomendava citar a de 2014
  para balanceamento, o que se confirma.

### [DB3] Dai & Lin, 2012: Algoritmo DBSCAN eficiente com Map/Reduce e particionamento de dados otimizado (DBSCAN-MR e PRBP)

**Arquivo:** `DAI_2012_mapreduce_dbscan_optimized_data_partition.pdf`

**Referência completa:**
Dai, B.-R.; Lin, I.-C. Efficient Map/Reduce-based DBSCAN algorithm with optimized data partition. In:
**2012 IEEE Fifth International Conference on Cloud Computing (CLOUD)**, p. 59–66, 2012.
DOI: 10.1109/CLOUD.2012.42.

**Chave BibTeX:** `dai2012efficient`

**Tipo:** Artigo de conferência (IEEE CLOUD 2012), revisado por pares.

**Versão lida:** PDF da IEEE (8 páginas), com paginação impressa 59–66 conferida (página impressa =
página do PDF + 58).

**Origem:** trazido por Julia (commit `a12394d`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–5, algoritmos; figuras de
resultado percorridas, com os valores de tempo só em gráficos). Citações literais conferidas com o
texto do PDF.

**O que o artigo diz** (p. 59–66):
- Problema e ideia (p. 59–60): o DBSCAN em uma máquina não escala. As abordagens distribuídas
  anteriores consultam um índice espacial global, o que gera muita comunicação entre nós e não se
  adapta ao Map/Reduce. Os autores propõem o **DBSCAN-MR**, sem índice global, e um algoritmo de
  particionamento, o **PRBP** (*partition with reduced boundary points*), que escolhe os cortes pela
  distribuição dos pontos para equilibrar a carga e reduzir os pontos de fronteira duplicados.
- Por que os pontos de fronteira importam (§3, p. 60–61): os pontos da região de fronteira são
  copiados para as duas partições adjacentes para permitir a fusão, e mais pontos de fronteira
  aumentam a carga dos nós e o custo de fusão. O exemplo da Fig. 1 mostra duas escolhas de corte com
  44 e 14 pontos de fronteira.
- Cinco fases (p. 61): (1) PRBP; (2) *DBSCAN-Map*, que roda o DBSCAN com kd-tree em cada partição
  (reduz de O(n²) para O(n log n)); (3) *DBSCAN-Reduce*, que reúne os resultados dos pontos de
  fronteira por índice do ponto e marca como núcleo o ponto que é núcleo em alguma partição; (4)
  *Merge*, que constrói a lista de fusão (conjuntos de rótulos a juntar), combinando conjuntos com
  interseção até estabilizar; (5) *Relabel*, que rotula os pontos de fronteira e depois todos os
  pontos com o rótulo global.
- PRBP (§3.1, p. 61–63): cada dimensão é dividida em fatias de largura 2ε (porque o DBSCAN expande no
  raio ε e a fronteira precisa de pelo menos 2ε para conter a informação de fusão). Acumula-se o
  número de pontos por fatia, e a fatia candidata deve ter a contagem acumulada entre θ·N e (1−θ)·N
  (θ ∈ (0; 0,5), para não desbalancear). Entre as candidatas, escolhe-se a de menos pontos, e divide-se
  recursivamente até a partição caber na memória do nó (limite β). Há duas variantes: o DBSCAN-MR
  (usa as informações das fatias já calculadas) e o **DBSCAN-MR-N** (recalcula as fatias a cada
  corte, com menos fronteira, mas mais tempo de particionamento).
- Experimentos (§4, p. 64–66). O cluster é Hadoop 0.20.2 com 10 nós (4 CPUs Intel Xeon de 3,0 GHz e
  4 GB cada), implementado em Java. A comparação é com o GRIDBSCAN reimplementado em Map/Reduce
  (células de largura 10ε) e com o DBSCAN-MR-N. Os dados são quatro sintéticos ampliados a partir
  dos conjuntos do Chameleon (1.000k e 800k pontos) e um real da Califórnia (785.685 objetos). No
  nt7_1000k com 4 nós, o GRIDBSCAN gera 328.900 pontos de fronteira, contra 114.600 (DBSCAN-MR-N) e
  114.700 (DBSCAN-MR). Em 4 nós, o DBSCAN-MR é 21% a 37% mais rápido em tempo total que o GRIDBSCAN,
  e o PRBP reduz os pontos de fronteira em 43% a 77%. O tempo total cai de 1 a 7 nós, e depois o
  custo de E/S em disco e de comunicação reduz o ganho (Fig. 11, p. 66). Os autores afirmam que o
  resultado é exatamente o mesmo do DBSCAN original com os mesmos parâmetros (p. 64).
- Inconsistências internas resolvidas, adotando o que a descrição dos conjuntos e as tabelas mostram:
  1. A lista de dados sintéticos (p. 64) cita "nt7,1000k" e, em seguida, "nt5,800k, nt4,800k e
     nt5,800k", ou seja, o mesmo nome duas vezes. Adotado: nt8,800k como o quarto conjunto, que é o
     que aparece na Fig. 7 (nt7,1000k, nt8,800k, nt4,800k, nt5,800k).
  2. O texto diz que o PRBP reduz os pontos de fronteira em "43% a 77%" e que o DBSCAN-MR é "21% a
     37%" mais rápido, ambos lidos de gráficos (Fig. 9–10). O único valor tabulado é o do nt7,1000k
     (328.900 contra 114.700, uma redução de ≈ 65%). Adotado: citar a tabela para esse conjunto e os
     percentuais do texto como valores de gráfico.
  3. O texto (p. 61) refere-se à "Figura 2(b)" no cálculo de 44 pontos de fronteira, mas o exemplo é
     da Fig. 1(b). Adotado: Fig. 1(b).

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): a motivação de minimizar pontos duplicados na fronteira ao
  particionar, e a largura de fronteira de 2ε.
- Relatório §4 (Análise de Escalabilidade): a contagem de pontos de fronteira e o tempo de fusão como
  custos do particionamento.

**Aplicabilidade ao nosso caso:** média. A ideia de escolher cortes que minimizem os pontos de
fronteira vale para qualquer paralelismo por partição espacial, e o Hadoop em 10 nós difere do nosso
cenário. Descritivo.

**Dado/afirmação específica a usar:**
> "However, choosing different partition mechanisms will affect the execution efficiency and load balance of each node." (p. 59).

> "First, previous works on distributed system query a global spatial index to obtain correct global results, but this approach is not suitable for the Map/Reduce-style system because it incurs lots of inter-node communication." (p. 59).

> "Load imbalance negates the benefits of parallelism." (p. 61).

> "The goal of this step is to minimize the total number of points in partition boundaries." (p. 61).

> "In addition, the efficiency of the original DBSCAN algorithm is improved from O(n2) to O(nlogn) by using the KD-Tree [21] spatial index." (p. 63).

> "Note that the clustering results of DBSCAN-MR are exactly the same as that of the original DBSCAN for the same parameters (MinPts, ε), which means that DBSCAN-MR successfully performs the clustering process of DBSCAN with the cloud computing technology." (p. 64).

> "We can observe that GRIDBSCAN generates much more boundary points (328900) than DBSCAN-MR-N and DBSCAN-MR (114600 and 114700)." (p. 65).

> "It even outperforms GRIDBSCAN algorithm for 21% to 37% on the total execution time." (p. 65).

> "However, the overheads of disk I/O and message communication retard the reduction rate of the total execution time when the number of nodes is further increased." (p. 66).

**Limitações ou ressalvas:**
- A igualdade ao DBSCAN original é afirmada (p. 64), sem demonstração no artigo. A regra de fusão
  considera um ponto como núcleo se for núcleo em alguma partição (§3.3), o que não discrimina pontos
  de borda compartilhados (observação nossa; comparar com He et al., 2011 e 2014, Teorema de pontos
  de fusão).
- São só 10 nós, no máximo 7 no gráfico de escalabilidade, e conjuntos de até ≈ 1 milhão de pontos. A
  maioria dos resultados está em gráficos (Fig. 9–11).
- Os comparadores (GRIDBSCAN e o DBSCAN-MR-N) são reimplementações dos próprios autores em
  Map/Reduce.
- O PRBP escolhe a fatia de menos pontos, o que reduz a fronteira, mas não leva em conta o custo
  (densidade) como He et al. (2014).
- Divergência com o referencial original: ele dizia que o artigo "foca em equilibrar a carga e
  reduzir pontos duplicados entre partições vizinhas", o que se confirma. Acrescentamos o detalhe de
  que a carga é equilibrada pela faixa de contagem acumulada (θ) e pelo limite de memória (β), e não
  por um modelo de custo.

### [DB3] Han, Agrawal, Liao & Choudhary, 2016: Um novo algoritmo DBSCAN escalável com Spark

**Arquivo:** `HAN_2016_scalable_dbscan_spark.pdf`

**Referência completa:**
Han, D.; Agrawal, A.; Liao, W.-K.; Choudhary, A. A novel scalable DBSCAN algorithm with Spark. In:
**2016 IEEE International Parallel and Distributed Processing Symposium Workshops (IPDPSW)**, p. 1393–1402,
2016. DOI: 10.1109/IPDPSW.2016.57.

**Chave BibTeX:** `han2016novel`

**Tipo:** Artigo de workshop (IPDPSW 2016), revisado por pares.

**Versão lida:** PDF da IEEE (10 páginas), com paginação impressa 1393–1402 conferida (página impressa
= página do PDF + 1392). A lista inicial do grupo trazia 2018 e o `referencial_teorico.md` trazia as
páginas 271–280, mas os dados corretos são os do PDF.

**Origem:** trazido por Sofia (commit `e6c8608`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções I–VI; as figuras de
speedup e de tempo foram lidas pelos valores citados no texto e pelos rótulos numéricos extraídos).
Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 1393–1401):
- Contribuição (p. 1393): um DBSCAN em Spark que evita a comunicação entre *executors* e, com isso, as
  operações de *shuffle*, que são caras. Usa kd-tree para reduzir a busca de vizinhos de O(n²) para
  O(n log n). Segundo os autores, nenhuma implementação de DBSCAN em Spark era conhecida (p. 1396).
- Método (§III–IV, p. 1396–1398): o driver lê os dados do HDFS, cria RDDs e distribui os pontos aos
  *executors*. A kd-tree, os parâmetros e as informações de partição são difundidos (*broadcast*) a
  todos, e cada executor calcula agrupamentos parciais só para os pontos da sua partição, sem trocar
  mensagens. As partições são faixas de índices dos pontos (por exemplo, 0–2499 e 2500–4999) e não
  partições espaciais. Como as partições não compartilham pontos, os autores introduzem os **SEEDs**:
  pontos do agrupamento parcial cujo índice cai fora da faixa da partição. Os agrupamentos parciais
  voltam ao driver por acumulador e o driver, usando os SEEDs, acha o agrupamento "mestre" de cada
  SEED e os funde sequencialmente (Alg. 3 e 4). As estruturas Java são `Hashtable` e `LinkedList`.
- Custo (§IV-C, p. 1398–1399): T_p = O(Δ + n log n + (n/p)·V + m·V + t_straggling + n + K·m), com m o
  número de agrupamentos parciais e K o tamanho máximo deles. A fusão no driver custa O(n + K·m) e
  cresce com m.
- Experimentos (§V, p. 1399–1401). A máquina é o Edison (Cray XC30, NERSC; nós com dois Intel "Ivy
  Bridge" de 12 núcleos a 2,4 GHz e 64 GB), com Java 1.7, Spark 1.5 e Hadoop 2.4. Os dados são
  sintéticos do gerador da IBM (como em Patwary et al.), com d = 10, ε = 25 e minpts = 5: c10k e r10k
  (10.000 pontos), c100k e r100k (102.400) e r1m (1.024.000). O tempo de construção da kd-tree é de
  0,05% a 0,5% do total. O speedup nos executores (só computação local) chega a 1,9/3,6/6,2 com 2/4/8
  núcleos (10k), 3,3/6,0/8,8/10,2 com 4/8/16/32 núcleos (100k) e 58/83/110/137 com 64/128/256/512
  núcleos (1 milhão) (p. 1400). Considerando também o driver, as curvas achatam: no 100k, com 32
  núcleos, são gerados 9.279 agrupamentos parciais, e o speedup cai de 10,2 para 5,6 (p. 1400).
- Spark contra MapReduce (§V-D, p. 1400–1401): os autores implementaram o próprio DBSCAN em
  MapReduce, sem acesso aos códigos de outros. Com 10.000 pontos, o Spark é 9 a 16 vezes mais rápido
  (178, 93, 50 e 31 s contra 1.666, 1.248, 832 e 521 s, com 1, 2, 4 e 8 núcleos), e só nesse tamanho,
  porque o MapReduce demorava demais.
- Conclusão (p. 1401): o método escala bem e supera o MapReduce "por um fator de mais de 10". Os
  autores reconhecem que não particionaram os pontos pela vizinhança, o que pode desbalancear a carga,
  e dizem que pretendem fazê-lo.
- Inconsistências internas resolvidas, adotando o que o texto e as medidas mostram:
  1. O resumo cita "speedups de até 6 (8 núcleos, 10k), 10 (32 núcleos, 100k) e 137 (512 núcleos, 1
     milhão)", sem dizer que são speedups só da computação nos executores. O texto (p. 1400) mostra
     que, com o tempo do driver, o do 100k com 32 núcleos é 5,6. Adotado: os valores do resumo são
     speedups dos executores, e o speedup total é menor.
  2. O artigo afirma que "todas as execuções paralelas geram o mesmo resultado da serial" (p. 1399),
     mas, para o conjunto de 1 milhão, o texto informa que se usam ramos podados da kd-tree e que são
     filtrados os agrupamentos parciais pequenos, e que a remoção "não afeta significativamente" a
     acurácia (p. 1400). Adotado: a igualdade ao serial vale para os conjuntos menores, o resultado
     de 1 milhão é aproximado, e o speedup de 137 corresponde a essa configuração.
  3. O resumo e a conclusão dizem "mais de 10 vezes" mais rápido que o MapReduce, mas a medição
     (p. 1400–1401) dá 9 a 16 (≈ 9,4× com 1 núcleo e ≈ 16,8× com 8), só em 10.000 pontos e contra
     uma implementação própria. Adotado: "9× a 17×, em 10 mil pontos".
  4. O speedup de 58 a 137 para 1 milhão de pontos parte de 64 núcleos, sem dizer qual é a linha de
     base (1 núcleo?). Pelas Figs. 6(b) e 8, os tempos dos executores (5.123, 2.764, 1.875 e 1.480 s)
     multiplicados pelos speedups dão tempos de 1 núcleo diferentes (≈ 297.000, 229.000, 206.000 e
     203.000 s). A linha de base não é reconstruível a partir do que está no artigo. Fica registrado
     como não conciliável, e os valores de 58–137 não são usados em cálculos.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): a estratégia de agrupamentos parciais sem comunicação e fusão
  posterior por pontos de ligação (SEEDs).
- Relatório §4 (Análise de Escalabilidade): a distinção entre o speedup da computação local e o
  speedup total (com o custo da fusão), e a relação entre o número de partições, o de agrupamentos
  parciais e o tempo de fusão.

**Aplicabilidade ao nosso caso:** média. A divisão por faixa de índices (sem partição espacial) e a
fusão centralizada no driver são as soluções mais simples e a principal limitação (a fusão cresce
com o número de agrupamentos parciais). Spark e 512 núcleos diferem do nosso cenário. Descritivo.

**Dado/afirmação específica a usar:**
> "Each executor just performs its computation without communicating with others." (p. 1393).

> "This way we avoid shuffle operations that are very expensive." (p. 1393).

> "It is very efficient to use broadcast variables to give every executor a copy of a large input dataset in an efficient manner." (p. 1398).

> "If the current point's index is beyond the range of current partition it is taken as a SEED." (p. 1397).

> "We need to note that all parallel executions generate the same result as the serial execuation." (p. 1399). A grafia é do original, e o próprio artigo qualifica a afirmação para 1 milhão de pontos.

> "It is obvious that the local computation in executors scales better than the whole computation since their computations are independent." (p. 1400).

> "When using 32 cores, 9279 partial clusters are generated in executors and collected in driver. So the speedup drops to 5.6 from 10.2." (p. 1400).

> "Also we filter out those partial clusters whose size is too small, and their removal does not impact the accuracy significantly." (p. 1400).

> "As we are not able to get source code from the other research teams [9], [7], [14], we have implemented our own DBSCAN with MapReduce approach." (p. 1400).

> "We did not partition data points based on the neighborhood relationship in our work and that might cause workload to be unbalanced." (p. 1401).

**Limitações ou ressalvas:**
- Os conjuntos são sintéticos, de 10 dimensões, com ε = 25 e minpts = 5 (parâmetros herdados de
  Patwary et al.). A "alta dimensão" do resumo é d = 10.
- A fusão é sequencial no driver e cresce com o número de agrupamentos parciais, e o particionamento
  por índices pode desbalancear a carga (os próprios autores).
- O speedup de 1 milhão de pontos usa poda da kd-tree e filtragem de agrupamentos pequenos, e a linha
  de base não é reconstruível (ver acima).
- A comparação com o MapReduce é só com 10 mil pontos e uma implementação própria. Não há comparação
  com o PDSDBSCAN nem com o HPDBSCAN, embora o artigo diga que seus resultados "batem" com os de
  Patwary et al. (p. 1399).
- Divergência com o referencial original: as páginas 271–280 dele estão erradas (as corretas são
  1393–1402) e o ano é 2016. O referencial dizia "particionamento espacial e redistribuição de pontos
  de borda", mas o artigo não particiona espacialmente nem redistribui pontos de borda: usa faixas de
  índices e SEEDs.

### [DB3] Cordova & Moh, 2015: DBSCAN sobre Resilient Distributed Datasets (RDD-DBSCAN)

**Arquivo:** `CORDOVA_2015_dbscan_resilient_distributed_datasets.pdf`

**Referência completa:**
Cordova, I.; Moh, T.-S. DBSCAN on resilient distributed datasets. In: **2015 International Conference on
High Performance Computing & Simulation (HPCS)**, p. 531–540, 2015. DOI: 10.1109/HPCSim.2015.7237086.

**Chave BibTeX:** `cordova2015dbscan`

**Tipo:** Artigo de conferência (HPCS 2015), revisado por pares.

**Versão lida:** PDF da IEEE (10 páginas), com paginação impressa 531–540 conferida (página impressa =
página do PDF + 530).

**Origem:** trazido por Sofia (commit `0d1e1ed`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções I–VI; as figuras de
resultado são curvas sem valores no texto). Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 531–540):
- Contribuição (p. 531–532): o **RDD-DBSCAN**, uma adaptação do MR-DBSCAN de He et al. [9] (2011) ao
  modelo de RDDs do Spark, em Scala, que "obtém os mesmos resultados do algoritmo original". A
  motivação é que o MapReduce em Hadoop só comunica entre etapas pelo sistema de arquivos, o que é
  caro em algoritmos iterativos.
- Três fases (§III, p. 533–537):
  1. Particionamento: o retângulo que envolve o conjunto é dividido recursivamente por BSP em dois
     retângulos com número parecido de pontos, até a partição ter menos de MaxPoints pontos (limite
     de memória do menor nó) ou lado menor que 2ε. Depois cada retângulo é ampliado em ε, de modo que
     um ponto pode estar em mais de uma partição. O custo de um corte é só o número de pontos, o que
     os autores contrastam com o modelo de custo de He et al. (2014), por considerarem que o acesso a
     disco não entra no Spark (p. 535).
  2. DBSCAN local com R-tree em memória (O(n log n)). Os rótulos locais são únicos só na partição,
     então o rótulo é o par (partição, agrupamento).
  3. Rotulagem global: o algoritmo de aliases (Alg. 5) examina os pontos a até ε da fronteira
     (excluindo ruído) e, se um ponto tem mais de um rótulo, os agrupamentos são "aliases" do mesmo
     agrupamento global. Um grafo de aliases dá o mapeamento global (Alg. 6), construído de forma
     sequencial (os autores dizem que paralelizá-lo, se necessário, "deve ser trivial").
- Restrição declarada (p. 534): o algoritmo supõe dados representáveis em duas dimensões, porque usa
  a representação 2-D do espaço para o particionamento.
- Experimentos (§IV, p. 537–539): um cluster de 5 máquinas virtuais na AWS (m3.large: 2 vCPUs, 7,5
  GiB), com até 10 núcleos, e um conjunto sintético de 1 milhão de pontos em cinco agrupamentos
  (gerador do scikit-learn). Para a correção, o resultado foi comparado, numa versão menor dos dados,
  com o DBSCAN do scikit-learn, com saída idêntica (p. 538). Quanto à escalabilidade, o tempo total
  diminui com o número de núcleos (Fig. 4, em minutos, de 2 a 10 núcleos), e a computação domina a
  comunicação, com percentual constante (Fig. 5). O tempo cresce com ε (porque as partições crescem) e
  não é afetado por MaxPoints quando ε é grande (p. 539). Todos os resultados são curvas, sem valores
  numéricos no texto.
- Trabalho futuro (p. 539): remover o limite de memória por partição (permitir descarga parcial para
  disco) e melhorar o particionamento para dados muito enviesados e para mais de 2 dimensões.
- Inconsistências internas resolvidas:
  1. A lista de definições (p. 533) traz "noise point: p é um *border point* se não pertence a nenhum
     agrupamento", que contradiz a definição de ponto de borda dada na linha anterior (pertence a um
     agrupamento e não é núcleo). Adotado: ruído é o ponto que não pertence a nenhum agrupamento,
     como no restante do artigo e em Ester et al.
  2. A conclusão afirma que se "provou experimentalmente" que o RDD-DBSCAN escala com o número de
     nós, mas o único dado é a curva de tempo por núcleos de um conjunto de 1 milhão de pontos em até
     10 núcleos (Fig. 4), sem o speedup numérico. Adotado: o experimento mostra redução do tempo, e
     não linearidade medida.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): particionamento BSP por contagem de pontos, margem ε e
  rotulagem global por aliases, além das restrições do método (2-D, memória por partição).
- Relatório §4: um exemplo de avaliação fraca (um conjunto, 10 núcleos, curvas sem valores).

**Aplicabilidade ao nosso caso:** média a baixa. O algoritmo é uma adaptação do MR-DBSCAN a Spark,
restrita a 2-D, com avaliação pequena. As restrições declaradas (2-D, memória, dados enviesados)
valem como alerta. Descritivo.

**Dado/afirmação específica a usar:**
> "RDD-DBSCAN overcomes the scalability limitations of the traditional DBSCAN algorithm by operating in a fully distributed fashion." (p. 531).

> "Describing a modified DBSCAN algorithm that takes full advantage of Apache Spark's parallel capabilities and obtains the same results as the original algorithm." (p. 532).

> "Our algorithm builds on the algorithm presented in [9], which parallelized DBSCAN using MapReduce." (p. 534).

> "One important restriction of RDD-DBSCAN is that it assumes that the data points to be clustered can be represented in two dimensions." (p. 534).

> "Also, in RDD-DBSCAN we define cost as the number of points contained by each partition." (p. 535).

> "Note that the section that finds the globally unique identifiers is not parallelized." (p. 537).

> "The data set consisted of one million entries, organized into five different clusters with five different centers." (p. 538).

> "The output of both executions of the algorithm was identical, so we were able to verify that RDD-DBSCAN was correct." (p. 538).

> "Our implementation of RDD-DBSCAN was evaluated using a cluster of five virtual machines running inside the Amazon Web Services environment." (p. 538).

> "The first important result is that, as the size of ϵ increases, so does the execution time of the algorithm." (p. 539).

> "The main limitation of RDD-DBSCAN is the need to load the complete data set for a given partition into memory." (p. 539).

**Limitações ou ressalvas:**
- A avaliação é mínima: um conjunto sintético (cinco grupos), 5 VMs de 2 vCPUs e a validação de
  correção numa versão menor dos dados, sem medida de speedup e sem comparação com outros paralelos.
- O algoritmo é só 2-D, e as partições são limitadas pela memória do menor nó. Para ε grande, as
  partições podem exceder a memória (p. 538).
- A regra de aliases (um ponto com mais de um rótulo implica o mesmo agrupamento) não distingue ponto
  de borda de ponto núcleo. Um ponto de borda alcançável de dois agrupamentos distintos não deveria
  uni-los (Ester et al.; Schubert et al., 19:4; He et al., 2014, Teorema dos pontos de fusão). É uma
  observação nossa, não testada, e a validação do artigo é por igualdade de saída numa amostra.
- A rotulagem global é sequencial, e o particionamento por contagem de pontos pode desbalancear dados
  enviesados (os próprios autores).
- Divergência com o referencial original: ele dizia que o método "particiona o espaço em uma grade e
  processa cada célula de forma independente", mas o artigo usa BSP por contagem de pontos, e a
  palavra "grid" não aparece no PDF. O referencial também dizia que é uma "alternativa mais citada
  que o Han et al."; o artigo de Han et al. (2016) é posterior e não cita o de Cordova & Moh, e
  vice-versa.

---

## Revisão de consistência do slot DB3

*(22/10/2026.)*

- Sete estratégias de particionamento para o mesmo problema. Xu et al. (1999) distribuem os dados pela
  curva de Hilbert com a dR\*-tree. Götz et al. (2015) usam uma grade de lado ε com heurística de
  custo. He et al. (2011) usam partição por *grid file* com carga estimada pela contagem de pontos, e
  He et al. (2014) trocam a contagem por um modelo de custo (CBP). Dai & Lin (2012) escolhem a fatia
  de menor número de pontos (PRBP), Cordova & Moh (2015) cortam por contagem de pontos (BSP), e Han et
  al. (2016) não particionam espacialmente (faixas de índices). O custo de uma partição é sempre uma
  estimativa (número de pontos ou modelo de custo), e dois artigos admitem desbalanceamento com dados
  enviesados (Cordova & Moh; Han et al.). No relatório, convém tratar o particionamento como uma
  decisão de projeto com consequência medida (equilíbrio de carga contra pontos duplicados) e não
  como detalhe.
- Seis formas de fusão dos resultados parciais aparecem: candidatos de ligação no mestre (Xu et al.),
  regras de fusão após troca de halos (Götz et al.), conjuntos de pontos de fusão com teorema de
  corretude (He et al., 2011 e 2014), lista de fusão por interseção de rótulos (Dai & Lin),
  rótulos-alias com grafo de componentes (Cordova & Moh) e fusão sequencial no *driver* por SEEDs (Han
  et al.). Em quatro delas há uma etapa de fusão sequencial ou centralizada (Han et al.; Cordova &
  Moh; mapeamento global em uma só *thread* em He et al., 2011; um único *reducer* em He et al.,
  2014), e é esse o ponto que Han et al. mostram degradar o speedup total (5,6 contra 10,2 em 32
  núcleos).
- O tratamento correto da borda entre duas partições exige distinguir ponto núcleo de ponto de borda
  (He et al.; Götz et al.). Dai & Lin (um ponto é núcleo se for núcleo em alguma partição) e Cordova &
  Moh (qualquer ponto não ruído com mais de um rótulo une os agrupamentos) usam regras que, na nossa
  leitura, podem unir agrupamentos ligados só por um ponto de borda. Nenhum dos dois demonstra o
  contrário, e a validação de Cordova & Moh é por igualdade de saída numa amostra. É uma observação
  nossa, não testada.
- Sobre a exatidão do resultado, Xu (Lema 10), He (2011, 2014; teoremas de fusão), Dai e Cordova
  afirmam equivalência com o DBSCAN sequencial. A prova de Xu et al. é qualificada como "óbvia" a
  partir de lemas anteriores, e Dai e Cordova não provam. Götz et al. não a afirmam como tese. Han et
  al. fazem a afirmação para os conjuntos pequenos, mas usam poda e filtragem de agrupamentos pequenos
  no de 1 milhão. O relatório deve declarar se compara a saída paralela com a sequencial e em que
  sentido (igualdade de partições, pontos de borda).
- As linhas de base do speedup não são as mesmas: Xu et al. (1 computador; valores só em figuras), He
  et al. (2014; base de 4 tarefas), Han et al. (só executores; base de 1 milhão não reconstruível),
  Götz et al. (o mesmo programa em 1 núcleo; 460,0 em 768 núcleos) e Dai & Lin (tempo total até 7
  nós). Os valores não são comparáveis entre si e só valem dentro de cada artigo, com a base
  declarada.
- Cordova & Moh se restringem a 2-D, Han et al. usam d = 10, e He et al. e Dai & Lin usam GPS 2-D e
  sintéticos. Todos usam clusters de 5 a 128 máquinas ou um supercomputador (JUDGE, Edison), e nenhum
  roda em uma só máquina com *threads* Java. Os resultados servem como evidência de fenômenos (custo
  da fusão, desbalanceamento, pontos duplicados) e não como valores esperados para o nosso ambiente.
- As inconsistências internas resolvidas estão no §9: Xu et al. ("10 MB LAN" contra 10 Mbit/s), Götz
  et al. (Def. 1 "<ε" contra "≤ε"; "ordem de grandeza"), He et al. 2011 ("um mês" contra "dois
  anos"; "≤ MinPts"), He et al. 2014 ("totalmente paralelizado" contra reducer único; "ordem de
  grandeza"; CBP "melhor em todos" contra Tabela 5), Dai & Lin (nt5,800k repetido; "Figura 2(b)"),
  Han et al. (speedups de executores; igualdade ao serial; "mais de 10"; base não reconstruível) e
  Cordova & Moh (definição de ruído; "provado experimentalmente").

---

## Slot DB4: DBSCAN em GPU (contraste com o nosso modelo de memória compartilhada em CPU)

*(2 fichas, 22/10/2026: Böhm et al. 2009 e Andrade et al. 2013. Lidos por inteiro.)*

### [DB4] Böhm, Noll, Plant & Wackersreuther, 2009: Agrupamento baseado em densidade usando processadores gráficos (CUDA-DClust)

**Arquivo:** `BOHM_2009_density_based_clustering_gpu.pdf`

**Referência completa:**
Böhm, C.; Noll, R.; Plant, C.; Wackersreuther, B. Density-based clustering using graphics processors. In:
**Proceedings of the 18th ACM Conference on Information and Knowledge Management (CIKM '09)**, p. 661–670,
2009. DOI: 10.1145/1645953.1646038.

**Chave BibTeX:** `bohm2009density`

**Tipo:** Artigo de conferência (CIKM 2009), revisado por pares.

**Versão lida:** PDF da ACM (10 páginas), com paginação impressa 661–670 conferida (página impressa =
página do PDF + 660).

**Origem:** trazido por Sofia (commit `e6c8608`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–8; pseudocódigo dos
*kernels*, Tabela 1 e Figuras 7–8 percorridos, com os valores de tempo e de speedup só em gráficos).
Citações literais conferidas com o texto do PDF.

**O que o artigo diz** (p. 661–670):
- Contribuição (p. 661–662): o **CUDA-DClust**, um DBSCAN massivamente paralelo em GPU (CUDA), e o
  **CUDA-DClust\***, que acrescenta um índice simples para GPU. Os autores declaram resultado
  equivalente ao do DBSCAN e speedup "tipicamente de uma ordem de grandeza".
- Ideia central, as cadeias (*chains*) (§5, p. 664–667): em vez de uma única expansão de agrupamento
  com uma lista de sementes, fazem muitas expansões simultâneas a partir de pontos iniciais
  diferentes, cada uma com um ID de cadeia (um agrupamento provisório, sem exigir maximalidade). Cada
  cadeia é tratada por um grupo de *threads* (um grupo por cadeia, com uma *thread* por candidato a
  vizinho), com uma lista de sementes por cadeia na memória do dispositivo (1.024 pontos por lista;
  em caso de estouro, os pontos recebem o ID mas não entram na lista).
- Colisões e fusão (§5.2): quando duas cadeias alcançam o mesmo ponto, há uma colisão (as duas
  pertencem ao mesmo agrupamento). As colisões são registradas numa matriz booleana triangular
  superior p × p (p é o número de cadeias simultâneas), e a união final é feita no CPU. A
  sincronização usa operações atômicas (`atomicCAS`, `atomicInc`). Os vizinhos encontrados antes de
  confirmar que o ponto é núcleo ficam num *buffer* de quarentena de até MinPts entradas.
- Balanceamento de carga (§5.4): quando uma cadeia termina, é substituída por uma nova, e quando uma
  lista de sementes cresce demais, a cadeia é dividida. O programa principal no CPU é um laço de três
  *kernels* (expansão, novas sementes, recarga de sementes) até processar todos os pontos.
- Índice (§6, p. 666–667): os *kernels* não permitem recursão, então o índice precisa ser muito
  simples. É um diretório de níveis constantes (nos experimentos, 3 níveis com *fanout* 16), construído
  no CPU ordenando os dados pela 1ª dimensão, partindo em quantis, ordenando cada parte pela 2ª e
  assim por diante. Os autores avisam que o índice só é seletivo se as primeiras dimensões forem
  seletivas, e sugerem PCA antes.
- Experimentos (§7, p. 668–669). A máquina é um Intel Core 2 Duo E4500 de 2,2 GHz com 2 GB e uma
  GeForce GTX 280 (240 processadores, 1 GB), em C++. O *benchmark* é um DBSCAN de uma só *thread* no
  CPU, com o mesmo índice nos experimentos com índice. Os dados são sintéticos, com 20 gaussianas
  aleatórias, ε = 0,05 e MinPts = 4 por padrão. O CUDA-DClust tem speedup de 10 (30 mil pontos) a 15
  (1 milhão) contra o DBSCAN sem índice (40 minutos na GPU contra nove horas no CPU). O
  CUDA-DClust\* tem speedup de 3,5 (30 mil) a quase 15 (2 milhões), "garantido" ≥ 10 acima de 250 mil
  pontos. Variando MinPts de 4 a 2.048, o speedup fica entre 10 e 5 até MinPts = 512 e depois cai,
  chegando a 2 com MinPts grande. O efeito de ε é desprezível (speedup entre 9 e 10), a
  dimensionalidade de 4 a 64 dá speedups de 7 a 12, e a construção do índice custa menos de 5% do
  tempo (Tabela 1: de 4,5% em 65 mil pontos a 0,5% em 2 milhões, com 3,6 s contra 681,8 s no maior).
- Inconsistências internas resolvidas, adotando o que as tabelas e o texto de resultados mostram:
  1. O resumo e a introdução dizem "tipicamente uma ordem de grandeza", mas a medição dá 3,5 a ≈ 15
     para o CUDA-DClust\* (a ordem de grandeza só é "garantida" acima de 250 mil pontos) e 2 com
     MinPts muito grande. Adotado: a faixa medida (3,5–15), e "≥ 10" só para n > 250 mil.
  2. A legenda da Figura 7 atribui a dimensionalidade à "Figura 7(c)" (p. 669), mas a 7(c) é o
     impacto de MinPts e a dimensionalidade é a 7(e). Adotado: 7(e).
  3. O artigo afirma que o resultado é "garantido equivalente" (resumo) e que "pode ser provado
     idêntico" (introdução), mas não traz a prova no texto, e a descrição do tratamento das colisões
     não distingue ponto núcleo de ponto de borda (observação nossa; comparar com Schubert et al.
     19:4 e Wang et al. p. 2556). Fica registrado como afirmação não demonstrada.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): a ideia de múltiplas expansões simultâneas (cadeias) com
  registro de colisões como alternativa à fila de sementes única, e a necessidade de lidar com a
  ordem e a colisão de agrupamentos em paralelo.
- Relatório §4 (Análise de Escalabilidade): a influência de MinPts, ε e dimensão sobre o speedup, e a
  fração do tempo gasta no índice.

**Aplicabilidade ao nosso caso:** baixa a média. O modelo (GPU/CUDA, milhares de *threads*, memória do
dispositivo) difere do nosso (Java, CPU, poucos núcleos). Serve de contraste e como evidência de que
o speedup depende do índice e dos parâmetros. Descritivo.

**Dado/afirmação específica a usar:**
> "While the result of this algorithm is guaranteed to be equivalent to that of DBSCAN, we demonstrate a high speed-up, particularly in combination with a novel index structure for use in GPUs." (p. 661).

> "Both algorithms outperform their sequential counterpart by typically one order of magnitude, while the clustering results can be proven to be identical." (p. 662).

> "The result of DBSCAN is determinate." (p. 665).

> "A collision matrix C is an upper triangular boolean matrix of size (p × p), where p is the number of chains that are expanded simultaneously." (p. 665).

> "We can say, that Q is taken under quarantine until the core status of P is really known." (p. 666).

> "In our experiments, we used 1,024 points per seed list." (p. 667).

> "As a benchmark we apply a single-threaded implementation of DBSCAN on the CPU, that uses the same index structure as CUDA-DClust for all experiments with index support." (p. 668).

> "Note that the calculation on GPU takes 40 minutes, compared to nine hours on CPU." (p. 668).

> "In this evaluations the speedup ranges from 3.5 for 30k points to almost 15 for 2m points." (p. 668).

> "For 4 ≤MinPts ≤512 the speedup factor remains relatively stable between 10 and 5." (p. 668).

> "Evidently, the impact of ϵ is negligible, as the range of the corresponding speedup factor is almost stable between 9 and 10." (p. 668).

> "All experiments indicate that the time for building the index structure is negligible and independent of the parameter-setting by a fraction of lower than 5%." (p. 668).

**Limitações ou ressalvas:**
- O *benchmark* é de uma só *thread* num Core 2 Duo (2 núcleos) de 2009. O speedup sobre um CPU com
  várias *threads* seria menor (observação nossa).
- Os dados são só sintéticos (20 gaussianas), com ε = 0,05 e MinPts = 4. O speedup mínimo (3,5) e a
  queda com MinPts grande mostram forte dependência dos parâmetros.
- A equivalência com o DBSCAN não é demonstrada no texto, e a afirmação "o resultado do DBSCAN é
  determinístico" (p. 665) ignora a dependência de ordem dos pontos de borda (Schubert et al., 2017).
- Há limites de hardware: lista de sementes de 1.024 pontos, memória do dispositivo de 1 GB e índice
  de níveis fixos que exige dimensões iniciais seletivas.
- Divergência com o referencial original: ele citava "GPU/CUDA com 'speedup' de uma ordem de
  grandeza". O artigo diz isso no resumo, mas a medição varia de 3,5 a ≈ 15 e depende de n, MinPts e
  índice.

### [DB4] Andrade, Ramos, Madeira, Sachetto, Ferreira & Rocha, 2013: G-DBSCAN, um algoritmo de agrupamento baseado em densidade acelerado por GPU

**Arquivo:** `ANDRADE_2013_g_dbscan_gpu_accelerated.pdf`

**Referência completa:**
Andrade, G.; Ramos, G.; Madeira, D.; Sachetto, R.; Ferreira, R.; Rocha, L. G-DBSCAN: a GPU accelerated
algorithm for density-based clustering. **Procedia Computer Science**, v. 18, p. 369–378, 2013
(International Conference on Computational Science, ICCS 2013). DOI: 10.1016/j.procs.2013.05.200.

**Chave BibTeX:** `andrade2013gdbscan`

**Tipo:** Artigo de conferência publicado em periódico de anais (ICCS 2013), revisado por pares.

**Versão lida:** PDF do Procedia (10 páginas), com paginação impressa 369–378 conferida (página
impressa = página do PDF + 368).

**Origem:** trazido por Sofia (commit `e6c8608`, 21/10/2026). Dados conferidos no PDF e no Crossref
(22/10/2026).

**Estado da leitura:** texto lido por inteiro em 22/10/2026 (resumo, seções 1–5, algoritmos; figuras
de tempo e speedup lidas pelos valores citados no texto). Citações literais conferidas com o texto do
PDF.

**O que o artigo diz** (p. 369–377):
- Contribuição (p. 369–370): o **G-DBSCAN**, que indexa os dados num grafo G(V, E), com os objetos
  como vértices e arestas entre objetos a distância ≤ R, e executa o DBSCAN em duas etapas
  paralelizáveis em GPU: construção do grafo e busca em largura (BFS) a partir dos núcleos. Usa lista
  de adjacência compacta (vetores Va e Ea) por causa da limitação de memória da GPU.
- Algoritmo (§3, p. 370–374): a construção do grafo compara cada par de objetos, com custo O(V²), e a
  BFS custa O(V + E). O custo total é O(V²), e os autores escolhem essa forma em função das
  oportunidades de paralelização e não do melhor limite. Na GPU, uma *thread* por vértice calcula o
  grau (sem dependência entre as *threads*, "embaraçosamente paralelo"), um *scan* exclusivo
  (biblioteca `thrust`) calcula os índices iniciais das listas e outra passagem preenche as arestas.
  A BFS é paralela por nível, com vetores booleanos de fronteira e de visitados e uma *thread* por
  vértice.
- Experimentos (§4, p. 374–377). A máquina é um Intel Xeon de 2,40 GHz com uma Tesla M2050 (448
  núcleos CUDA, 3 GB), com C e CUDA C. Os dados são de 5.000 a 700.000 pontos 2-D com 20 gaussianas
  aleatórias, MinPts = 4 e raio = 0,05, configuração copiada de Böhm et al. para comparar. O speedup
  da construção do grafo cresce até N ≈ 200 mil e estabiliza em ≈ 82× em torno de N = 600 mil. O da
  BFS estabiliza em ≈ 21× (172,2 s no CPU contra 8,1 s na GPU, no maior conjunto). O speedup total
  máximo é 112× (9.261,1 s no CPU contra 82,9 s na GPU, com 700 mil objetos), contra 15× do
  CUDA-DClust (Böhm et al.).
- Linha de base (§4.4, p. 376): comparam a sua própria versão sequencial em CPU, O(V²), e argumentam
  que o tempo dela é próximo ao da versão sequencial de Böhm et al., o que tornaria a comparação de
  speedups "justa". Explicam a diferença pela constante multiplicativa e pela maior paralelização das
  duas etapas.
- Trabalho futuro (p. 377): avaliar dados reais, grandes volumes e várias dimensões.
- Inconsistências internas resolvidas, adotando o que as medidas mostram:
  1. O texto diz que a complexidade da construção do grafo "pode ser reduzida de O(V²) para O(V)"
     (p. 373). Isso vale para o tempo por *thread* (a profundidade), com V *threads* fazendo V
     comparações cada, e não para o trabalho total, que continua O(V²) (o próprio artigo declara
     O(V²) total). Adotado: O(V²) de trabalho, com a redução para O(V) só no caminho crítico
     paralelo.
  2. O artigo atribui ao CUDA-DClust uma complexidade O(V log V) (p. 376), mas Böhm et al. descrevem
     o custo como "n vezes o custo da consulta de vizinhança" (linear sem índice, sublinear com
     índice), sem declarar O(n log n) (p. 665). Adotado: a descrição de Böhm et al., e a atribuição
     de Andrade et al. fica como interpretação deles.
  3. A comparação "justa" de speedups (112× contra 15×) mistura linhas de base diferentes: a deles é
     O(V²) e os 9.261,1 s para 700 mil pontos são ≈ 2,6 h num Xeon de 2,40 GHz, e a de Böhm et al. é
     um DBSCAN com ou sem índice num Core 2 Duo, com ≈ 9 h para 1 milhão sem índice, em hardware
     diferente. Adotado: os dois valores só valem dentro de cada artigo e não são comparáveis
     diretamente.

**Onde usar no trabalho:**
- Relatório §2 (Algoritmo 2: DBSCAN): uma formulação alternativa do DBSCAN em duas fases, com grafo
  de vizinhança e componentes conexos por BFS (comparar com Wang et al., 2020, e Patwary et al.,
  2012).
- Relatório §4 (Análise de Escalabilidade): a importância de declarar a linha de base e a
  complexidade dela. Um speedup de 112× sobre um sequencial O(V²) é compatível com tempos absolutos
  de ordem parecida com a de implementações indexadas.

**Aplicabilidade ao nosso caso:** média a baixa. A divisão em "construir a vizinhança" e "achar
componentes" é um padrão que pode ser pensado em CPU, mas o ganho depende de GPU. Os pontos 2-D, de
até 700 mil, estão na faixa do nosso interesse (até 150 mil pontos). Descritivo.

**Dado/afirmação específica a usar:**
> "In our evaluation we show that the G-DBSCAN using GPU, can be over 100x faster than its sequential version using CPU." (p. 369).

> "As our main goal is the parallelization of this algorithm using GPUs, which have a major memory limitation, we chose to represent the graph using a compact adjacency list." (p. 371).

> "So, the total complexity of our approach is O(V2)." (p. 373).

> "Thus, the computational complexity can be reduced from O(V2) to O(V)." (p. 373).

> "In our implementation we assign one thread to each vertex." (p. 374).

> "The choice of this configuration was based on that presented in [5], in order to compare the algorithm proposed in that paper with our proposal." (p. 375).

> "The speedup stabilizes around 21× with inputs greater than 600, 000 objects." (p. 375).

> "Still, we have an impressive reduction of the execution time for the BFS, dropping the time from 172.2s to 8.1s comparing our CPU and GPU implementations, in the largest input instance." (p. 376).

> "However, the time of our CPU implementation and the one presented in [5] are relatively close, making the comparison of our speedups with [5] fair." (p. 376).

> "The maximum speedup achieved was 112x, decreasing the execution time from 9, 261.1s on CPU to 82.9s on GPU, with 700, 000 objects." (p. 376).

> "As future work, we aim to evaluate the G-DBSCAN on real data scenarios, where the volume is very large and the data is represented by several dimensions." (p. 377).

**Limitações ou ressalvas:**
- O sequencial de referência é O(V²), sem índice espacial. O speedup de 112× é sobre essa base, e o
  trabalho total do algoritmo continua quadrático. Para n = 150.000, o termo V² é ≈ 2,3 × 10¹⁰
  comparações de distância (cálculo nosso).
- O grafo guarda O(V + E) posições. Em regiões densas, E pode ser da ordem de V² e estourar a
  memória da GPU de 3 GB (observação nossa; o artigo não discute).
- Os dados são só sintéticos, 2-D e com 20 gaussianas, com os parâmetros de Böhm et al. Não há
  avaliação com dados reais nem de dimensão alta (trabalho futuro dos autores).
- O artigo não verifica a igualdade de saída com o DBSCAN sequencial. Afirma que a BFS rotula os
  vizinhos alcançáveis do núcleo, sem tratar a ordem dos pontos de borda.
- Divergência com o referencial original: ele citava "mais de 100×", o que se confirma (máximo de
  112× no maior conjunto, 700 mil pontos).

---

## Revisão de consistência do slot DB4

*(22/10/2026.)*

- Há dois caminhos em GPU. Böhm et al. mantêm a expansão de agrupamentos do DBSCAN, paralelizada por
  cadeias com matriz de colisões e índice simples. Andrade et al. mudam a formulação (grafo de
  vizinhança e BFS), de modo semelhante à reformulação por grafo de células de Wang et al. (2020) em
  CPU, mas sem índice, ficando em O(V²).
- Os 15× e os 112× não são comparáveis. As bases sequenciais, o hardware (GTX 280 contra Tesla M2050;
  Core 2 Duo contra Xeon) e os intervalos de n (até 2 milhões contra 700 mil) são diferentes, e o
  argumento de "comparação justa" de Andrade et al. repousa só na proximidade dos tempos
  sequenciais. O relatório deve citar cada valor com a sua base.
- Böhm et al. mostram que o speedup depende de n, de MinPts e do índice (3,5 a 15, com queda a 2 com
  MinPts muito grande), e que ε quase não importa. Andrade et al. usam um único par (MinPts = 4,
  R = 0,05).
- Böhm et al. afirmam que o resultado do DBSCAN é determinístico (p. 665), o que contradiz a
  dependência de ordem dos pontos de borda registrada por Schubert et al. (DB1) e por Patwary et al.
  (DB2). Andrade et al. não discutem o assunto.
- Os dois são de GPU e, portanto, não são o nosso modelo (Java em CPU, uma máquina), mas mostram a
  mesma lição dos outros slots: o ganho depende da linha de base declarada e do índice espacial.
- As inconsistências internas resolvidas estão no §9: Böhm et al. (ordem de grandeza contra 3,5–15;
  legenda da Figura 7; equivalência não demonstrada) e Andrade et al. (O(V²) contra O(V); atribuição
  de O(V log V) a Böhm et al.; comparação de speedups).
