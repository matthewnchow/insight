#!/bin/bash
# A bash script to compile and run single java class
# Java class main takes arguments ("CERTKEY, CATEGORY1_KEYWORDS, CATEGORY2_KEYWORDS, ... CATEGORYN_KEYWORDS")
# written by Matthew Chow

THE_CLASSPATH=
PROGRAM_NAME=Main.java
cd src
javac -classpath ".:${THE_CLASSPATH}" $PROGRAM_NAME

java main CERTIFIED WORK-STATE JOB-TITLE
