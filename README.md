# insight
Matthew Chow's Insight Coding Challenge Solution

The Problem:
Read and process (possibly large) files containing H1b visa information. 
The raw data may be found on the US Dept. of Labor website:
https://www.foreignlaborcert.doleta.gov/performancedata.cfm#dis
Text files containing the top ten states where work takes place and 
job titles for certified H1b visa applicants are created by this program.

This solution:
Given the size of the input data, it is desireable to have fast reading of each
file and fast search methods for each job title and state. For this reason,
I wrote a simple External Chain HashMap class to allow for data access in asymptotically 
constant time. The bulk of the time used by the data structure comes from resizing.
Here I have set the maximum load to 3, and the minimum load to .333.

Technical details:
This program follows the input/output structure described in 
the 2018 Insight Data Science challenge. This program takes csv input, with 
semicolon(;) delimiters. It could easily be modified to take other delimiters 
instead or as well, but is currently set only to take the specified format.

	Repository Structure:
		-git files
		-README
		-run.sh
		-input
			-Put all the input .csv's here
		-output
			-top_10_states.txt
			-top_10_occupations.txt
		-src
			-main.java     (Directs processing of files in input and writing of outputs)
			-ECHashMap.java  (HashMap class with external chaining using linked list)
			-Head.java   (Used for head of linked list, has String and int)
			-myUts.java  (Utility class)
			-Chain.java (Linked list class for external chaining)
		-insight_testsuite
			-temp
			-tests
				-test1
					-input(given)
					-output(given)
				-test2
					-myinput (Lines that originally tripped up my code, used in testing)
					-myoutput
			
		
		