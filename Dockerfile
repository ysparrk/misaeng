FROM openjdk:17-jdk
LABEL maintainer="email@example.com"
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]