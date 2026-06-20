# ============================================
# Etapa 1 — Build con Maven
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar solo el pom.xml primero (cache de dependencias)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el resto del código y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================
# Etapa 2 — Imagen final, liviana
# ============================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Render inyecta la variable PORT; el application.properties
# ya está configurado para leerla con server.port=${PORT:8080}
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
