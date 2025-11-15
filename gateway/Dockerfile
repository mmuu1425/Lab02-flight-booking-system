FROM eclipse-temurin:17-jre-alpine
# 设置工作目录
WORKDIR /app

# 复制Maven构建的JAR文件
COPY target/gateway-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]