#!/bin/bash
# Build the Socket Communication Demo (uses Maven + MigLayout)
# Usage: ./build.sh        # Build only
#        ./build.sh run    # Build and run

set -e
cd "$(dirname "$0")"

if ! command -v mvn &> /dev/null; then
    echo "Maven is required. Install from https://maven.apache.org/"
    exit 1
fi

echo "Building..."
mvn -q compile

echo "Build complete."
if [ "$1" = "run" ]; then
    echo "Starting Server + Client windows..."
    mvn -q javafx:run
else
    echo "Run with: mvn javafx:run"
    echo "Or:       ./build.sh run"
fi
