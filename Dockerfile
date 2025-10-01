# ===== Build Stage =====
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Package the app and repackage into fat jar
RUN mvn clean package spring-boot:repackage -DskipTests

# ===== Run Stage =====
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the fat JAR from build stage
COPY --from=build /app/target/*SNAPSHOT.jar app.jar

# Expose port (Render usually sets $PORT env)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
