FROM openjdk:17-jdk-slim-buster
#RUN apk update; apk add --update  fontconfig libfreetype6
WORKDIR /apps
EXPOSE 5000
COPY target/seerbit-jb-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5004
ENTRYPOINT ["java","-jar","app.jar"]