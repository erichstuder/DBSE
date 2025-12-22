#/bin/bash

set -e

if [[ "$1" == "-h" ]] || [[ "$1" == "--help" ]]; then
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -v          Show verbose build output"
    echo "  -h, --help  Show this help message"
    exit 0
fi



if [[ "$1" == "-v" ]]; then
    gradle processResources --configuration-cache
    gradle compileJava --configuration-cache
    echo ""
else
    gradle processResources --configuration-cache > /dev/null 2>&1
    gradle compileJava --configuration-cache > /dev/null 2>&1
fi

INTERACTIVE="$HOME/SysML-v2-Pilot-Implementation/org.omg.sysml.interactive/target/org.omg.sysml.interactive-0.55.0-SNAPSHOT-all.jar"

echo running the program...
echo ""
java -cp "build/classes/java/main:build/resources/main:$INTERACTIVE" Parser
echo ""
echo ...done
