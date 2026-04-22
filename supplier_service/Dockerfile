# Dockerfile for Supplier Service
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Your Team" \
      description="Supplier Service" \
      version="1.0"

RUN apk add --no-cache curl
WORKDIR /app

COPY supplier_service/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]