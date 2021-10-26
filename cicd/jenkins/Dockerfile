FROM jenkins/jenkins:2.315-jdk11

USER root

RUN apt-get update \
    && apt-get install -y\
       apt-transport-https \
       ca-certificates \
       curl \
       gnupg \
       lsb-release \
    && curl -fsSL https://download.docker.com/linux/debian/gpg | \
        gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

RUN echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian \
      bullseye stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

RUN apt-get update \
    && apt-get install -y \
        docker-ce \
        docker-ce-cli \
        containerd.io