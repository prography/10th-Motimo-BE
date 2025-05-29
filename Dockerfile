FROM openjdk:21-jdk

WORKDIR /app

COPY motimo-api/build/libs/motimo-api-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
