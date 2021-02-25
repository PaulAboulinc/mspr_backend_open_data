# MSPR - Recipe Backend

## Pré-requis

* Git
* Docker et Docker Compose

## Installation de l'environnement de développement

* Pour l'environnement local, utiliser le fichier `docker-compose.dev.yml`

```yml
version: '3'  
  
services:  
  backend_dev:  
    build:  
      context: .  
      args:  
        - ENV=dev  
    network_mode: bridge  
    container_name: backend_dev  
    depends_on:  
      - dbpostgres_dev  
    links:  
      - dbpostgres_dev  
    volumes:  
      -  ./:/home/app/  
    environment:  
      - SPRING_DATASOURCE_URL=jdbc:postgresql://dbpostgres_dev:5432/database  
      - SPRING_DATASOURCE_USERNAME=username  
      - SPRING_DATASOURCE_PASSWORD=password  
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update  
    ports:  
      - 7001:8080  
  
  dbpostgres_dev:  
    image: postgres:13.1-alpine  
    network_mode: bridge  
    container_name: dbpostgres_dev  
    volumes:  
      - postgres_dev:/var/lib/postgresql  
      - postgres_data_dev:/var/lib/postgresql/data  
    environment:  
      POSTGRES_DB: database  
      POSTGRES_USER: username  
      POSTGRES_PASSWORD: password  
  
volumes:  
  postgres_data_dev:  
  postgres_dev:
```

> Ce fichier **docker-compose** permet de :
>
> * Pour le service `backend_dev`
>    * Indique que `dbpostgres_dev` est pré-requis pour le construction du service `backend_dev`
>    * Construire un conteneur appelé `backend_dev` à partir du **Dockerfile** présent à la racine du projet
>    * Lier au conteneur `dbpostgres_dev`
>    * Utiliser la racine du répertoire local comme **volumes** et le lier au source du container
>    * Renseigner les variables d'environnements dont l'application à besoin
>    * Rediriger le port **8080** du container vers le **7001** de la machine parent
>  * Pour le service `dbpostgres_dev`
>     * Construire le container `dbpostgres_dev` à partir de l'image `postgres:13.1-alpine`
>     * Renseigner les variables d'environnements nécessaires à la base de donnée
>     * Lier le dossier `/var/lib/postgresql` au volume `postgres_dev`
>     * Lier le dossier `/var/lib/postgresql/data` au volume `postgres_data_dev`
>  * Pour la partie `volumes`
>     * Création du volume `postgres_dev`
>     * Création du volume `postgres_data_dev`

* Pour construire le container et le déployer en local

```bash
docker-compose -f docker-compose.dev.yml up --build
```

* L'Application est disponible à l'URL suivant : 

```html
http://localhost:7001/api/
```


## Dépendances
| Dépendance         | Version | Commentaire                                                     |
|--------------------|---------|-----------------------------------------------------------------|
| Spring Web         | 2.4.1   | Permet de build (utilise apache tomcat)                         |
| Spring Data JPA    | 2.4.1   | Utilise Hibernate pour gérer la persistance                     |
| PostgresSQL driver | 42.2.18 | Driver pour se connecter à la base de données PostgesSQL        |
| Springdoc openapi  | 1.5.1   | Permet de générer la documentation                              |
| Junit              | 4.13.1  | Permet de réaliser les tests unitaires                          |
| H2database         | 1.4.200 | Base de données SQL pour JAVA, celle-ci est utilisée pour les tests unitaires |
| Jasper Reports     | 6.1.0   | Permet de générer des pdf                                       |
| Log4j2             | 2.4.2   | Permet de générer et formatter les logs                         |
| Plugin JaCoCo      | 0.8.5   | Permet de calculer le code coverage (fonctionne avec sonarqube) |


## Tests Unitaires
Afin de réaliser les tests unitaires sur le projet, nous avons utilisé Junit 4.13.1 associé à maven pour les exécuter et à un base de données H2 pour le stockage des données. Nous utilisons également le plugin JaCoCo afin de calculer le code coverage et le stocker sous un format XML qui sera utilisé par SonarQube.

La connexion à la base de données H2 est configurée à l'aide du fichier `/src/test/resources/application.properties` : 
```properties
spring.datasource.driver-class-name=org.h2.Driver  
spring.datasource.url=jdbc:h2:mem:db;DB_CLOSE_DELAY=-1  
spring.datasource.username=sa  
spring.datasource.password=sa
```

* Les tests doivent être lancée depuis le container docker, voici la commande à jouer : 
```shell
docker exec backend_dev mvn -B -f /home/app/pom.xml test
```

* Afin d'obtenir un rapport du coverage à l'aide de JaCoCo, exécuter la commande suivante :
```shell
docker exec backend_dev mvn -B -f /home/app/pom.xml jacoco:report
```
On peut ensuite accéder au résultat des tests au format HTML sur : http://localhost:7001/

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
