#!/bin/sh
#
min=0
max=0
sum=0
n=0
for i in `grep view_cart.jsp work/dyf.csv | grep -v nodyf | cut -d, -f 5`; do
	let "n+=1"
	if [ $min = "0" -o $i -lt $min ]; then
		min=$i
	fi
	if [ $max = "0" -o $i -gt $max ]; then
		max=$i
	fi
	let "sum+=$i"
done
avg=$[$sum / $n]

min_nodyf=0
max_nodyf=0
sum_nodyf=0
n=0
for i in `grep nodyf work/dyf.csv | cut -d, -f 5`; do
	let "n+=1"
	if [ $min_nodyf = "0" -o $i -lt $min_nodyf ]; then
		min_nodyf=$i
	fi
	if [ $max_nodyf = "0" -o $i -gt $max_nodyf ]; then
		max_nodyf=$i
	fi
	let "sum_nodyf+=$i"
done
avg_nodyf=$[$sum_nodyf / $n]

# echo $min $max $avg
# echo $min_nodyf $max_nodyf $avg_nodyf

echo Minimum difference: $[$min-$min_nodyf]
echo Max difference: $[$max-$max_nodyf]
echo Average difference: $[$avg-$avg_nodyf]
