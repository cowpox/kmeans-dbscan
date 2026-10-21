# Referencial Teórico — k-means e DBSCAN

Levantamento dos artigos principais sobre k-means e DBSCAN: as formulações originais (sequenciais) e os trabalhos que propõem adaptações para execução paralela/distribuída. Serve de base para a seção de fundamentação teórica do relatório e para justificar as decisões de particionamento tomadas na implementação.

---

## 1. k-means

### 1.1 Formulação original

**MacQueen, J. (1967).** "Some Methods for Classification and Analysis of Multivariate Observations." *Proceedings of the 5th Berkeley Symposium on Mathematical Statistics and Probability*, vol. 1, pp. 281–297.
Introduz o termo "k-means" e o algoritmo de particionamento de pontos em k grupos minimizando a variância intra-cluster. É a referência canônica para citar a origem do método.

**Lloyd, S. P. (1982).** "Least Squares Quantization in PCM." *IEEE Transactions on Information Theory*, 28(2), 129–137. (Relatório técnico original da Bell Labs de 1957, publicado formalmente em 1982.)
Descreve o algoritmo iterativo de duas etapas (atribuição de pontos ao centróide mais próximo + recálculo dos centróides) que é, na prática, o "k-means" que qualquer implementação atual roda. Citar junto com MacQueen (1967) ao explicar o algoritmo base que será paralelizado.

**Arthur, D., & Vassilvitskii, S. (2007).** "k-means++: The Advantages of Careful Seeding." *Proceedings of the 18th ACM-SIAM Symposium on Discrete Algorithms (SODA)*, pp. 1027–1035.
Propõe uma inicialização probabilística dos centróides que melhora a qualidade e a velocidade de convergência em relação à inicialização aleatória. Relevante se o grupo decidir usar k-means++ em vez de inicialização aleatória simples — vale declarar no relatório qual estratégia de inicialização foi usada.

### 1.2 Paralelização e distribuição

**Dhillon, I. S., & Modha, D. S. (2000).** "A Data-Clustering Algorithm on Distributed Memory Multiprocessors." In *Large-Scale Parallel Data Mining*, Lecture Notes in Computer Science, vol. 1759, pp. 245–260.
Um dos primeiros trabalhos a formalizar o k-means paralelo em memória distribuída: cada processo mantém uma partição dos dados, calcula somas parciais de pontos/centróides localmente e um passo de redução global combina os resultados a cada iteração. É essencialmente o modelo "map (atribuição local) + reduce (soma global dos centróides)" que serve de base conceitual para qualquer implementação paralela de k-means, seja com `ExecutorService` em Java, seja com RDDs em Spark.

**Zhao, W., Ma, H., & He, Q. (2009).** "Parallel K-Means Clustering Based on MapReduce." *Proceedings of the 1st International Conference on Cloud Computing (CloudCom)*, pp. 674–679.
Implementa k-means no paradigma MapReduce (Hadoop): a fase *map* calcula a distância de cada ponto aos centróides atuais e emite (centróide mais próximo, ponto); a fase *reduce* recalcula os novos centróides. Útil como referência direta de como mapear o algoritmo original para um modelo de particionamento de dados por partições/workers.

**Bahmani, B., Moseley, B., Vattani, A., Kumar, R., & Vassilvitskii, S. (2012).** "Scalable K-Means++." *Proceedings of the VLDB Endowment*, 5(7), 622–633.
Propõe o k-means‖ ("k-means parallel"), uma versão distribuída da inicialização k-means++ que reduz o número de passes sequenciais sobre os dados necessários para uma boa inicialização em larga escala. É a técnica de inicialização usada internamente pelo MLlib do Spark — citar se a implementação (Java ou Spark) adotar uma inicialização inspirada nela.

**Meng, X. et al. (2016).** "MLlib: Machine Learning in Apache Spark." *Journal of Machine Learning Research*, 17(34), 1–7.
Descreve a arquitetura da biblioteca de aprendizado de máquina do Spark, incluindo a implementação distribuída de k-means sobre RDDs (particionamento dos dados, broadcast dos centróides a cada iteração, agregação distribuída). Referência útil caso a implementação seja em Scala/Spark, mesmo implementando o algoritmo manualmente em vez de usar o `MLlib.KMeans` pronto.

---

## 2. DBSCAN

