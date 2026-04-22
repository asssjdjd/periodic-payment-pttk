# Dockerfile for Statistics Service
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Your Team" \
      description="Statistics Service" \
      version="1.0"

RUN apk add --no-cache curl
WORKDIR /app

COPY statistics_service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]