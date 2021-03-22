

# JAVA 8 Spring Boot Backend

### Sommaire :

* [Pré-requis](#pré-requis)
* [Installation de l'environnement de développement](#installation-de-lenvironnement-de-développement)
* [Dépendances](#dépendances)
* [Tests Unitaires](#tests-unitaires)
* [Outils de qualité du code](#outils-de-qualité-du-code)
* [Intégration continue](#intégration-continue)
* [Utilisation de Swagger](#utilisation-de-swagger)

-----------------------------------------------------------------------------------------------------

## Pré-requis

* Git
* Docker et Docker Compose

## Installation de l'environnement de développement

* Afin de conteneuriser notre application, nous avons créé le `Dockerfile` suivant : 
```dockerfile
#  
# Build stage  
#  
FROM maven:3.6.0-jdk-8-slim AS build  
ENV HOME=/home/usr/app  
RUN mkdir -p $HOME  
WORKDIR $HOME  
ADD pom.xml $HOME  
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]  
ADD . $HOME  
ARG ENV  
RUN echo "mvn clean package -DskipTests -P$ENV" > build.sh  
RUN chmod +x build.sh  
RUN ./build.sh  
  
#  
# Package stage  
#  
FROM maven:3.6.0-jdk-8-slim  
WORKDIR /home/app  
COPY --from=build /home/usr/app/target/*.jar /usr/local/lib/demo.jar  
EXPOSE 8080  
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]
```
> Ce fichier **Dockerfile** permet de :
> * Construire un container docker avec l'image `maven:3.6.0-jdk-8-slim` en y intégrant notre code source
> * Dans ce container, nous allons build ce code à l'aide de maven en spécifiant l'environnement
> * Ensuite, nous construisons le container final ou sera utilisé, comme entrypoint, le jar de l'application construit dans l'étape précédente et exposé sur le port 8080


* On utilise ensuite le fichier `docker-compose.dev.yml` (qui utilisera le `Dockerfile` pour build l'application)

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
      - dbmysql_dev  
    links:  
      - dbmysql_dev  
    volumes:  
      -  ./:/home/app/  
    environment:  
      - SPRING_DATASOURCE_URL=jdbc:mysql://dbmysql_dev:3306/database  
      - SPRING_DATASOURCE_USERNAME=username  
      - SPRING_DATASOURCE_PASSWORD=password  
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update  
    ports:  
      - 7001:8080  
  
  dbmysql_dev:  
    image: mysql:5.7.32  
    network_mode: bridge  
    container_name: dbmysql_dev  
    volumes:  
      - mysql_dev:/var/lib/mysql  
      - mysql_data_dev:/var/lib/mysql/data  
    environment:  
      MYSQL_DB: database  
      MYSQL_USER: username  
      MYSQL_PASSWORD: password  
  
volumes:  
  mysql_data_dev:  
  mysql_dev:
```

> Ce fichier **docker-compose** permet de :
>
> * Pour le service `backend_dev`
>    * Indique que `dbmysql_dev` est pré-requis pour le construction du service `backend_dev`
>    * Construire un conteneur appelé `backend_dev` à partir du **Dockerfile** présent à la racine du projet
>    * Lier au conteneur `dbmysql_dev`
>    * Utiliser la racine du répertoire local comme **volumes** et le lier au source du container
>    * Renseigner les variables d'environnements dont l'application à besoin
>    * Rediriger le port **8080** du container vers le **7001** de la machine parent
>  * Pour le service `dbmysql_dev`
>     * Construire le container `dbmysql_dev` à partir de l'image `mysql:5.7.32`
>     * Renseigner les variables d'environnements nécessaires à la base de donnée
>     * Lier le dossier `/var/lib/mysql` au volume `mysql_dev`
>     * Lier le dossier `/var/lib/mysql/data` au volume `mysql_data_dev`
>  * Pour la partie `volumes`
>     * Création du volume `mysql_dev`
>     * Création du volume `mysql_data_dev`

* Pour construire le container et le déployer en local

```bash
docker-compose -f docker-compose.dev.yml up --build
```

* L'Application est disponible à l'URL suivant : 

```html
http://localhost:7001/api/
```


## Dépendances
| Dépendance           | Version | Commentaire                                                     |
|----------------------|---------|-----------------------------------------------------------------|
| Spring Web           | 2.4.1   | Permet de build (utilise apache tomcat)                         |
| Spring Data JPA      | 2.4.1   | Utilise Hibernate pour gérer la persistance                     |
| Mysql connector java | 8.0.22  | Driver pour se connecter à la base de données Mysql             |
| Springdoc openapi    | 1.5.1   | Permet de générer la documentation                              |
| Junit                | 4.13.1  | Permet de réaliser les tests unitaires                          |
| H2database           | 1.4.200 | Base de données SQL pour JAVA, celle-ci est utilisée pour les tests unitaires |
| Jasper Reports       | 6.1.0   | Permet de générer des pdf                                       |
| Log4j2               | 2.4.2   | Permet de générer et formater les logs                          |
| Plugin JaCoCo        | 0.8.5   | Permet de calculer le code coverage (fonctionne avec sonarqube) |

## Logger

Nous utilisons Log4j2 pour créer et formater les logs dans la console ainsi que dans un fichier (disponible dans le dossier `logs`). Cette configuration est faite dans le fichier `src/main/resources/log4j2.xml` .

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<Configuration>  
 <Appenders>
	 <Console name="Console" target="SYSTEM_OUT">  
		 <PatternLayout  pattern="%style{%d{ISO8601}}{white} %highlight{%-5level }[%style{%t}{bright,blue}] %style{%C{1.}}{bright,yellow}: %msg%n%throwable" />  
	 </Console>  
	 <RollingFile name="RollingFile"  
	  fileName="./logs/spring-boot-logger-log4j2.log"  
	  filePattern="./logs/$${date:yyyy-MM}/spring-boot-logger-log4j2-%d{-dd-MMMM-yyyy}-%i.log.gz">  
		 <PatternLayout>
			 <pattern>%date{dd/mm/yyyy HH:mm:ss,SSS} %p (%C{1.}:%L) - %m%n</pattern>  
		 </PatternLayout>
		 <Policies>
			 <OnStartupTriggeringPolicy />
			 <SizeBasedTriggeringPolicy  size="10 MB" />  
			 <TimeBasedTriggeringPolicy />
		 </Policies>
	 </RollingFile>
 </Appenders>  
 <Loggers>  <!-- LOG everything at INFO level -->  
  <Root level="info">  
 <AppenderRef ref="Console" />  
 <AppenderRef ref="RollingFile" />  
 </Root>  
 <Logger name="com.pafpsdnc.opendata" level="trace"/>  
 </Loggers>
</Configuration>
```

- Pour les fichiers, le pattern suivant est utilisé : 
```c++
%date{dd/mm/yyyy HH:mm:ss,SSS} %p (%C{1.}:%L) - %m%n
```
- Pour la console, le pattern est utilisé : 
```c++
%style{%d{ISO8601}}{white} %highlight{%-5level }[%style{%t}{bright,blue}] %style{%C{1.}}{bright,yellow}: %msg%n%throwable
```    

- On peut trouver les logs dans le fichier  : `/logs/spring-boot-logger-log4j2.log`

- Exemple de log :
```bash
26/05/2021 14:05:20,905 TRACE (c.p.r.c.OpenDataController:39) - Show : OpenData{id=5, uniqId=50, name='Évènement', value='Festival de la charcuterie'}
```

## Tests Unitaires
Afin de réaliser les tests unitaires sur le projet, nous avons utilisé Junit 4.13.1 associé à maven pour les exécuter et à un base de données H2 pour le stockage des données. Nous utilisons également le plugin JaCoCo afin de calculer le code coverage et le stocker sous un format XML qui sera utilisé par SonarQube.

* La connexion à la base de données H2 est configurée dans le `pom.xml` : 
```xml
<plugin>  
	<groupId>org.apache.maven.plugins</groupId>  
	<artifactId>maven-surefire-plugin</artifactId>  
	<version>3.0.0-M5</version>  
	<configuration>
		<systemPropertyVariables>
			<spring.datasource.driver-class-name>
				org.h2.Driver
			</spring.datasource.driver-class-name>  
			<spring.datasource.url>
				jdbc:h2:mem:db;DB_CLOSE_DELAY=-1
			</spring.datasource.url>  
			<spring.datasource.username>sa</spring.datasource.username>  
			<spring.datasource.password>sa</spring.datasource.password>  
		</systemPropertyVariables>
	</configuration>
</plugin>
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

Pour vérifier la qualité du code, nous analysons celui-ci avec SonarQube qui est disponible sur l'url : https://sonarqube.nonstopintegration.ml

* Afin de configurer la connexion de notre projet à SonarQube,la localisation des informations fournis par les tests unitaires et jacoco ainsi que le langage du projet, nous avons ajouté les propriétés suivantes dans le fichier `pom.xml`: 
```xml
<sonar.projectKey>com.pafpsdnc:opendata</sonar.projectKey>  
<sonar.host.url>https://sonarqube.nonstopintegration.ml</sonar.host.url>  
<sonar.login>4f2a23bcf93a430dbf93c3f50d9af08f266fdade</sonar.login>  
<sonar.junit.reportPaths>target/surefire-reports/</sonar.junit.reportPaths>  
<sonar.java.coveragePlugin>jacoco</sonar.java.coveragePlugin>  
<sonar.dynamicAnalysis>reuseReports</sonar.dynamicAnalysis>  
<sonar.coverage.jacoco.xmlReportPaths>
	${project.basedir}/src/main/resources/jacoco/jacoco.xml
</sonar.coverage.jacoco.xmlReportPaths>  
<sonar.language>java</sonar.language>
``` 

## Intégration continue
L’intégration continue de projet est géré avec une pipeline `jenkins` multibranche disponible sur l'url : http://nonstopintegration.ml:8080/

* Le fichier `Jenkinsfile` nous permet de gérer cette pipeline : 
```java
def getEnvName(branchName) {  
    if (branchName.startsWith("release-")) {  
        return 'prod';  
    } else if (branchName == "preprod") {  
        return 'preprod';  
    }  
  
    return "dev";  
}

pipeline {  
    agent none  
    stages {  
        stage('Set environment') {  
            agent any  
            steps {  
                script {  
                    env.BRANCH_NAME = "${env.GIT_BRANCH.replaceFirst(/^.*\//, '')}"  
                    env.ENV_NAME = getEnvName(env.BRANCH_NAME)  
                }  
            }  
        }  
        stage('Build') {  
            agent {  
                docker { image 'maven:3.6.0-jdk-8-slim'}  
            }  
            steps {  
                sh 'mvn clean package -DskipTests -P${ENV_NAME}'  
            }  
        }  
        stage('Test') {  
            agent {  
                docker { image 'maven:3.6.0-jdk-8-slim'}  
            }  
            steps {  
                sh 'mvn -P${ENV_NAME} -B test'  
                sh 'mvn -P${ENV_NAME} -B jacoco:report'  
                junit '**/target/surefire-reports/TEST-*.xml'  
            }  
        }  
        stage("Sonarqube") {  
            agent {  
                docker { image 'maven:3.6.0-jdk-8-slim'}  
            }  
            steps {  
                withSonarQubeEnv('SonarQube') {  
                    sh 'mvn -P${ENV_NAME} -B sonar:sonar'  
                }  
                timeout(time: 5, unit: 'MINUTES') {  
                    waitForQualityGate abortPipeline: true  
                }  
            }  
        }  
        stage('Deploy') {  
            agent any  
            when {  
                expression { ENV_NAME == 'preprod' || ENV_NAME == 'prod' }  
            }  
            steps {  
                sh 'docker-compose -p backend_${ENV_NAME} -f \
                 docker-compose.${ENV_NAME}.yml up --build -d'  
            }  
        }  
    }  
    post {  
        always {  
            emailext to: "nonstopintegration@gmail.com",  
                     subject: "Jenkins Build ${currentBuild.currentResult}: \
		                       Job ${env.JOB_NAME}",  
                     attachLog: true,  
                     body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build \
                            ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}"
         }  
    }  
}  
```
> Ce fichier **Jenkinsfile** permet de créer une pipeline déclarative dont les différentes étapes sont :
>
> * De set les variables d'environnements :
>   * `BRANCH_NAME` : Ici on retire le préfix "origin/" du nom de la  branche
>   * `ENV_NAME` : On définit l'environnement (`prod`, `préprod` ou `dev`) en fonction du nom de la branche.
> * De tester le build du projet : 
>   * A partir d'un docker créé avec l'image `maven:3.6.0-jdk-8-slim`
>   * En spécifiant l'option `-P${ENV_NAME}`
> * D'exécuter les tests unitaire du projet et partager un rapport des résultats : 
>   * A partir d'un docker créé avec l'image `maven:3.6.0-jdk-8-slim`
>   * En spécifiant l'option `-P${ENV_NAME}`
> * D'analyser la qualité du code avec SonarQube : 
>   * A partir d'un docker créé avec l'image `maven:3.6.0-jdk-8-slim`
>   * En spécifiant l'option `-P${ENV_NAME}`
>   * withSonarQubeEnv nous permet d'exécuter l'analyse sur l'environnement
>   * waitForQualityGate nous permet d'attendre la réponse de sonar et ainsi d'indiquer à la pipeline si ce stage doit échouer ou non
> * De déployer notre application si l'environnement est la prod ou la préprod, sinon l'étape n'est pas effectuée. Nous utilisons la variable "ENV_NAME" pour utiliser le bon fichier "docker-compose"
> * À la fin de la pipeline, un mail récap est envoyé en indiquant si la pipeline est un succès ou un échec. Ce mail est accompagné des logs de la pipeline en pièce jointe

#### Le fonctionnement de notre intégration continue est le suivant :
* Lorsqu'on push un commit ou un tag, un webhook sur notre projet github va s'activer et informer Jenkins qu'une branche a été mis à jour (avec le commit) ou qu'un nouveau tag est disponible.
* Lorsque le webhook indique à jenkins les changements sur la nouvelle branche, celui-ci va automatiquement exécuter la pipeline et vérifier si toutes les étapes passent.
* Si on crée une Pull Request sur github, la dernière pipeline effectuée sur la branche sera automatiquement affiché dans la PR avec le statut de celui-ci : En cours, Succès ou Échec
* Un lien amenant au détail de la pipeline est également affichée et permet de consulter, en autre, les résultats des tests unitaires ou de l'analyse de sonarqube


## Utilisation de Swagger

Afin de documenter la partie backend de notre application de façon automatique nous avons installé Swagger. Swagger peut être installé avec beaucoup de technologies dont Spring boot et permet d'analyser et de documenter automatiquement une API. Afin de visualiser cette documentation, on peut récupérer celle-ci au format :

* JSON sur la route : https://api-opendata.nonstopintegration.ml/api-docs
* YAML sur la route : https://api-opendata.nonstopintegration.ml/api-docs.yaml
* HTML sur la route : https://api-opendata.nonstopintegration.ml/swagger-ui.html

### Exemple du format HTML

![Capture du 2021-03-10 19-58-13](https://nsa40.casimages.com/img/2021/03/10//210310080851135426.png)
