#!/usr/bin/env bash
set -e
echo "Compiling Java OOP Circuit Simulator Backend..."
mkdir -p backend/out
javac -d backend/out $(find backend/src -name "*.java")
echo "Compilation successful! Starting Java Backend Server on http://localhost:8080 ..."
java -cp backend/out simulator.Main "$@"
