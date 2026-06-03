#!/bin/sh

# create the output folder if needed
mkdir -p DEMO-v1/output

echo "=================================================="
echo "Example 1"
echo "=================================================="
java -jar project-v1.jar DEMO-v1/input/ex1-deck.txt DEMO-v1/input/ex1-script.txt 2 > DEMO-v1/output/out1.txt

echo "=================================================="
echo "Example 2"
echo "=================================================="
java -jar project-v1.jar DEMO-v1/input/ex2-deck.txt DEMO-v1/input/ex2-script.txt 3 > DEMO-v1/output/out2.txt

echo "=================================================="
echo "Example 3"
echo "=================================================="
java -jar project-v1.jar DEMO-v1/input/ex3-deck.txt DEMO-v1/input/ex3-script.txt 4 > DEMO-v1/output/out3.txt

echo "=================================================="
echo "Example 4"
echo "=================================================="
java -jar project-v1.jar DEMO-v1/input/ex4-deck.txt DEMO-v1/input/ex4-script.txt 5 > DEMO-v1/output/out4.txt

echo "=================================================="
echo "Example 5"
echo "=================================================="
java -jar project-v1.jar DEMO-v1/input/ex5-deck.txt DEMO-v1/input/ex5-script.txt 6 > DEMO-v1/output/out5.txt

echo "=================================================="
echo "All done! Outputs saved in output/"
echo "=================================================="
