# Template de fichamento da revisão de literatura

> Processo e molde para transformar um PDF numa ficha verificável e citável. Adaptado do processo
> usado no projeto PASSO/CURA (`docs/revisao-literatura/fichamento_literatura_template.md`), que por
> sua vez vem do trabalho final de Metodologia de Pesquisa (2COP021). Mesma sequência de passos;
> slots, seções do relatório e campos de método adaptados a este trabalho (k-means e DBSCAN
> paralelos em Java).
>
> **Objetivo da revisão:** mapear, sintetizar e analisar criticamente o conhecimento científico já
> existente sobre k-means e DBSCAN (formulação original e paralelização), para fundamentar o
> relatório e evitar redescobrir o que já foi provado.
>
> **Escopo:** o fichamento é fundamentação teórica. Ele descreve o que cada artigo diz; não contém
> código, pseudocódigo nem decisões de implementação, que ficam com as duplas responsáveis.
>
> **Onde as coisas vivem:**
> - `revisao_literatura.md` — slots, fila de leitura, artigos lidos, síntese por tema, posicionamento.
> - `fichamentos.md` — as fichas completas (uma por artigo lido), no molde da §5.
> - `referencias.bib` — entradas BibTeX, só de artigo **lido e fichado**.
> - `artigos/` — os PDFs e o catálogo `artigos/README.md`.

---

## 1. Nomenclatura do arquivo do artigo

**Padrão:** `SOBRENOME_ANO_tema-curto.pdf`

- `SOBRENOME` — sobrenome do 1º autor, maiúsculas, sem acento, um único token.
- `ANO` — ano de publicação. Se o PDF for versão anterior à publicada (autor, arXiv), o ano segue o
  da publicação oficial e o catálogo registra a versão.
- `tema-curto` — 3 a 6 palavras-chave do título, minúsculas, `_` no lugar de espaço, sem acento.
- Mesmo autor e ano: acrescentar `a`/`b` ao ano.

**Renomear depois de commitado:** `git mv antigo.pdf novo.pdf`, nunca apagar e recriar (preserva o
histórico). Foi assim que os PDFs deste repositório foram padronizados em 21/10/2026.

**Ao adicionar um artigo:** salvar em `artigos/`, adicionar a linha no catálogo e mapear o slot em
`revisao_literatura.md` §4 (fila de leitura), antes de ler.

---

## 2. Critérios de qualidade — aplicar antes de aceitar qualquer candidato

1. **Existe?** DOI ativo ou URL que abre o documento real.
2. **É acessível?** Dá para ler o texto integral.
3. **É científico?** Periódico com revisão por pares, anais de conferência indexada, ou *preprint*
   citado como tal (arXiv **não** é peer-reviewed; usar com a ressalva explícita). Não aceitar blog,
   site corporativo, relatório técnico sem revisão.
4. **É recente o suficiente?** Para algoritmos clássicos (MacQueen 1967, Lloyd 1957/1982, Ester
   1996) não há limite: são a referência histórica deliberada. Para paralelização, vale o que
   descreve o modelo de execução usado (memória compartilhada, distribuída, GPU).
5. **Diz o que o título sugere?** Confirmar na leitura; não citar pelo título.

**Regra de ouro:** na dúvida, não citar.

---

## 3. Aviso sobre citações

DOI, título, autor, ano, número de página ou afirmação lembrados de memória **podem estar errados**.
Neste projeto isso já aconteceu: o `referencial_teorico.md` original (agora absorvido pela síntese de
`revisao_literatura.md`) foi escrito sem leitura integral e continha afirmações que os PDFs não
sustentam (p. ex., "coresets" no Ene et al.; páginas erradas do Han et al.). Regras:

- Dado bibliográfico só entra na ficha se estiver no cabeçalho do PDF ou conferido no Crossref/DOI;
  o que veio de outra fonte vai marcado "a conferir".
- Citação literal só entra se for **verificada por busca no texto extraído do PDF** (passo 4 da §4).
- Quando a leitura contradiz uma nota anterior, **a leitura completa vence**; corrigir a nota e
  registrar a correção no changelog de `revisao_literatura.md` §8.
- **Divergência dentro do mesmo artigo** (texto × figura/tabela): vale o dado observado (figura, tabela,
  traços vetoriais da figura), e a divergência é registrada em `revisao_literatura.md` §9 e na ficha,
  no formato "encontrado no artigo X; adotado o valor Y".
- **Conferência por integrante.** Antes de citar um trecho no relatório, um integrante do grupo confere
  a citação no PDF; a ficha registra se isso já foi feito no campo "Estado da leitura". (Regra
  herdada do projeto GCS.)

---

## 4. Leitura — rotina de cada parte

**Não fichar a partir do título, do abstract ou do que "parece razoável" que o artigo diga.**

1. **Extrair** o texto integral do PDF com marcação de página (PyMuPDF ou `pdftotext`), para a pasta
   temporária — nunca para o repositório.
2. **Ler tudo**: resumo, introdução, método, experimentos, conclusão e, nos artigos teóricos, os
   enunciados de teoremas e lemas (as demonstrações podem ser percorridas sem reproduzi-las).
3. **Anotar** dado, número e citação **literais** com a página. Citar a página **impressa** do
   veículo quando o PDF a traz; senão, "p. N do PDF" (e dizer isso na ficha).
