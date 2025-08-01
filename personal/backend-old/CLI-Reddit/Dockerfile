FROM openjdk:17-jdk-slim

COPY /target/*with-dependencies.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
