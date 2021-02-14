# MSPR - Recipe Backend

## Pré-requis

* Git
* Docker et Docker Compose

## Installation de l'environnement de développement

* Pour l'environnement local, utiliser le fichier `docker-compose.yml`

```yml
version: '3'

services:
  app:
    build: .
    container_name: api_backend
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
```

> Ce fichier **docker-compose** permet de :
>
> * Pour le service `backend`
>    * Indique que `dbpostgres` est pré-requis pour le construction du service `app`
>    * Construire un conteneur appelé `recipe_back_msp` à partir du **Dockerfile** présent à la racine du projet
>    * Lier au conteneur `dbpostgres`
>    * Utiliser la racine du répertoire local comme **volumes** et le lier au source du container
>    * Renseigner les variables d'environnements dont l'application à besoin
>    * Rediriger le port **8080** du container vers le **7001** de la machine parent
>  * Pour le service `db`
>     * Construire le container `dbpostgres` à partir de l'image `postgres:13.1-alpine`
>     * Renseigner les variables d'environnements nécessaires à la base de donnée
>     * Lier le dossier `/var/lib/postgresql` au volume `postgres`
>     * Lier le dossier `/var/lib/postgresql/data` au volume `postgres_data`
>     * Rediriger le port **5432** du container vers le **5432** du serveur
>  * Pour la partie `volumes`
>     * Création du volume `postgres`
>     * Création du volume `postgres_data`

* Pour construire le container et le deployer en localement

```bash
docker-compose up --build -d
```

* L'Application est disponible à l'URL suivant : 

```html
http://localhost:7001/api/
```

> **NB :** Les sources local sont liées à celle présente dans le container, du coup pas besoin de build de nouveau à chaque changement dans le code.

## Dépendances
| Dépendance         | Version | Commentaire                                                     |
|--------------------|---------|-----------------------------------------------------------------|
| Spring Web         | 2.4.1   | Permet de build (utilise apache tomcat)                         |
| Spring Data JPA    | 2.4.1   | Utilise Hibernate pour gérer la persistance                     |
| PostgresSQL driver | 42.2.18 | Driver pour se connecter à la base de données PostgesSQL        |
| Springdoc openapi  | 1.5.1   | Permet de générer la documentation                              |
| Junit              | 4.13.1  | Permet de réaliser les tests unitaires                          |
| Jasper Reports     | 6.1.0   | Permet de générer des pdf                                       |
| Log4j2             | 2.4.2   | Permet de générer et formatter les logs                         |
| Plugin JaCoCo      | 0.8.5   | Permet de calculer le code coverage (fonctionne avec sonarqube) |

## Tests Unitaires
Afin de réaliser les tests unitaires sur le projet, nous avons utilisé Junit 4.13.1 associé à maven pour les exécuter. Nous utilisons également le plugin JaCoCo afin de calculer le code coverage et le stocker sous un format XML qui sera utilisé par SonarQube.

* Les tests doivent être lancée depuis le container docker, voici la commande à jouer : 
```shell
docker exec api_backend mvn -B -f /home/app/pom.xml test
```

* Afin d'obtenir un rapport du coverage à l'aide de JaCoCo, éxécuter la commande suivante :
```shell
docker exec api_backend mvn -B -f /home/app/pom.xml jacoco:report
```
Puis ouvrez le fichier "target/site/jacoco/index.html" dans votre navigateur.

## Outils de qualité du code
Sonarqube 
* Indiquer la conf pour expliquer comme la changer (pom.xml) => source a inclure ou exclure
* Indiquer les règles importantes/bloquantes par défault et si il y en a, les customs
 

## Intégration continue
* Redmine : Expliquer comment utiliser jenkins (créer un compte, lancer le build, ...)

* Expliquer la stratégie du build (décrire jenkinsfile)
* Indiquer que c'est automatique (hook) + autres règles s'il y en a


## Déploiment
* à ajouter sur redmine de façon précise (ici on a déjà ce qu'il faut avec l'integration continue)