4. **Conferir cada citação literal** por busca no texto extraído (normalizando ligaturas,
   hifenização e espaços). Citação que não passa na busca é refeita ou removida.
5. **Separar** o que o artigo afirma do que é inferência nossa; a inferência vai marcada
   "(inferência nossa)".
6. Escrever a ficha, adicionar a entrada BibTeX e atualizar catálogo, fila, lidos e changelog.

---

## 5. O fichamento — molde (uma entrada por artigo)

```markdown
### [SLOT] Sobrenome et al., ANO — Título curto descritivo

**Arquivo:** `SOBRENOME_ANO_tema-curto.pdf`

**Referência completa:**
SOBRENOME, Nome. Título completo. **Veículo**, v. X, n. Y, p. AAA–BBB, ANO. DOI: xxxxx.

**Chave BibTeX:** `sobrenome2024palavra`

**Tipo:** Artigo original / Revisão / Capítulo / Artigo curto (*poster*) / *Preprint* / Clássico.

**Versão lida:** versão oficial, manuscrito do autor, arXiv vN etc., e como a paginação foi citada.

**Origem:** quem trouxe (commit/data) e como os dados foram conferidos.

**Estado da leitura:** quando foi lido, quais seções, e se a conferência por integrante do grupo já foi feita.

**O que o artigo diz** (lido na íntegra, p. X–Y). Achados principais, com número literal:
- Problema e contribuição.
- Modelo de execução: sequencial, memória compartilhada, memória distribuída ou GPU.
- Estratégia de particionamento e de agregação/fusão (quando houver).
- Complexidade ou garantia declarada.
- Experimentos: dataset, tamanho, hardware, métrica e resultado.

**Onde usar no trabalho:**
- Relatório §1 Introdução / §2 Algoritmos Implementados / §3 Setup Experimental /
  §4 Análise de Escalabilidade / §5 Conclusões — motivo específico.

**Aplicabilidade ao nosso caso** (Java, `ExecutorService`/`ForkJoin`, uma máquina, datasets
sintéticos de até 150 mil pontos): alta / média / só contexto — e por quê. Descritivo; não é
recomendação de implementação.

**Dado/afirmação específica a usar:**
> "citação literal" (p. NN) — o que ela sustenta.

**Limitações ou ressalvas:**
- Cenário diferente do nosso, experimento fraco, versão não oficial, divergência com outro artigo
  já fichado (registrar também na revisão de consistência do slot, §7).
```

Preencher **todo** campo, para **todo** artigo, antes de citá-lo em qualquer seção do relatório.

---

## 6. Construir o `.bib` incrementalmente

Ao terminar cada ficha, adicionar a entrada em `referencias.bib` **na hora**. Chave no padrão do
PASSO: sobrenome do 1º autor + ano + uma palavra do título, minúsculas (`arthur2007kmeanspp`).
`keywords = {fichado}` para o que foi lido; o grupo do slot em `note` quando útil. Sem chave
duplicada. Entrada só com dado conferido; o que falta fica em `note = {a conferir: ...}`.

---

## 7. Revisão de consistência por slot

Ao terminar os artigos de um slot, reler as fichas em sequência:

- Os artigos se complementam ou se contradizem? Contradição registrada explicitamente é ponto forte.
- Falta algum ângulo óbvio do slot que nenhum artigo cobriu?
- Há redundância (mesma ideia, mesmo grupo, versão estendida) que dispensaria um deles?
- O que o artigo mais antigo já provou e o mais novo apenas reencontra?

Registrar o resultado em `revisao_literatura.md` §6 (síntese por tema).

---

## 8. Mapear artigos-âncora por seção do relatório

Depois de fichar uma família de algoritmos (k-means, depois DBSCAN), preencher a tabela "Artigos-âncora por seção" em `revisao_literatura.md`
§7, ligando cada seção do `relatorio/template_trabalho_semestre.tex` (na raiz do repositório) aos artigos que a sustentam,
para não reler todas as fichas na hora de escrever.

---

## 9. Checklist antes de considerar um slot concluído

- [ ] Todo artigo do slot tem ficha completa, preenchida **após** leitura integral.
- [ ] Toda ficha tem citação literal com página, **verificada** contra o texto extraído.
- [ ] `referencias.bib` tem uma entrada por artigo fichado, sem chave duplicada.
- [ ] A revisão de consistência do slot (§7) foi feita.
- [ ] `revisao_literatura.md` §6 tem o parágrafo do slot.
- [ ] Alegações antigas do `referencial_teorico.md` sobre esses artigos foram confirmadas ou
      corrigidas, e a correção está no changelog.

---

## 10. Erros comuns a evitar

1. Fichar a partir do título/abstract: o dado-chave costuma estar nos experimentos ou na discussão.
2. Citar página do PDF como se fosse a do veículo (ou o contrário) sem avisar.
3. Atribuir a um artigo o que só um comentário posterior diz dele (p. ex., chamar de "algoritmo de
   Lloyd" algo que o paper de Lloyd não trata).
4. Deixar o `.bib` para o final.
5. Tratar *preprint*/manuscrito do autor como versão oficial sem dizer.
6. Esconder contradição entre artigos do mesmo slot.
7. Renomear PDF apagando e recriando em vez de `git mv`.
8. Citar no relatório um trecho que nenhum integrante conferiu no PDF.
