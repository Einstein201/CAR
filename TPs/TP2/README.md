# TP2 - Microservices Store & Stock avec Kafka

## Prérequis

- Java 17+
- Maven installé (`mvn`)
- Docker Desktop en cours d'exécution

## Démarrage

### 1. Lancer Kafka (Docker)

Dans le dossier `TP2/kafka` :

```
docker compose up -d
```

Attendez quelques secondes que le topic `my-first-topic` soit créé.

### 2. Lancer le stock-service (terminal 1)

Dans le dossier `TP2/stock-service` :

```
mvn spring-boot:run
```

Le service démarre sur le port **8082**.

### 3. Lancer le store (terminal 2)

Dans le dossier `TP2/TP1_2/store` :

```
mvnw.cmd spring-boot:run
```

Le service démarre sur le port **8080**.

## URLs

Service         URL                                  
--------------- -------------------------------------
Store (app)   :  http://localhost:8080/store/home     
Stock-service :  http://localhost:8082/stocks         
Kafka UI      : http://localhost:3000                
H2 Store      : http://localhost:8080/h2-console     
H2 Stock      : http://localhost:8082/h2-console     

## Fonctionnement

- Le **store** gère les clients, commandes et produits avec gestion du stock local.
- Le **stock-service** gère ses propres articles (`stylo`, `cahier`, `clavier`) et écoute Kafka.

- Quand une ligne est ajoutée à une commande dans le store :
  1. Le stock du produit est décrémenté dans le store.
  2. Un message Kafka `commandeId;libelle;quantite` est publié sur `my-first-topic`.
  3. Le stock-service consomme le message et décrémente son propre stock.

## Scénario de test

1. Aller sur http://localhost:8080/store/register ---->>  créer un compte
2. Créer une commande
3. Ajouter des lignes (Stylo, Cahier, etc.)
4. Vérifier http://localhost:8082/stocks --- >>  le stock est décrémenté
5. Vérifier http://localhost:3000 → les messages Kafka sont visibles
