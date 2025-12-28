#/bin/bash

set -e
cd "$(dirname "$0")"

if [[ "$1" == "-h" ]] || [[ "$1" == "--help" ]]; then
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  --silent    Build without outputting to the console"
    echo "  -h, --help  Show this help message"
    exit 0
fi

if [[ "$1" == "--silent" ]]; then
    gradle processResources --configuration-cache > /dev/null 2>&1
    gradle compileJava --configuration-cache > /dev/null 2>&1
else
    gradle processResources --configuration-cache
    gradle compileJava --configuration-cache
    echo ""
fi
