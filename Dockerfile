FROM gradle:8.13-jdk21 AS builder

WORKDIR /app

#RUN apt-get update && apt-get install -y findutils

COPY . .


RUN chmod +x ./gradlew

RUN ls -la /app

RUN ./gradlew :motimo-api:bootJar --no-daemon

RUN ls -la /app

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=builder /app/motimo-api/build/libs/motimo-api-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
