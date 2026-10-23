# artigos/

PDFs da revisão de literatura sobre k-means e DBSCAN (formulação original e versões paralelas).
**Versionados**, para todo o grupo ter a mesma versão de cada artigo ao clonar.

A leitura e o fichamento estão em [`../`](../): processo em
`fichamento_literatura_template.md`, slots e síntese em `revisao_literatura.md`, fichas em
`fichamentos.md`, bibliografia em `referencias.bib`.

## Convenção de nome

`SOBRENOME_ANO_tema-curto.pdf` — sobrenome do 1º autor em maiúsculas, ano de publicação, 3–6
palavras-chave do tema separadas por `_`, sem espaço nem acento. Padronizada em 21/10/2026 (commit
`2f4599a`). Para renomear um PDF já commitado, usar `git mv` (ver `fichamento_literatura_template.md` §1).

## Catálogo — k-means

Dados bibliográficos **conferidos no cabeçalho do próprio PDF e no Crossref (22/10/2026)**; o que não
foi conferido está marcado "a conferir". "Status": *fichado* = lido na íntegra e com ficha em
`fichamentos.md`; *fila* = ainda não lido.

| Arquivo | Referência | Slot | Versão no PDF / observações | Trazido por | Status |
|---|---|---|---|---|---|
| `MACQUEEN_1967_some_methods_classification_analysis_multivariate_observations.pdf` | MacQueen J. *Some methods for classification and analysis of multivariate observations.* Proc. 5th Berkeley Symp. Math. Stat. Prob., v. 1, p. 281–297, 1967. Sem DOI. | KM1 | Reprodução do artigo publicado, 17 p.; página impressa = página do PDF + 280. | Herik (a301449, 21/10) | fichado (22/10) |
| `LLOYD_1982_least_squares_quantization_in_pcm.pdf` | Lloyd S. P. *Least squares quantization in PCM.* IEEE Trans. Inf. Theory, 28(2):129–137, 1982. DOI: 10.1109/TIT.1982.1056489. | KM1 | PDF digitalizado, 9 p.; a p. 137 traz o início de outro artigo (Newman, *The Hexagon Theorem*). Rascunho de 1957. | Herik (a301449, 21/10) | fichado (22/10) |
| `ARTHUR_2007_k_means_plusplus_advantages_of_careful_seeding.pdf` | Arthur D., Vassilvitskii S. *k-means++: the advantages of careful seeding.* SODA '07, p. 1027–1035, 2007. DOI 10.5555/1283383.1283494 (a conferir). | KM2 | **Manuscrito do autor** (2006), sem veículo nem paginação do SODA; citado por página do PDF. | Herik (a301449, 21/10) | fichado (22/10) |
| `BAHMANI_2012_scalable_k_means_plusplus.pdf` | Bahmani B., Moseley B., Vattani A., Kumar R., Vassilvitskii S. *Scalable k-means++.* PVLDB, 5(7):622–633, 2012. DOI: 10.14778/2180912.2180915. | KM2 | arXiv:1203.6402v1 com rodapé da VLDB Endowment; página impressa = página do PDF + 621. | Herik (a301449, 21/10) | fichado (22/10) |
| `DHILLON_2000_data_clustering_algorithm_distributed_memory_multiprocessors.pdf` | Dhillon I. S., Modha D. S. *A data-clustering algorithm on distributed memory multiprocessors.* In: Zaki M. J., Ho C.-T. (eds.) *Large-Scale Parallel Data Mining*, LNCS 1759, p. 245–260, Springer, 2000. DOI: 10.1007/3-540-46502-2_13. | KM3 | Origem: workshop do KDD'99. O Crossref data de 2002 (publicação online); usamos 2000 (volume impresso). Manuscrito em formato LNCS (numeração própria 1–15); nº do volume LNCS a conferir. | Herik (a301449, 21/10) | fichado (22/10) |
| `ZHAO_2009_parallel_k_means_clustering_based_on_mapreduce.pdf` | Zhao W., Ma H., He Q. *Parallel K-Means Clustering Based on MapReduce.* CloudCom 2009, LNCS 5931, p. 674–679. DOI: 10.1007/978-3-642-10665-1_71. | KM3 | Capítulo do LNCS, 6 p.; página impressa 674–679. | Herik (a301449, 21/10) | fichado (22/10) |
| `ENE_2011_fast_clustering_mapreduce.pdf` | Ene A., Im S., Moseley B. *Fast clustering using MapReduce.* KDD '11, p. 681–689, 2011. DOI: 10.1145/2020408.2020515. | KM3 | **arXiv:1109.1579v1** (20 p., versão estendida; o artigo do KDD tem 9 p.). Trata k-center e k-median, não k-means; tabelas de tempo em simulação numa máquina. | Adriano (2f4599a, 21/10) | fichado (22/10) |
| `MENG_2016_mllib_machine_learning_in_apache_spark.pdf` | Meng X. et al. *MLlib: Machine Learning in Apache Spark.* JMLR, 17(34):1–7, 2016. | KM4 | PDF da JMLR (cabeçalho "17 (2016) 1-7"); nº do artigo (34) a conferir. Descrição de biblioteca; k-means só em uma lista. | Herik (a301449, 21/10) | fichado (22/10) |
| `SCULLEY_2010_webscale_kmeans_clustering.pdf` | Sculley D. *Web-scale k-means clustering.* WWW '10 (pôster), p. 1177–1178, 2010. DOI: 10.1145/1772690.1772862. | KM4 | Pôster de 2 p.; página impressa 1177–1178. | Adriano (2f4599a, 21/10) | fichado (22/10) |

