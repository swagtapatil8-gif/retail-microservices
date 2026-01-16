# Base image with Java 17
FROM eclipse-temurin:17-jdk

# App directory inside container
WORKDIR /app

# Copy jar into container
COPY target/*.jar app.jar

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]
