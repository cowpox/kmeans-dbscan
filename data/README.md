# Datasets

Os arquivos CSV deste diretório são gerados por `scripts/generate_datasets.py`
e **não são versionados no git** (ver `.gitignore`) — são grandes e totalmente
reproduzíveis a partir do script.

## Como gerar

```bash
pip install -r scripts/requirements.txt
python scripts/generate_datasets.py
```

Isso cria:

- `dataset1_full.csv` — blobs gaussianos bem separados (8 clusters, mesmo desvio padrão)
- `dataset2_full.csv` — blobs anisotrópicos com densidades diferentes (5 clusters)

Ambos no tamanho máximo (150.000 pontos). Os testes de escalabilidade usam os
primeiros N pontos de cada arquivo para cada tamanho avaliado (10k, 30k, ...,
150k), garantindo que o mesmo dado "cresça" de forma consistente.

Formato: `id,x1,x2` — sem necessidade de normalização adicional.
