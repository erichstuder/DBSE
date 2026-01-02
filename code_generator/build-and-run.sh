#!/bin/bash

set -e

if [[ "$1" == "-h" ]] || [[ "$1" == "--help" ]]; then
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  --silent    Build without outputting to the console"
    echo "  -h, --help  Show this help message"
    exit 0
fi

./build.sh "$1"

INTERACTIVE="/SysML-v2-Pilot-Implementation/org.omg.sysml.interactive/target/org.omg.sysml.interactive-0.55.0-SNAPSHOT-all.jar"

echo running interactive parser...
echo ""
java -cp "build/classes/java/main:build/resources/main:$INTERACTIVE" CodeGenerator
echo ""
echo ...done
