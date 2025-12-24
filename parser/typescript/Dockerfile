FROM mcr.microsoft.com/devcontainers/typescript-node:4.0.4-24-trixie

RUN apt-get update && apt-get install -y \
    tig \
    openjdk-21-jdk

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

WORKDIR /home/vscode/
RUN git clone https://github.com/Systems-Modeling/SysML-v2-Pilot-Implementation.git && \
    cd SysML-v2-Pilot-Implementation && \
    git checkout 433a8595d4843335107f8f1956a0396da512c5ba && \
    ./mvnw clean install
