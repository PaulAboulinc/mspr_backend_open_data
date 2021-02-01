#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
WORKDIR /home/app
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM maven:3.6.0-jdk-8-slim
WORKDIR /home/app
COPY --from=build /home/app/target/*.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]