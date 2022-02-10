#!/bin/bash

for scale in 5
do
for p in 4 5 6 7
do
for a in 1
do
				n=$((10**$p))
				n=`echo "$a * $n" | bc`
				bound=`echo "$n * $scale" | bc`
				n=${n%.*}
				bound=${bound%.*}
				for x in 'B' 'I' 'H' 'M' 'Q' 'R'
				do
								name=./.in_$n-$scale$x
								rm $name
								echo "r $n -$bound $bound" >> $name
								for i in 1 2 3 4
								do
												echo $x >> $name
								done
								echo X >> $name
				done
done
done
done
