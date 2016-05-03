import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class VirtualMM {
	/*Runner Function*/
	public int[] virtualMemory;
	public int[] physicalMemory;
    public static void main(String args[]) {
        //String filename = "addresses.txt";
        if (args.length == 0 || args.length >= 2 ) { 
        	System.out.println("Wrong number of Arguments!!");
        	System.exit(1);
        } else {        	
        	String filename = args[0]; 
        
	        System.out.println("Starting VirtualMM with filename: " + filename);
	        VirtualMM run = new VirtualMM();
	        
	        ArrayList<Integer> arr = new ArrayList<Integer>();
	        run.virtualMemory = new int[256];
	        run.physicalMemory = new int[256];
	        
	        try {
	            arr = run.getTable(filename);
	        } catch (FileNotFoundException e) {
	            System.out.println("Unable to read file!");
	        }
	        //System.out.println(run.getRightmost(run.StringToBinaryString(arr.get(1).toString()),8,0));
	        run.printTest(arr);
	        return;
        }
    }
    public 
    /*Newer Version, doing things the easy way*/
    public int getOffset(int input) {
    	return input % 256;
    }
    /*Newer Versions, doing things the easy way*/
    public int getPageNum(int input) { 
    	return input / 256;
    }
    /*Utility Function that converts a BinaryString to an integer*/
    public int BinaryStringtoInt(String s) {
        return Integer.parseInt(s,2);
    }
    /*Utility Function that changes string to BinaryInteger*/
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
    public int getBackValue(String filename, int pos ) throws FileNotFoundException {
    	int retValue;
    	File currDir = new File(".");
    	File parDir = currDir.getParentFile();
    	File realfile = new File(parDir, filename);
    	RandomAccessFile file = new RandomAccessFile(realfile, "r");
    	
    	/*Add Logic here!*/
    	return -1; //temp value
    	
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
