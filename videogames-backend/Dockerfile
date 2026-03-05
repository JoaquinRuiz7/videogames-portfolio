# ---- Build stage ----
FROM gradle:8.5-jdk21 AS builder
WORKDIR /videogames
COPY . .
RUN ./gradlew clean build -x test

# ---- Runtime stage ----
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=builder /videogames/build/libs/*.jar videogames.jar

#ENV RAWG_API_KEY= comment just to let know it uses an env variable must be loaded with secrets
#in prodution and with .env file when running docker localy

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "videogames.jar"]