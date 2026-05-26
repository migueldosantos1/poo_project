#!/bin/sh
# Phase 1 execution script

mkdir -p output

echo "=================================================="
echo "Example 1 - 4 players, full game, Player 1 wins"
echo "=================================================="
java -jar project-v1.jar input/deck-1.txt input/script-1.txt 4 > output/out1.txt
echo "Done. See output/out1.txt"
