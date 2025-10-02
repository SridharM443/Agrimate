# =======================================
# Stage 1: Build the Spring Boot fat jar
# =======================================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first and download dependencies (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the entire source code
COPY src ./src

# Build the fat jar (skipping tests for faster builds)
RUN mvn clean package -DskipTests

# =======================================
# Stage 2: Run the Spring Boot app
# =======================================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy only the fat jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the app port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
