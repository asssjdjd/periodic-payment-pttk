# Dockerfile for User Service
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Your Team" \
      description="User Service" \
      version="1.0"

WORKDIR /app

COPY user_service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
