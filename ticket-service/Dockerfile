# FROM eclipse-temurin:17-jre-alpine
#
# # 安装 wget 用于健康检查
# RUN apk add --no-cache wget
#
# WORKDIR /app
# COPY target/booking-service-1.0.0.jar app.jar
# EXPOSE 8070
# ENTRYPOINT ["java", "-jar", "app.jar"]
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8060
ENTRYPOINT ["java", "-jar", "app.jar"]