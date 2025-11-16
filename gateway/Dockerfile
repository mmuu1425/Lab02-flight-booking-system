FROM eclipse-temurin:17-jre-alpine
# 设置工作目录
WORKDIR /app

# 安装 wget 用于健康检查
RUN apk add --no-cache wget

# 复制Maven构建的JAR文件
COPY target/gateway-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]