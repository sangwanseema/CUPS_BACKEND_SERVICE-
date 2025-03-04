# Use OpenJDK 18 as the base image
FROM openjdk:18-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/CUPS_SERVICE-1.0-SNAPSHOT.jar /app/app.jar

# Copy the Dropwizard config file
COPY config.yml /app/config.yml

# Set environment variables (these can be overridden in Kubernetes)
ENV DB_USER=""
ENV DB_PASSWORD=""
ENV DB_HOST="localhost"
ENV DB_PORT="3306"

# Expose the Dropwizard port
EXPOSE 8080

# Command to start the Dropwizard application
CMD ["java", "-jar", "/app/app.jar", "server", "/app/config.yml"]
