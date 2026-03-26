# 1. Use an official Java runtime as a parent image
FROM eclipse-temurin:21-jdk-alpine

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy the project's compiled JAR file into the container
# (Make sure your target folder has the compiled .jar file!)
COPY target/*.jar app.jar

# 4. Make port 8080 available to the world outside this container
EXPOSE 8080

# 5. Run the jar file when the container launches
ENTRYPOINT ["java", "-jar", "app.jar"]