/**
 * Bradley Taniguchi
 * 05/09/16
 * Virtual Memory Manager
 * 
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class VirtMM {
	//private int[] virtualMemory; //local addresses
	private byte[] physicalMemory; //holds items from BACKING_STORE.bin
	private int[] pageTable; //keeps track of pages loaded in mem
	private int numPages;
	private int pageSize; //or offset within page!
	private int lastPage;
	
	public VirtMM() {
		/*allocate memory for arrays*/
		this.pageSize = 256;
		this.numPages = 256;
		//this.virtualMemory = new int[numPages]; 
		this.physicalMemory = new byte[numPages*pageSize];
		this.pageTable = new int[numPages];
		this.lastPage = 0; 
		//initialize pageTable to -1 for empty
		initPageTable();
	} 
	/*Primary memory handler function, ADD HITS AND MISSES HERE*/
	public void handleMemory(String addressesName, String databaseName) {
		int pageFaults=0,tlbHits=0,numAddresses=0,phyAddress,offset, pagenum;
		System.out.println("Looking into addresses file defined by: " + addressesName);
		/*Read the address table*/
		ArrayList<Integer> list = new ArrayList<Integer>();
		list = getAddressTable(addressesName);
		if(list == null) { //nothing to look at ERROR!
			System.out.println("ERROR list is empty!");
			return; 
		}
		/*Now for each item in Arraylist, we handle memory*/
		numAddresses = list.size();
		for(Integer i : list) {
			offset = getOffset(i.intValue());
			pagenum = getPageNum(i.intValue());
			//System.out.println("BUFF1: pn:" + pagenum + " of:" + offset); //gives 66, which is PHYSICAL, needs to convert to logical
			
			
			/*First see if page number is not in pageTable*/
			if(pageTable[pagenum] == -1) {  
				/*If here its a page miss, load into memory*/
				byte[] page = getBackValue(databaseName, pagenum);
				
				/*With page of data, put it into memory*/
				setPhysicalMemory(page);
				
				/*Update faults, as not in memory*/
				pageFaults++;
				
				/*Get Physical Address of page, to print out later*/
				phyAddress = getLastPageAddress(); //add offset
				
				/*Set pageTablePos to the Physical address*/
				this.pageTable[pagenum] = getLastPageAddress();
				
				/*Update how many pages we have*/
				this.lastPage++; 
				
			} else { //our page is ALREADY in memory!
				/*We do not need to put item into memory, update hits*/
				tlbHits++;
				
				/*If we already have page in memory, read pageTable*/
				phyAddress = pageTable[pagenum]; //is this correct??
			}
			
			/*Now go fetch item in memory, and printout result*/
			int val = physicalMemory[phyAddress + offset]; //NOTE WE ARE PUTTING THINGS INTO MEMORY WRONG!
			printOut(i.intValue(), (phyAddress + offset), val);
			System.out.println("Offset: " + offset + " pageNum: " + pagenum);
			
		}
		System.out.println("DONE");
	}
	/*Reads the given file for addresses to load, returns all addresses*/
	private ArrayList<Integer> getAddressTable(String filename) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		File currDir = new File(".");
		File parDir = currDir.getParentFile();
		File file = new File(parDir, filename);
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch(FileNotFoundException e) {
			System.out.println("File not Found Exception for: " + filename);
			return null; //handle this elsewhere
		} 
		while(scanner.hasNextInt()) {
			Integer i = new Integer(scanner.nextInt());
			list.add(i);
		}
		scanner.close();
		return list;
	}
	/*sets all values in page table to -1*/
	private void initPageTable(){
		for(int i=0;i<pageSize;i++) {
			pageTable[i] = -1;
		}
	}
	/*Utility function to get offset given local address*/
	private int getOffset(int logicalAddress) {
		return logicalAddress % this.pageSize; //correct?
	}
	/*Utility function to get pagenumber given local address*/
	private int getPageNum(int logicalAddress) {
		return logicalAddress/this.numPages; //correct?
	}
	public void test(int logical) {
		int pagenum = this.getPageNum(logical);
		int offset = this.getOffset(logical);
		byte[] arr = getBackValue("BACKING_STORE.bin", pagenum);
		System.out.println("START TEST");
		for(int i=0;i<arr.length;i++) {
			System.out.println(i + " " + arr[i]);
			if(i == offset){
				System.out.println("Item at offset:" + offset + " value: " + arr[i]);
				break;
			}
		}
		System.out.println("END TEST");
	}
	/*Reads the BACKING_STORE.bin file starting at line position pos*/
	private byte[] getBackValue(String filename, int pos) {
		byte[] retArray = new byte[pageSize]; 
		File currDir = new File(".");
    	File parDir = currDir.getParentFile();
    	File realfile = new File(parDir, filename);
    	RandomAccessFile file;
    	try {
    		file = new RandomAccessFile(realfile, "r");
    	} catch(FileNotFoundException e) {
    		System.out.println("FILE NOT FOUND for: " + filename);
    		return null;
    	}
    	try {
    		for(int i=0;i<pageSize;i++) {
    			file.seek(pos+i); //go to position in file
        		retArray[i] =  file.readByte(); //read the line
    		}
    		file.close(); 
    	} catch(IOException e) {
    		System.out.println("IOException!");
    		retArray = null;
    	}
    	return retArray; 
	}
	/*Puts the page into physical memory, determined by page size
	 * FIRST FIT, increase counter*/
	private void setPhysicalMemory(byte[] page) {
		int startAddress = getLastPageAddress();
		System.out.println("Updating page: " + this.lastPage);
		for(int i=0;i<pageSize;i++) {
			if(this.lastPage == 2 || this.lastPage == 1 || this.lastPage == 0) { 
				System.out.println("#: PhysicalMemory: "+ (startAddress+i) + " value: " + page[i]);
			}
			//System.out.println("DEBUG: pM: " + (startAddress+i) + " " + page[i]); //debug print
			this.physicalMemory[startAddress + i] = page[i];
		}
	}
	/*Prints out calling function*/
	private void printOut(int virtual, int physical, int value) {
		System.out.println("Virtual address: " + virtual + " Physical address: " + physical + " Value: " + value);
	}
	/*Gets the latest physical address, according to firstFit*/
	private int getLastPageAddress() {
		return this.lastPage * pageSize;
	}
}
