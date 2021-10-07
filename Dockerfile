FROM ubuntu:21.04 as build

ARG GRADLE_VERSION="7.2"
ARG NODE_DIST="16.x"
ARG BUILD_VERSION="0.0.1-SNAPSHOT"

RUN apt-get update && \
    apt-get install -y \
        curl \
        wget \
        unzip \
        openjdk-16-jdk

WORKDIR /tmp

#Installing nodejs
RUN curl -fsSL https://deb.nodesource.com/setup_${NODE_DIST} | bash -
RUN apt-get install -y nodejs

#Installing gradle
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && mkdir -p /opt/gradle \
    && unzip -d /opt/gradle /tmp/gradle-*.zip

ENV GRADLE_HOME="/opt/gradle/gradle-${GRADLE_VERSION}"
ENV PATH="${GRADLE_HOME}/bin:${PATH}"

#Building project JAR file
RUN mkdir -p /app
COPY src/. /app
WORKDIR /app
RUN gradle build -P version="$BUILD_VERSION" && \
    echo "${BUILD_VERSION}" >> version.txt

#Production Image
FROM openjdk:16-jdk-alpine

RUN mkdir -p /app

COPY --from=build /app/build/libs/*.jar /app/
COPY --from=build /app/version.txt /app/version.txt

ENTRYPOINT java -jar /app/unitconverter-`cat /app/version.txt`.jar
