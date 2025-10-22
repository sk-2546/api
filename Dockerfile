FROM gradle:8.4.1-jdk17 AS build
WORKDIR /workspace

# Copy project files
COPY . /workspace

# Build only the :api project to produce distribution
RUN gradle :api:installDist --no-daemon --stacktrace -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy built distribution from build stage
COPY --from=build /workspace/api/build/install/api /app

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["/app/bin/api"]
