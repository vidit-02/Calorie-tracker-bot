FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/calorie-ai-backend.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
