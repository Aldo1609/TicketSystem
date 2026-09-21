# Etapa 1: Runtime ligero
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Argumento para el JAR compilado
ARG JAR_FILE=target/*.jar

# Copiamos el JAR generado previamente por Maven
COPY ${JAR_FILE} app.jar

# Exponemos el puerto estándar de Spring Boot
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
