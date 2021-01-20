FROM maven:3-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
