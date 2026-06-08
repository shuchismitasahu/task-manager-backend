# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy maven files
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn ./.mvn

# Build the jar
RUN ./mvnw clean package -DskipTests

# Run the jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/task-manager-backend-0.0.1-SNAPSHOT.jar"]