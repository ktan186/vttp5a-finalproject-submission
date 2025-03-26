

# Package Spring project
FROM openjdk:23 AS javabuild

WORKDIR /server

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src src

RUN chmod a+x mvnw
RUN ./mvnw package -Dmaven.test.skip=true

# Run container
FROM openjdk:23

WORKDIR /app

COPY --from=javabuild /server/target/final-project-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080

EXPOSE ${PORT}

ENTRYPOINT [ "java", "-jar", "app.jar"]