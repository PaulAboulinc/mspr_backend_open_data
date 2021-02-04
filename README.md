# MSPR - Recipe Backend

## Pré-requis
* Git
* Docker et Docker Compose
* 

## Installation de l'environnement de développement
* Pour l'environnement local, utiliser le fichier `docker-compose.yml`
```yml
version: '3'

services:
  app:
    build: .
    container_name: recipe_back_mspr
    depends_on:
      - dbpostgres
    links:
      - dbpostgres
    volumes:
      -  ./:/home/app/
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://dbpostgres:5432/cooking
      - SPRING_DATASOURCE_USERNAME=cooking
      - SPRING_DATASOURCE_PASSWORD=cooking
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    ports:
      - 7001:8080

  dbpostgres:
    image: postgres:13.1-alpine
    container_name: dbpostgres
    volumes:
      - postgres:/var/lib/postgresql
      - postgres_data:/var/lib/postgresql/data
    environment:
      POSTGRES_DB: cooking
      POSTGRES_USER: cooking
      POSTGRES_PASSWORD: cooking
    ports:
      - 5432:5432

volumes:
  postgres_data:
  postgres:
  app_data:
```
> Ce fichier docker-compose permet de :
> * Pour le service `app`
>  * Indique que `dbpostgres` est pré-requis pour le construction du service `app`
>  * Construire un conteneur appelé `recipe_back_msp` à partir du **Dockerfile** présent à la racine du projet
>  * Lier au conteneur `dbpostgres`
>  * Utiliser la racine du répertoire local comme **volumes** et le lier au source du conteneur
>  * Renseigner les variables d'environnements dont l'application à besoin
>  * Rediriger le port **8080** du conteneur vers le **7001** de la machine parent

* Pour construire le conteneur et le deployer en local
```bash
docker-compose up --build -d
```
* L'API est disponible à l'URL suivant : 
```html
http://localhost:7001/api/
```

## Dépendances

## Conteneurisation et Virtualisation

## Test Unitaires

## Intégration continue

## Déploiment

## Contribution ?
