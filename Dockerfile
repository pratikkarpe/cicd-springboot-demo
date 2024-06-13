FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/cameldynamicroute-0.0.1-SNAPSHOT.jar /app/cameldynamicroute.jar

ENTRYPOINT ["java", "-jar", "cameldynamicroute.jar"]