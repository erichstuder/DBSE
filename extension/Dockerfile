FROM mcr.microsoft.com/devcontainers/typescript-node:4.0.4-24-trixie

# These libraries are needed for testing of the extension.
RUN apt-get update && apt-get install -y \
    libnspr4 \
    libnss3 \
    libdbus-1-3 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libgtk-3-0 \
    libgbm1 \
    libasound2 \
    xvfb

RUN apt-get update && apt-get install -y \
    tig \
    graphviz \
    openjdk-21-jdk
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

ENV GRADLE_VERSION=9.2.1
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -O /tmp/gradle.zip && \
    unzip /tmp/gradle.zip -d /opt && \
    ln -s /opt/gradle-${GRADLE_VERSION} /opt/gradle && \
    rm /tmp/gradle.zip
ENV PATH="/opt/gradle/bin:${PATH}"

WORKDIR /
RUN git clone https://github.com/Systems-Modeling/SysML-v2-Pilot-Implementation.git && \
    cd SysML-v2-Pilot-Implementation && \
    git checkout 433a8595d4843335107f8f1956a0396da512c5ba && \
    ./mvnw clean install
