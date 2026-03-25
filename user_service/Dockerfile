FROM eclipse-temurin:21-jre-alpine

LABEL description="User Service - Core Domain"
LABEL maintainer="backend-team"

WORKDIR /app

# Cài đặt curl để Docker Compose gọi Healthcheck
RUN apk add --no-cache curl

# Dùng wildcard để copy file jar bất kỳ sinh ra trong target
COPY target/*.jar app.jar

# Tạo user không có quyền root để chạy ứng dụng (Bảo mật)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Expose đúng cổng 8081 của User Service
EXPOSE 8081

# Lệnh thực thi tối ưu hóa cho môi trường Container
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]