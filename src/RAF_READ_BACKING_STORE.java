/**
 * RAF_READ_BS.java
 *
 * This program demonstrates how to read BACKING_STORE.bin provided in Project 5 * using the RandomAccessFile api
 * 
 * A random access file permits reads/writes to specific byte
 * positions in a file. The seek() method permits moving to
 * specific potions of the file to read or write. 
 * Note this also allows a file to have "holes" by seeking to
 * position N and then writing data. Bytes (0 ... N-1) are empty
 * (i.e. are a hole.)
 *
 * Usage:
 *	java RAF_READ_BACKING_STORE
 */

import java.io.*;

public class RAF_READ_BACKING_STORE
{
	public static void main(String[] args) throws java.io.IOException {

		// the file representing the simulated  disk
     		File fileName;
     		RandomAccessFile disk = null;
		byte val;

		try {
               		fileName = new File("BACKING_STORE.bin");
               		disk = new RandomAccessFile(fileName, "r");

			disk.seek(0);
			//65535
			for (int i = 0; i < 65535; i++) {
				val = disk.readByte();
				System.out.println("Virtual Address " + i + ": " + val);
				if(i ==  53683){
					break;
				}
				
			}
		}
          catch (IOException e) {
               System.err.println ("Unable to start the disk");
               System.exit(1);
          }
		finally {
			disk.close();
		}
	}
}
