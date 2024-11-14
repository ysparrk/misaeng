FROM openjdk:17-jdk
LABEL maintainer="email"
ARG JAR_FILE=build/libs/misaeng-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-springboot.jar"]