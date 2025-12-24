#/bin/bash

set -e

if [[ "$1" == "-h" ]] || [[ "$1" == "--help" ]]; then
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -v           Show verbose build output"
    echo "  -vv          Show extra verbose (pass --verbose to build)"
    echo "  -h, --help   Show this help message"
    exit 0
fi

args=""
if [[ "$1" == "-vv" ]]; then
    args="--verbose"
elif [[ "$1" == "-v" ]]; then
    args=""
else
    args="--silent"
fi

echo ""
npm install $args
npm run build $args
npm run start $args
echo ""
