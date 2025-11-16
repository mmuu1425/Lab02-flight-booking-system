FROM eclipse-temurin:17-jre-alpine

# 安装 wget 用于健康检查
RUN apk add --no-cache wget

WORKDIR /app
COPY target/flight-service-1.0.0.jar app.jar
EXPOSE 8060
ENTRYPOINT ["java", "-jar", "app.jar"]