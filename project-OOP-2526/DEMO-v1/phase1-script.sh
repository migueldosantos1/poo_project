#!/bin/sh

# create the output folder if needed
mkdir -p output

echo "=================================================="
echo "Example 1"
echo "=================================================="
java -jar project-v1.jar in-out-files-for-demonstration/input/ex1-deck.txt in-out-files-for-demonstration/input/ex1-script.txt 4 > output/out1.txt

echo "=================================================="
echo "Example 2"
echo "=================================================="
java -jar project-v1.jar in-out-files-for-demonstration/input/ex2-deck.txt in-out-files-for-demonstration/input/ex2-script.txt 4 > output/out2.txt

echo "=================================================="
echo "Example 3"
echo "=================================================="
java -jar project-v1.jar in-out-files-for-demonstration/input/ex3-deck.txt in-out-files-for-demonstration/input/ex3-script.txt 4 > output/out3.txt

echo "=================================================="
echo "Example 4"
echo "=================================================="
java -jar project-v1.jar in-out-files-for-demonstration/input/ex4-deck.txt in-out-files-for-demonstration/input/ex4-script.txt 4 > output/out4.txt

echo "=================================================="
echo "Example 5"
echo "=================================================="
java -jar project-v1.jar in-out-files-for-demonstration/input/ex5-deck.txt in-out-files-for-demonstration/input/ex5-script.txt 4 > output/out5.txt

echo "=================================================="
echo "All done! Outputs saved in output/"
echo "=================================================="
