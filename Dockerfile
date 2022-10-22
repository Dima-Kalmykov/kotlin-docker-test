FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY /build/libs/kotlin-docker-test-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar", "/app.jar"]