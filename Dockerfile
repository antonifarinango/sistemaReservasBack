# Etapa de construcción (Build stage)
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Construimos el jar (saltando las pruebas para que sea más rápido en el entorno de build)
RUN mvn clean package -DskipTests

# Etapa de ejecución (Run stage)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copiamos el jar desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto (Render por defecto inyecta la variable PORT, comúnmente 10000 o el que configuremos,
# pero Spring Boot tomará esto de application.properties que configuramos para usar ${PORT:8080})
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
