FROM ubuntu:21.04 as build

ARG GRADLE_VERSION="7.2"
ARG NODE_DIST="16.x"

RUN apt update && \
    apt install -y \
        curl \
        wget \
        unzip \
        openjdk-16-jdk

WORKDIR /tmp

#Installing nodejs
RUN curl -fsSL https://deb.nodesource.com/setup_${NODE_DIST} | bash -
RUN apt install -y nodejs

#Installing gradle
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && mkdir -p /opt/gradle \
    && unzip -d /opt/gradle /tmp/gradle-*.zip \


ENV GRADLE_HOME="/opt/gradle/gradle-${GRADLE_VERSION}"
ENV PATH="${GRADLE_HOME}/bin:${PATH}"


RUN mkdir -p /app
COPY src/. /app
WORKDIR /app
RUN gradle build

FROM openjdk:16-jdk-alpine

RUN mkdir -p /app
COPY --from=build /app/src/build/libs/*.jar /app/
ENTRYPOINT ["java","-jar","/app/unitconverter-0.0.1-SNAPSHOT.jar"]