FROM eclipse-temurin:17-jre-alpine

LABEL description="PaymentService - Core Domain"
LABEL maintainer="Phạm Thành Long"

RUN apk add --no-cache curl
WORKDIR /app

COPY payment_service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]