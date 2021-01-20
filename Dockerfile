FROM maven:3.6.0-jdk-8-slim
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/app/target/recipe-BACKEND.jar"]