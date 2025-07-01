# 1.Base Image: Use a lightweight Java runtime image
FROM eclipse-temurin:17-jdk-jammy

# 2.Set the working directory in the container
WORKDIR /app

# 3️.Copy the application JAR into the container
COPY target/weatherByIp-0.0.1-SNAPSHOT.jar app.jar

# 4.Expose the application port (optional, for documentation)
EXPOSE 8080

# 5️.Default command to run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]