## Catálogo — DBSCAN

Mesmos critérios do catálogo de k-means: dados conferidos no cabeçalho do próprio PDF e no Crossref
(22/10/2026). "Páginas citadas": **impressas** quando o PDF traz a paginação do veículo; "p. N do PDF"
nos demais (Patwary, Götz, He 2011).

| Arquivo | Referência | Slot | Versão no PDF / observações | Trazido por | Status |
|---|---|---|---|---|---|
| `ESTER_1996_density_based_algorithm_discovering_clusters_noise.pdf` | Ester M., Kriegel H.-P., Sander J., Xu X. *A density-based algorithm for discovering clusters in large spatial databases with noise.* KDD-96, p. 226–231, AAAI Press, 1996. Sem DOI. | DB1 | 6 p.; paginação impressa 226–231. | Julia | fichado |
| `SCHUBERT_2017_dbscan_revisited_revisited.pdf` | Schubert E., Sander J., Ester M., Kriegel H.-P., Xu X. *DBSCAN revisited, revisited: why and how you should (still) use DBSCAN.* ACM TODS, 42(3), art. 19, 2017. DOI: 10.1145/3068335. | DB1 | 21 p.; paginação impressa "19:N". Fora da lista inicial do grupo. | Adriano | fichado |
| `PATWARY_2012_scalable_parallel_dbscan_disjoint_set.pdf` | Patwary M. M. A., Palsetia D., Agrawal A., Liao W.-K., Manne F., Choudhary A. *A new scalable parallel DBSCAN algorithm using the disjoint-set data structure.* SC '12, p. 1–11, 2012. DOI: 10.1109/SC.2012.9. | DB2 | 11 p.; sem paginação impressa por página (citar p. do PDF). | Julia | fichado |
| `WANG_2020_theoretically_efficient_parallel_dbscan.pdf` | Wang Y., Gu Y., Shun J. *Theoretically-efficient and practical parallel DBSCAN.* SIGMOD '20, p. 2555–2571, 2020. DOI: 10.1145/3318464.3380582. | DB2 | 17 p.; paginação impressa 2555–2571. As provas completas estão numa versão estendida (ref. [83] do artigo) que não está aqui. | Sofia | fichado |
| `XU_1999_fast_parallel_clustering_large_spatial_databases.pdf` | Xu X., Jäger J., Kriegel H.-P. *A fast parallel clustering algorithm for large spatial databases.* Data Mining and Knowledge Discovery, 3(3):263–290, 1999. DOI: 10.1023/A:1009884809343. | DB3 | 28 p.; paginação impressa 263–290. Figuras de resultado são imagens. | Julia | fichado |
| `GOTZ_2015_hpdbscan_highly_parallel.pdf` | Götz M., Bodenstein C., Riedel M. *HPDBSCAN: highly parallel DBSCAN.* MLHPC '15, art. 2, p. 1–10, 2015. DOI: 10.1145/2834892.2834894. | DB3 | 10 p.; sem paginação impressa por página (citar p. do PDF). | Sofia | fichado |
| `HE_2011_mr_dbscan_parallel_density_based_clustering_mapreduce.pdf` | He Y., Tan H., Luo W., Mao H., Ma D., Feng S., Fan J. *MR-DBSCAN: an efficient parallel density-based clustering algorithm using MapReduce.* ICPADS 2011, p. 473–480. DOI: 10.1109/ICPADS.2011.83. | DB3 | 8 p.; citar p. do PDF. | Julia | fichado |
| `HE_2014_mr_dbscan_extended.pdf` | He Y., Tan H., Luo W., Feng S., Fan J. *MR-DBSCAN: a scalable MapReduce-based DBSCAN algorithm for heavily skewed data.* Frontiers of Computer Science, 8(1):83–99, 2014. DOI: 10.1007/s11704-013-3158-3. | DB3 | 17 p.; paginação impressa 83–99. Versão estendida do He et al. 2011. Fora da lista inicial do grupo. | Adriano | fichado |
| `DAI_2012_mapreduce_dbscan_optimized_data_partition.pdf` | Dai B.-R., Lin I.-C. *Efficient Map/Reduce-based DBSCAN algorithm with optimized data partition.* IEEE CLOUD 2012, p. 59–66. DOI: 10.1109/CLOUD.2012.42. | DB3 | 8 p.; paginação impressa 59–66. | Julia | fichado |
| `HAN_2016_scalable_dbscan_spark.pdf` | Han D., Agrawal A., Liao W.-K., Choudhary A. *A novel scalable DBSCAN algorithm with Spark.* IPDPSW 2016, p. 1393–1402. DOI: 10.1109/IPDPSW.2016.57. | DB3 | 10 p.; paginação impressa 1393–1402. A lista inicial do grupo trazia o ano 2018. | Sofia | fichado |
| `CORDOVA_2015_dbscan_resilient_distributed_datasets.pdf` | Cordova I., Moh T.-S. *DBSCAN on resilient distributed datasets.* HPCS 2015, p. 531–540. DOI: 10.1109/HPCSim.2015.7237086. | DB3 | 10 p.; paginação impressa 531–540. | Sofia | fichado |
| `BOHM_2009_density_based_clustering_gpu.pdf` | Böhm C., Noll R., Plant C., Wackersreuther B. *Density-based clustering using graphics processors.* CIKM '09, p. 661–670. DOI: 10.1145/1645953.1646038. | DB4 | 10 p.; paginação impressa 661–670. | Sofia | fichado |
| `ANDRADE_2013_g_dbscan_gpu_accelerated.pdf` | Andrade G., Ramos G., Madeira D., Sachetto R., Ferreira R., Rocha L. *G-DBSCAN: a GPU accelerated algorithm for density-based clustering.* Procedia Computer Science, 18:369–378, 2013 (ICCS 2013). DOI: 10.1016/j.procs.2013.05.200. | DB4 | 10 p.; paginação impressa 369–378. | Sofia | fichado |

## Como adicionar um artigo novo

1. Salve o PDF aqui já com o nome no padrão acima.
2. Adicione a linha na tabela da família correspondente (k-means ou DBSCAN).
3. Some o slot em `../revisao_literatura.md` §4 (fila de leitura).
4. Depois de **lido e fichado**, ele migra para "Artigos lidos" (§5) **e** para `referencias.bib`.
