FROM amazoncorretto:17-alpine-jdk

COPY SPRINGBOOT-APPLICATION-NOTEAPP/target/springboot-noteapp-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]