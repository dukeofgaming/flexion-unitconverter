FROM ubuntu:21.10 as build

RUN apt update && \
    apt install -y nodejs \
        openjdk-16-jdk \
        gradle

RUN mkdir -p /app
COPY src/. /app
WORKDIR /app
RUN gradle build

FROM openjdk:16-jdk-alpine

RUN mkdir -p /app
COPY --from=build /app/src/build/libs/*.jar /app/
ENTRYPOINT ["java","-jar","/app/unitconverter-0.0.1-SNAPSHOT.jar"]