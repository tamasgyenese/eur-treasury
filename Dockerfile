FROM openjdk:11-slim as build
MAINTAINER tgyenese
COPY target/eur-treasury-0.0.1-SNAPSHOT.jar target/eur-treasury-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/target/eur-treasury-0.0.1-SNAPSHOT.jar"]