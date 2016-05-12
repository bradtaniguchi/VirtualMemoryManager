# VirtualMemoryManager
Bradley Taniguchi
Java programming Project for OpeRrating Systems 341.

####INSTRUCTIONS
Its best to import the project into Eclipse, to compile and run(optimal):   
1. `File -> Import.. -> General -> Existing Projects into Workspace`   
	Then let Eclipse Build the project automatically.
	  
2. To run hit Ctrl+F11   
	OR  
	`Run -> Run`    

To Compile and run from the command-line(less optimal):  
1. Navigate to the root directory of the Project (VirtualMemoryManager and run the following command to compile  
`javac -d bin src/*`

2. Manually move the addresses.txt, and the BACKING_STORE.bin files from the root, into the bin directory with command  
`mv addrresses.txt BACKING_STORE.BIN bin`

3. Run the program with:  
`java bin/VirtualMemoryManagerRunner`

---

####Files
File structure is normally as followed:    
bin - to hold compiled class files  
src - to hold source code files  
	VirtualMemoryManagerRunner.java - runner class, runs the program and holds filenames
	VirtualMM.java - main class, does the logic and main program.  
addresses.txt - list of addresses to access  
BACKING_STORE.bin - to represent memory.

---

####Prompt
Design a virtual memory manager. Page 458 of textbook 9th edition. Total 100 points.

Please submit a zip file titled "LastNameStudent1_LastNameStudent2_341_proj5.zip", in which please include a readme file as how to compile and run your program, any test output files you have, and the c/Java source programs. If you also implement extra credit, please submit two separate program and indicate which one is for extra credit.

Extra credits. Modifications on page 461. 20 points.

