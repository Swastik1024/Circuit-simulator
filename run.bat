@echo off
echo Compiling Java OOP Circuit Simulator Backend...
if not exist backend\out mkdir backend\out
dir /s /B backend\src\*.java > sources.txt
javac -d backend\out @sources.txt
del sources.txt
echo Compilation successful! Starting Java Backend Server on http://localhost:8080 ...
java -cp backend\out simulator.Main %*
