FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

# Instalar MySQL client para el health check
RUN apt-get update && apt-get install -y mysql-client && rm -rf /var/lib/apt/lists/*

# Copiar script de espera
COPY wait-for-mysql.sh /wait-for-mysql.sh
RUN chmod +x /wait-for-mysql.sh

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Usar el script de espera
CMD ["/wait-for-mysql.sh", "mysql", "java", "-jar", "app.jar"]