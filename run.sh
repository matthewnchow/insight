#!/bin/bash
# A bash script to compile and run single java class
# Java class main takes arguments ("CERTKEY, CATEGORY1_KEYWORDS, CATEGORY2_KEYWORDS, ... CATEGORYN_KEYWORDS")
# written by Matthew Chow

cd src/insightSolution

javac Head.java
javac ECHashMap.java
javac myUts.java
javac main.java

java insightSolution/main CERTIFIED WORK-STATE JOB-TITLE
