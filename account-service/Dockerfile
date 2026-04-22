# Dockerfile for Account Service
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Your Team" \
      description="Account Service" \
      version="1.0"

RUN apk add --no-cache curl
WORKDIR /app

COPY account-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]