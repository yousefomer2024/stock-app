# Simple Dockerfile that builds with Gradle and runs the app (shell form)
FROM openjdk:24-jdk-slim

WORKDIR /app

# Copy everything (project root) into the container
COPY . /app

# Make the Gradle wrapper executable, build the project
RUN chmod +x ./gradlew && ./gradlew build --no-daemon

# Run the app (shell form avoids JSON parsing issues)
CMD ./gradlew run
