# TP2 - Microservices Store & Stock
1. Ouvre deux terminaux 
2. Va dans le dossier `TP2/stock-service` dans le premier terminal.
3. Tape :
```
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```
4. Va dans le dossier `TP2/store` dans le deuxième terminal.
5. Tape :
```
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```
6. Ouvre ton navigateur et va sur :
```
http://localhost:8081/store/commandes
```

## Fonctionnement

- Le microservice **stock-service** gère les articles en stock (produits, quantités).
- Le microservice **store** gère les commandes clients.
- Quand tu crées une commande, tu choisis un produit du stock et une quantité (la quantité ne peut pas dépasser le stock dispo).
- Tu peux voir les détails d'une commande, ajouter ou supprimer des lignes (produits/quantités) tant que la commande n'est pas soumise.
- Quand tu soumets la commande, elle est envoyée via Kafka (pas besoin de lancer Kafka pour tester la base).


