#!/bin/bash
set -e

./parser/build.sh
npm install --prefix ./sysmlv2-renderer
