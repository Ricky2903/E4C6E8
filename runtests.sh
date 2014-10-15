#!/bin/bash

java -cp lib/antlr.jar:classes/ Micro testcases/input/test_combination.micro > tiny.asm
echo $(./tiny tiny.asm nostats) > tout.log
echo $(./tiny testcases/output/test_combination.out nostats) > gold.log
if [ "$(diff -b -B tout.log gold.log)" = "" ]
then
	echo "COMB: Pass"
else
	echo "COMB: Fail"
fi

java -cp lib/antlr.jar:classes/ Micro testcases/input/test_expr.micro > tiny.asm
echo $(./tiny tiny.asm nostats) > tout.log
echo $(./tiny testcases/output/test_expr.out nostats) > gold.log
if [ "$(diff -b -B tout.log gold.log)" = "" ]
then
	echo "EXPR: Pass"
else
	echo "EXPR: Fail"
fi

java -cp lib/antlr.jar:classes/ Micro testcases/input/test_mult.micro > tiny.asm
echo $(echo "5 6" | ./tiny tiny.asm nostats) > tout.log
echo $(echo "5 6" | ./tiny testcases/output/test_mult.out nostats) > gold.log
if [ "$(diff -b -B tout.log gold.log)" = "" ]
then
	echo "MULT: Pass"
else
	echo "MULT: Fail"
fi

#rm tiny.asm tout.log gold.log