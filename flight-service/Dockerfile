FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/flight-service-1.0.0.jar app.jar
EXPOSE 8060
ENTRYPOINT ["java", "-jar", "app.jar"]