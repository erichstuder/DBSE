#!/bin/bash

set -e
cd "$(dirname "$0")"

gradle test
