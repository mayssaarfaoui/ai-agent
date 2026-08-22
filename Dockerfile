
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY graph-builder-rest/target/*.jar graph-builder.jar
COPY graph-chat-rest/target/*.jar graph-chat.jar
COPY user-management-service/target/*.jar user-management.jar

# Exposer le port (adaptez selon votre application)
EXPOSE 8080

# Commande par défaut (à adapter selon le service principal)
ENTRYPOINT ["java", "-jar", "/app/graph-builder.jar"]