# Dockerfile

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy build output (adjust path if needed)
COPY build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
