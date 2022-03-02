FROM maven:3.8.4-openjdk-17-slim AS build
RUN mkdir /opt/circus
WORKDIR /opt/circus
COPY . /opt/circus
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
COPY --from=build /opt/circus/target/circus.jar circus.jar
EXPOSE 8080
ENTRYPOINT java -jar circus.jar