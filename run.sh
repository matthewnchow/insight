#!/bin/bash
# A bash script to compile and run single java class
# Java class main takes arguments ("CERTKEY, CATEGORY1_KEYWORDS, CATEGORY2_KEYWORDS, ... CATEGORYN_KEYWORDS")
# written by Matthew Chow

cd src
make clean
make

java main CERTIFIED WORK-STATE SOC-NAME