### 2.1 Formulação original

**Ester, M., Kriegel, H.-P., Sander, J., & Xu, X. (1996).** "A Density-Based Algorithm for Discovering Clusters in Large Spatial Databases with Noise." *Proceedings of the 2nd International Conference on Knowledge Discovery and Data Mining (KDD-96)*, pp. 226–231.
Artigo original do DBSCAN. Define os conceitos de ε-vizinhança, ponto núcleo (*core point*), ponto de borda e ruído, e o algoritmo sequencial baseado em expansão de clusters a partir de pontos núcleo usando *region queries*. Referência obrigatória para a fundamentação teórica do algoritmo.

### 2.2 Paralelização e distribuição

**Xu, X., Jäger, J., & Kriegel, H.-P. (1999).** "A Fast Parallel Clustering Algorithm for Large Spatial Databases." *Data Mining and Knowledge Discovery*, 3(3), 263–290.
Propõe o PDBSCAN, uma das primeiras versões paralelas do DBSCAN, usando uma estrutura de índice distribuída (dR*-tree) replicada entre os nós e um esquema mestre/escravo para distribuir as *region queries*. Relevante para discutir estratégias de particionamento espacial dos dados (não só particionamento aleatório).

**Patwary, M. M. A., Palsetia, D., Agrawal, A., Liao, W., Manne, F., & Choudhary, A. (2012).** "A New Scalable Parallel DBSCAN Algorithm Using the Disjoint-Set Data Structure." *Proceedings of the International Conference for High Performance Computing, Networking, Storage and Analysis (SC '12)*, article 62.
Usa uma estrutura de conjuntos disjuntos (*union-find*) para unificar com eficiência clusters que se espalham por diferentes partições/threads, evitando sincronização excessiva. É a referência mais direta para justificar a decisão de implementação quando o DBSCAN é paralelizado por particionamento de dados com `ExecutorService`/`ForkJoin`, já que o problema central (mesclar clusters nas fronteiras entre partições) é exatamente esse.

**He, Y., Tan, H., Luo, W., Mao, H., Ma, D., Feng, S., & Fan, J. (2011).** "MR-DBSCAN: An Efficient Parallel Density-Based Clustering Algorithm Using MapReduce." *Proceedings of the 17th IEEE International Conference on Parallel and Distributed Systems (ICPADS)*, pp. 473–480.
Implementação de DBSCAN sobre MapReduce com particionamento espacial balanceado dos dados (via amostragem e KD-tree) e uma segunda fase que funde clusters entre partições vizinhas — mesmo problema de "fronteira de partição" tratado de outra forma.

**Dai, B.-R., & Lin, I.-C. (2012).** "Efficient Map/Reduce-Based DBSCAN Algorithm with Optimized Data Partitioning." *Proceedings of the IEEE 5th International Conference on Cloud Computing (CLOUD)*, pp. 59–66.
Foca especificamente em como particionar o espaço de dados para equilibrar carga entre os nós/reducers e reduzir a quantidade de pontos duplicados entre partições vizinhas — referência útil para justificar a escolha do esquema de particionamento (grade, k-d tree, etc.) no relatório.

**Han, D., Agrawal, A., Liao, W.-K., & Choudhary, A. (2018).** "A Novel Scalable DBSCAN Algorithm with Spark." *Proceedings of the 2018 IEEE International Parallel and Distributed Processing Symposium Workshops (IPDPSW)*, pp. 271–280.
Implementação de DBSCAN em Spark (RDDs), com particionamento espacial e redistribuição de pontos de fronteira. Referência direta caso a implementação do grupo seja em Scala/Spark.

**Cordova, I., & Moh, T.-S. (2015).** "DBSCAN on Resilient Distributed Datasets." *Proceedings of the 2015 International Conference on High Performance Computing & Simulation (HPCS)*, pp. 531–540. IEEE.
Conhecido como RDD-DBSCAN. Implementação sobre RDDs do Spark que particiona o espaço em uma grade e processa cada célula de forma independente antes de uma fase de fusão das bordas. Alternativa mais citada que o Han et al. (2018) quando o assunto é "DBSCAN em Spark" — vale citar os dois ou escolher o que mais se aproxima da estratégia adotada.

**Götz, M., Bodenstein, C., & Riedel, M. (2015).** "HPDBSCAN: Highly Parallel DBSCAN." *Proceedings of the Workshop on Machine Learning in High-Performance Computing Environments (MLHPC), colocado com a SC15*, article 2. ACM.
Particiona o espaço em uma grade (grid) com balanceamento de carga entre os workers e paraleliza com um modelo híbrido OpenMP (memória compartilhada, dentro de cada nó) + MPI (memória distribuída, entre nós). É a referência mais próxima de um cenário de HPC "de verdade" com particionamento espacial balanceado — útil para comparar com uma abordagem mais simples de particionamento aleatório.

**Böhm, C., Noll, R., Plant, C., & Wackersreuther, B. (2009).** "Density-Based Clustering Using Graphics Processors." *Proceedings of the 18th ACM Conference on Information and Knowledge Management (CIKM)*, pp. 661–670.
Apresenta o CUDA-DClust, uma das primeiras paralelizações do DBSCAN em GPU. Relevante apenas como contexto comparativo (o grupo não vai implementar em GPU/CUDA), mas útil para contrastar paralelismo em CPU (threads/Spark) com paralelismo em GPU na seção de trabalhos relacionados.

**Andrade, G., Ramos, G., Madeira, D., Sachetto, R., Ferreira, R., & Rocha, L. (2013).** "G-DBSCAN: A GPU Accelerated Algorithm for Density-Based Clustering." *Procedia Computer Science*, 18, 369–378.
Modela o DBSCAN como um problema de construção e busca em grafo, acelerado em GPU. Mesma observação do item anterior: útil como contraponto (CPU vs. GPU) na discussão teórica, não como modelo a seguir na implementação.

**Wang, Y., Gu, Y., & Shun, J. (2020).** "Theoretically-Efficient and Practical Parallel DBSCAN." *Proceedings of the 2020 ACM SIGMOD International Conference on Management of Data (SIGMOD '20)*, pp. 2555–2571.
Trabalho mais recente e citado como estado da arte em DBSCAN paralelo de memória compartilhada (multicore), com garantias teóricas de eficiência e desempenho prático competitivo usando k-d trees para as *range queries*. É a referência mais indicada como ponto de partida teórico se a implementação for em Java com `ExecutorService`/`ForkJoin` (paralelismo de memória compartilhada, não distribuído), já que PDBSCAN/HPDBSCAN/MR-DBSCAN assumem memória distribuída entre múltiplas máquinas.

---

## 3. Como usar este material no relatório

- Ao descrever cada algoritmo na seção "Algoritmos Implementados" do relatório, citar primeiro a formulação original (MacQueen/Lloyd para k-means; Ester et al. para DBSCAN) e, na sequência, o artigo de paralelização que mais se aproxima da estratégia de particionamento efetivamente usada na implementação.
- Para k-means, o ponto central a justificar é como os centróides são agregados entre as threads/partições a cada iteração (padrão map-reduce de Dhillon & Modha / Zhao et al.).
- Para DBSCAN, o ponto central a justificar é como os clusters são mesclados nas fronteiras entre partições — esse é o problema mais citado em todos os artigos de paralelização (Patwary et al., He et al., Dai & Lin, Cordova & Moh) e deve aparecer na discussão de "decisões de projeto" do relatório.
- Atenção à diferença entre paralelismo de **memória compartilhada** (uma máquina, várias threads — o caso do grupo com `ExecutorService`/`ForkJoin`) e paralelismo de **memória distribuída** (várias máquinas — PDBSCAN, HPDBSCAN, MR-DBSCAN, RDD-DBSCAN). Se a implementação for em Java rodando numa única máquina, Patwary et al. (2012) e especialmente Wang, Gu & Shun (2020) são as referências mais diretamente aplicáveis; os trabalhos baseados em MapReduce/Spark/MPI descrevem um cenário diferente (ainda assim úteis para contextualizar, mas não para copiar a estratégia de particionamento).
- Os trabalhos em GPU (Böhm et al., Andrade et al.) servem só de contraste teórico na introdução/trabalhos relacionados — não são aplicáveis à implementação do grupo.
- Esta leitura é trabalho de fundamentação teórica, não faz parte da implementação do algoritmo paralelo em si — portanto não entra na restrição de uso de IA da disciplina (que veda apenas o uso de IA para implementar o algoritmo distribuído/paralelo).
