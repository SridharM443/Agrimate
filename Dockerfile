# Use Maven to build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build the app
COPY src ./src
RUN mvn clean package -DskipTests

# Use a smaller JDK image for running
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV PORT=8080

CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
