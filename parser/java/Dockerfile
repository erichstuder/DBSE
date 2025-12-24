FROM mcr.microsoft.com/devcontainers/java:21-jdk-bookworm

RUN apt-get update && apt-get install -y \
    tig \
    libfontconfig1 libfreetype6 libx11-6 libxau6 libxdmcp6 libxcb1 libxext6 libxrender1 libxtst6 libxi6 \
    graphviz

# Install gradle that way as sdk is not accessible here and apt-get installs a very old version which leads to problems.
ENV GRADLE_VERSION=9.2.1
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -O /tmp/gradle.zip && \
    unzip /tmp/gradle.zip -d /opt && \
    ln -s /opt/gradle-${GRADLE_VERSION} /opt/gradle && \
    rm /tmp/gradle.zip

ENV PATH="/opt/gradle/bin:${PATH}"

WORKDIR /home/vscode/
RUN git clone https://github.com/Systems-Modeling/SysML-v2-Pilot-Implementation.git && \
    cd SysML-v2-Pilot-Implementation && \
    git checkout 433a8595d4843335107f8f1956a0396da512c5ba && \
    ./mvnw clean install
