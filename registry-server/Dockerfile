FROM eclipse-temurin:17-alpine
VOLUME /temp
COPY target/registry-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
