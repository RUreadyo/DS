#!/bin/bash

rm ./result.txt

for data in `pwd`/.in_*
do
	name=`basename $data` 
	n=${name:4:-2}
	x=${name: -1}
	echo -n -e "\n$name " >> result.txt
	echo "$n $x"
	res=`timeout 30 java SortingTest < $data`
	res2=`echo $res | rev | cut -f2 -d " " | rev`
	echo -n $res2 >> result.txt
done
