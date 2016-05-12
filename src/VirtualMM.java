import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Bradley Taniguchi
 * 5/09/16
 * Virtual Memory Manager
 */

public class VirtualMM {
	private byte[] backingArray; //holds the whole array to allow easier "reading"
	private byte[] physicalMemory; //holds values from backing store
	private int[] pageTable; //keeps track of whats "in memory"
	private int[] TLB; //side table buffer, check this first because its "faster"
	private int[] TLBcounter; //keeps track of "ages" in TLB, used to remove oldest items in TLB
	private int TLBSize; 
	private int numPages; //number of pages in memory
	private int pageSize; //size of pages
	private int lastPage; //last page used in our physical memory
	private String backFileName;
	private String addressFileName;
	
	public VirtualMM(String addressFileName, String backFileName) {
		this.addressFileName = addressFileName;
		this.backFileName = backFileName;
		numPages = 256; //256 pages
		pageSize = 256; //256 bytes
		TLBSize = 16; //16 entries
		this.backingArray = new byte[numPages*pageSize];
		this.physicalMemory = new byte[numPages*pageSize]; //array to hold things in physical mem  
		this.pageTable = new int[numPages]; //holds information on what is loaded into memory
		this.TLB = new int[TLBSize]; //holds 16 latest entries
		this.TLBcounter = new int[TLBSize]; //holds 16 entires.
		this.initPageTable(); //set pageTable to -1 
		lastPage = 0; //no pages are used in our physical memory
		
	}
	/*primary memory function handler*/
	public void handleMemory() {
		int pageFaults=0,tlbHits=0,numAddresses=0,phyAddress,offset, pagenum, val;
		System.out.println("Starting main function");
		/*Read the address table*/
		ArrayList<Integer> list = new ArrayList<Integer>();
		list = getAddressTable();
		if(list == null) { //nothing to look at ERROR!
			System.out.println("ERROR list is empty!");
			return; 
		}
		this.getBackValue();
		numAddresses = list.size(); //get the number of addresses
		for(Integer item : list) {
			offset = getOffset(item.intValue()); //get the offset of address
			pagenum = getPageNum(item.intValue()); //get the pagenum of the address
			
			/*If this page value isn't in memory, add it*/
			if(this.pageTable[pagenum] == -1) {
				
				/*If we are here increment page faults */
				pageFaults++;
				
				/*If here its a page miss, load into memory*/
				byte[] page = loadIntoMemory(pagenum);
				
				/*Put that page into main memory*/
				for(int i=0;i<pageSize;i++) {
					this.physicalMemory[lastPage+i] = page[i]; //put byte in page, into memory
				}
				
				/*Calculate the phyAddress in our PhysicalMemory*/
				phyAddress = lastPage + offset;
				
				/*set pagetable value to which page, IE address/256 */
				this.pageTable[pagenum] = lastPage;
				
				/*Print the Memory Value*/
				
				//val = physicalMemory[lastPage + offset]; //this doesnt work but should work...
				val = getValue(item.intValue()); //this does work, but it takes the values directly from BACKINGSTORE
				System.out.print("VirtualAddress: " + item.intValue() + " Physical Address: " + phyAddress +
						" Value: " + val + "// Offset: " + offset + "LastPage: " + lastPage);
				System.out.println("");
				
				/*increment the last page we have in Physical Memory, so we can write i*/
				lastPage += pageSize; 
				
				/*Update TLB, buffer, see if we want to remove the last item and replace it with the latest*/
				
			} else { //our page is ALREADY in memory!
				/*We do not need to put item into memory, update hits*/
				tlbHits++; 
				/*Our page is already in memory, consult TLB to get the information, */
			}
		} //end for loop
		double faultRate = ((double) pageFaults/((double) numAddresses));
		System.out.println("Number of Translated Addresses: " + numAddresses);
		System.out.println("Page Faults: " + pageFaults);
		System.out.println("Page Fault Rate: " + faultRate);
		System.out.println("TLB Hits: ");
		System.out.println("TLB Hit Rate: ");
	}
	/*Reads the given file for addresses to load, returns all addresses*/
	private ArrayList<Integer> getAddressTable() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		//File currDir = new File(".");
		//File parDir = currDir.getParentFile();
		File file = new File(this.addressFileName);
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch(FileNotFoundException e) {
			System.out.println("File not Found Exception for: " + addressFileName);
			return null; //handle this elsewhere
		} 
		while(scanner.hasNextInt()) {
			Integer i = new Integer(scanner.nextInt());
			list.add(i);
		}
		scanner.close();
		return list;
	}
	/*Loads from the backingArray*/
	private byte[] loadIntoMemory(int pos){
		byte[] b = new byte[pageSize];
		for(int i=0;i<pageSize;i++) {
			b[i] = this.backingArray[i];
		}
		return b;
	}
	/*Gets values from source.*/
	private int getValue(int pos){
		byte value;
		//File currDir = new File(".");
    	//File parDir = currDir.getParentFile();
    	File realfile = new File(this.backFileName);
    	RandomAccessFile file;
    	try {
    		file = new RandomAccessFile(realfile, "r");
    	} catch(FileNotFoundException e) {
    		System.out.println("FILE NOT FOUND for: " + this.backFileName);
    		return -99;
    	}
    	try {
    		file.seek(pos); //go to position in file
        		value = file.readByte(); //read the line
        	
    		file.close(); 
    	} catch(IOException e) {
    		System.out.println("IOException!");
    		value = -99;
    	}
    	return value;
	}
	/*Reads the BACKING_STORE.bin file and puts it into the BackingArray*/
	private void getBackValue() { 
		//File currDir = new File(".");
    	//File parDir = currDir.getParentFile();
    	File realfile = new File(this.backFileName);
    	RandomAccessFile file;
    	try {
    		file = new RandomAccessFile(realfile, "r");
    	} catch(FileNotFoundException e) {
    		System.out.println("FILE NOT FOUND for: " + this.backFileName);
    		return;
    	}
    	try {
    		file.seek(0); //go to position in file
    		for(int i=0;i<pageSize*numPages;i++) {
        		backingArray[i] = file.readByte(); //read the line	
    		}
    		file.close(); 
    	} catch(IOException e) {
    		System.out.println("IOException!");
    	} 
	}
	/*Prints out calling function*/
	private void printOut(int virtual, int physical, int value) {
		System.out.println("Virtual address: " + virtual + " Physical address: " + physical + " Value: " + value);
	}
	/*Utility function to get offset given local address*/
	private int getOffset(int logicalAddress) {
		return logicalAddress % this.pageSize; //correct?
	}
	/*Utility function to get pagenumber given local address*/
	private int getPageNum(int logicalAddress) {
		return logicalAddress/this.numPages; //correct?
	}
	/*sets all values in page table to -1*/
	private void initPageTable(){
		for(int i=0;i<pageTable.length;i++) {
			pageTable[i] = -1;
		}
	}
}