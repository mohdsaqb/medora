# Build stage.
FROM eclipse-temurin:23-jdk-alpine AS build
WORKDIR /build

# Copy the wrapper and pom before the sources so dependency resolution is
# cached in its own layer and is not invalidated by every code change.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

COPY src/ src/
RUN ./mvnw -B clean package -DskipTests

# Runtime stage. JRE only, so the compiler and build tooling are not shipped.
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

# Run as a non root user.
RUN addgroup -S medora && adduser -S -G medora medora

COPY --from=build /build/target/*.jar app.jar
USER medora

# There is no Docker daemon inside the container, so the Compose integration
# used for local development must stay off here.
ENV DOCKER_COMPOSE_ENABLED=false

# Informational. Hosting platforms inject the real port through PORT, which
# application.properties reads.
EXPOSE 8185

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
