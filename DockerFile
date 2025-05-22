# Use official Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory inside container
WORKDIR /app

# Copy everything into container
COPY . .

# Build the app (creates the jar in target/)
RUN mvn clean package

# Use a lightweight JDK image for running the app
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/NoteStream-0.0.1-SNAPSHOT.jar app.jar

# Set environment port (Render provides this)
ENV PORT=8080

# Expose the port
EXPOSE 8080

# Start the app
CMD ["java", "-jar", "app.jar"]
