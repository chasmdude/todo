# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the project files to the container
COPY . .

# Ensure Maven wrapper is executable
RUN chmod +x ./mvnw

# Package the application
RUN ./mvnw clean package -DskipTests

# Expose the port the application runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/todos-0.0.1.jar"]
