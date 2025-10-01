# Use OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy Maven wrapper and pom first to cache dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline

# Copy all project files
COPY . .

# Build the app
RUN ./mvnw clean package -DskipTests

# Expose the port
EXPOSE 8080

# Run the Spring Boot jar
CMD ["java", "-jar", "target/agrimate-0.0.1-SNAPSHOT.jar"]
