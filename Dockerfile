FROM adoptopenjdk/openjdk11

WORKDIR /app

COPY target/cameldynamicroute-0.0.1-SNAPSHOT.jar /app/cameldynamicroute.jar

ENTRYPOINT ["java", "-jar", "cameldynamicroute.jar"]