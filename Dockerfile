# ========================
# 1. Build Stage
# ========================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (for build cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the rest of the source code
COPY src ./src

# Package the application (fat jar will be created)
RUN mvn clean package -DskipTests

# ========================
# 2. Run Stage
# ========================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the fat jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
