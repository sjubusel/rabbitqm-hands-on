# Multi-stage build for optimal image size
FROM gradle:8-jdk21-alpine AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and configuration files first (for better layer caching)
COPY ./gradlew gradlew.bat gradle.properties settings.gradle.kts ./
COPY ./gradle gradle/

# Copy build source files
COPY ./buildSrc buildSrc/

# Copy module build files (for dependency resolution)
COPY ./modules/utils/build.gradle.kts modules/utils/build.gradle.kts
COPY ./modules/consumer/build.gradle.kts modules/consumer/build.gradle.kts
COPY ./build.gradle.kts ./

# Download dependencies (separate layer for better caching)
RUN gradle dependencies --no-daemon

# Copy source code
COPY ./modules/utils/src modules/utils/src/
COPY ./modules/consumer/src modules/consumer/src/

# Build the application
RUN gradle :consumer:build --no-daemon -x test

# Runtime stage - use smaller JRE image
FROM eclipse-temurin:21-jre-alpine

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder --chown=appuser:appgroup /app/modules/consumer/build/libs/*.jar app.jar

# Switch to non-root user
USER appuser

# Configure JVM for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

# Health check (optional - adjust based on your app's health endpoint)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD pgrep -f java || exit 1

# Expose port if needed (adjust based on your application)
# EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
