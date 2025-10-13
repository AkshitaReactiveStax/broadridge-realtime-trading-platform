# Use lightweight JDK base
FROM eclipse-temurin:17-jdk-jammy

# Set workdir
WORKDIR /app

# Copy the built jar (name must match your target jar)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose port (customize per service)
EXPOSE 8082

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]