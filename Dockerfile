# --- Build stage ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Download dependencies first (layer-cached separately from source)
RUN apk add --no-cache maven && mvn dependency:go-offline -q
RUN mvn package -DskipTests -q

# --- Runtime stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create logs directory
RUN mkdir -p logs

COPY --from=build /app/target/*.jar app.jar

# Run as non-root user
RUN addgroup -S oktaguard && adduser -S oktaguard -G oktaguard
USER oktaguard

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]