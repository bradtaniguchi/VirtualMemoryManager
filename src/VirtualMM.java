import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class VirtualMM {
	/*Runner Function*/
	private int[] virtualMemory; //local addresses
	private int[] physicalMemory; //long term positions, WHAT Data type do I make this?
	private boolean[] pageTable; //mid range position
	
    public static void main(String args[]) {
        //String filename = "addresses.txt";
        if (args.length == 0 || args.length >= 2 ) { 
        	System.out.println("Wrong number of Arguments!!");
        	System.exit(1);
        } else {        	
        	String filename = args[0]; 
        
	        System.out.println("Starting VirtualMM with filename: " + filename);
	        VirtualMM run = new VirtualMM(filename);
	        
	        ArrayList<Integer> arr = new ArrayList<Integer>();
	        
	        try {
	            arr = run.getTable(filename);
	        } catch (FileNotFoundException e) {
	            System.out.println("Unable to read file!");
	        }
	        /*1. Tell VirtualMM to read the file
	         *2. Get the page number to SEE if on page table 
	         *3. IF not on page table move stuff from backing_store into physical memmory
	         *4. 
	         *
	         * 
	         * */
	        
        	run.checkAll(arr);
	        return;
        }
    } 
    /*Constructor we will use*/
    public VirtualMM(String filename) {
    	/*Create two arrays to handle*/
    	this.virtualMemory = new int[256];
    	this.physicalMemory = new int[256];
    	this.pageTable = new boolean[256];
    	/*Add onto here function logic to start program.*/
    }
    /*Reads in arr of numbers, gets the pagenumber, checks the tablelocation to see if checked
     * Primary usage function*/
    public void checkAll(ArrayList<Integer> arr) {
    	int offset, pagenum;
    	int tempStore; //change this name and type later
    	for(Integer i : arr) { 
    		/*get the offset*/
    		offset = getOffset(i.intValue());
    		pagenum = getPageNum(i.intValue());
    		
    		/*see if page number is already used*/
    		if(!pageTable[pagenum]) { //if false location, thus empty
    			//PAGE FAULT here!
    			flipTable(pagenum); //flip to being used!
    			//get the number from the backing file
    			tempStore = getBackValue("BACKING_STORE.bin", pagenum);
    			System.out.println("PN: " + pagenum +" T:" + tempStore); //print
    			
    			//Need to figure out the type and move this type into the Physical Array of memory.
    			
    		} else { //location is used!
    			//Later modify this to handle things in use...
    			System.out.println("LOCATION USED NOT PREPARED! " + pagenum);
    			return;
    		}
    	}
    }
    /*Utility function to flip boolean values in pageTable*/
    private void flipTable(int location) {
    	pageTable[location] = !pageTable[location];     	
    }
    /*Newer Version, doing things the easy way*/
    public static int getOffset(int input) {
    	return input % 256;
    }
    /*Newer Versions, doing things the easy way*/
    public static int getPageNum(int input) { 
    	return input / 256;
    }
    /*Utility Function that converts a BinaryString to an integer*/
    public static int BinaryStringtoInt(String s) {
        return Integer.parseInt(s,2);
    }
    /*Utility Function that changes string to BinaryInteger, for printing and debugging*/
    public String StringToBinaryString(String s) {
        String retValue, bin;
        try {
           bin = Integer.toBinaryString(Integer.parseInt(s));
           /*Below is to allow padding for 16-bit integers*/
           /*Format*////1011110100001111
           retValue = ("0000000000000000" + bin).substring(bin.length());
        }catch(NumberFormatException e) {
            System.out.println("NumberFormatexception in StringToBinaryInt!");
            return null;
        }
        return retValue;
    }
    /*Utility Function to read BACKING_STORE.bin file, and get information from it.*/
    public int getBackValue(String filename, int pos ){
    	int retValue;
    	File currDir = new File(".");
    	File parDir = currDir.getParentFile();
    	File realfile = new File(parDir, filename);
    	RandomAccessFile file;
    	try {	
    		file = new RandomAccessFile(realfile, "r");
    	} catch(FileNotFoundException e) {
    		System.out.println("File Not Found Exception for: " + filename);
    		return -1;
    	}
    	try {
    		file.seek(pos);
    		retValue = file.read(); //get byte at pos
    		//System.out.println("TEST "+ file.read());
    		file.close();
    	} catch(IOException e){
    		System.out.println("IOException!");
    		retValue = -1;
    	}
    	return retValue; //temp value
    	
    }
    /*Function to read file, gets a list of addresses and puts into array*/
    public ArrayList<Integer> getTable(String filename) throws FileNotFoundException {
        ArrayList<Integer> list = new ArrayList<Integer>(); //declare length after reading file
        File currDir = new File(".");
        File parDir = currDir.getParentFile();
        File file = new File(parDir,filename);
        Scanner scanner = new Scanner(file);
        
        while(scanner.hasNextInt()) {
            Integer i = new Integer(scanner.nextInt());
            list.add(i);
        }
        scanner.close();
        return list;
    }
    /*This is the output we WANT to use in the end*/
    public void printOut(int virtual, int physical) {
        /*Also need to out print the VALUE at these locations in memory, said to use chars?*/
        System.out.println("Virtual address: " + virtual + " Physical address: " + physical + " Value: ");
        
    }
    public void printTest(ArrayList<Integer> list) {
        String padString, ex;
        System.out.println("Read-In : Binary-Rep : Physical-Address");
        for(Integer n : list) {
            /*padString = ("00000" + n.toString()).substring(n.toString().length());
            ex = StringToBinaryString(n.toString()); //BinaryStringtoInt(ex.substring(8,16))
            physical = (BinaryStringtoInt(ex.substring(8,16))); 
            		//(BinaryStringtoInt(ex.substring(0,8)));
            System.out.println("R: " + padString + " B: " + ex + " P: " + physical);
            System.out.println("    T: " + BinaryStringtoInt(ex.substring(0,8)));*/
        	padString = ("00000" + n.toString()).substring(n.toString().length());
        	System.out.println("R: " + padString + " B: " + StringToBinaryString(n.toString()) + "/n" +  
        					   "  O: " + getOffset(n.intValue()) + " P: " + getPageNum(n.intValue()));
        }
        
    }
}
