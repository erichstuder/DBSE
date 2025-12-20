FROM mcr.microsoft.com/devcontainers/rust:2.0.6-1-trixie

RUN apt-get update && apt-get install -y \
    tig

RUN curl --proto '=https' --tlsv1.2 -LsSf https://github.com/probe-rs/probe-rs/releases/download/v0.30.0/probe-rs-tools-installer.sh | sh

RUN rustup target add thumbv7em-none-eabihf

WORKDIR /home/vscode/dependencies_fetch_project/dummy/stm32f446re_example
RUN cargo init
COPY ./stm32f446re_example/Cargo.toml .
RUN cargo fetch
