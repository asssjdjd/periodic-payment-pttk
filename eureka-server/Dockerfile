# Dockerfile for Eureka Server
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Your Team" \
      description="Eureka Server" \
      version="1.0"

RUN apk add --no-cache curl
WORKDIR /app

COPY eureka-server/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]