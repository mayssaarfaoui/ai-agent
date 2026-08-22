FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY graph-builder-rest/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]