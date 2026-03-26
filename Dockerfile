
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Run the maven build inside the container!
RUN mvn clean package -DskipTests

# ==========================
# STAGE 2: RUN THE APP
# ==========================
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Grab ONLY the compiled jar from Stage 1
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]