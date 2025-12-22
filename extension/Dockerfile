FROM mcr.microsoft.com/devcontainers/typescript-node:4.0.4-24-trixie

# These libraries are needed for testing.
RUN sudo apt-get update && sudo apt-get install -y \
    libnspr4 \
    libnss3 \
    libdbus-1-3 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libgtk-3-0 \
    libgbm1 \
    libasound2
