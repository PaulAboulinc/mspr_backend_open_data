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