#RUN mvn clean package -DskipTests
FROM maven:3-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
EXPOSE 8080
ADD . /opt/app
WORKDIR /opt/app
RUN ls
RUN mvn clean package -DskipTests
#RUN mvn -f /home/app/pom.xml clean package -DskipTests
ARG JAR_FILE=target/recipe-BACKEND.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]