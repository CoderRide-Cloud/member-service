# Build from monorepo root: docker build -f member-service/Dockerfile -t member-service .
# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jdk-alpine AS build
RUN apk add --no-cache maven
WORKDIR /workspace
COPY common-lib ./common-lib
RUN cd common-lib && mvn clean install -DskipTests -q
COPY member-service/pom.xml ./member-service/pom.xml
COPY member-service/src ./member-service/src
RUN cd member-service && mvn clean package -DskipTests -q
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/member-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
