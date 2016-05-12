
public class VirtualMemoryManagerRunner {
	public static void main(String args[]) {
		String filename = "addresses.txt";
		String database = "BACKING_STORE.bin";
		
		//VirtMM v = new VirtMM();
		//v.handleMemory(filename, database);
		//v.test(62493); //given this address, get offset, page num and print out value
		VirtualMM v = new VirtualMM(filename, database);
		v.handleMemory();
		
		System.exit(0);
	}
}
