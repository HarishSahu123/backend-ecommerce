# Use Java 11 base image
FROM eclipse-temurin:11-jdk-alpine

WORKDIR /app

# Copy built jar file
COPY target/*.jar app.jar

# Expose port (Render will inject PORT env variable)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]