# Stage 1: Build met Maven --> om mvn clean package te doen voor elke docker compose
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -q

# Stage 2: Docker voor Java
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=builder /app/target/gRPC.jar grpc.jar

EXPOSE 8080

ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar grpc.jar"]
