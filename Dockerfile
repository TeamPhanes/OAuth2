FROM amazoncorretto:21
COPY /build/libs/OAuth2-0.0.1-SNAPSHOT.jar /oauth2.jar
ENTRYPOINT ["java", "-jar", "oauth2.jar"]