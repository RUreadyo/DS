#!/usr/bin bash
echo "-compile start-"

# Compile
javac CalculatorTest.java

mkdir -p my_output

start=$SECONDS

echo "-execute your program-"


for i in $(seq 1 100)
do
	timeout 30 java CalculatorTest < ./testset/input/$i.txt > ./my_output/$i.txt
done

echo "Execution time : $((SECONDS-start)) seconds"

echo "-print wrong answers-"

for i in $(seq 1 100)
do
        cmp ./my_output/$i.txt ./testset/output/$i.txt

done
