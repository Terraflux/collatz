package collatz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Collatz {

	// TODO
	// Which keys match their values? Only i = 5
	// How many consecutive keys have the same value?
	// Ratio of index to run size?
	// BiMap for run data?
	// Peak Values across a run?
	
	static int batchSize = 100000; // Size of Mappings
	static int returnCount = 0; // Magnitude of Return Time
	static int runCount = 1; // Magnitude of Run
	static int peakValue = 1; // Magnitude of Peak
	
	static int returnMax = 0; // Largest Steps to return to 1
	static int runMax = 1; // Largest Run of Consecutive Inputs With Same Output
	static int peakMax = 1; // Largest peak value
	
	static int[] returnMap = new int[batchSize + 1]; // ReturnCount Mapping
	static int[] peakMap = new int[batchSize + 1]; // Peak Mapping
	static HashMap<Integer,Integer> runMap = new HashMap<>(); // Run Mapping
	

	static void mapValues(){ // Initialize array of return times to index as input
		for (int i = 1; i < batchSize + 1; i++) {
			collatzCount(i);
			returnMap[i] = returnCount;
			peakMap[i] = peakValue;
			if (returnCount > returnMax){ // Update returnMax if returnCount exceeds
				returnMax = returnCount;
			}
			if (peakValue > peakMax){ // Update peakMax if peakValue exceeds
				peakMax = peakValue;
			}
		}
	}
	
	static void mapRuns(){ // Initialize Map of runs
		for (int i = 1; i < batchSize + 1; i++){
			runCount = 1;
			if (returnMap[i] == returnMap[i + 1]){
				int startIndex = i;
				while (returnMap[i] == returnMap[i + 1]){
					runCount++;
					i++;
				}
				runMap.put(startIndex, runCount);
			}
		}
	}
	
	public static void collatzCount(int k) { // Generate Collatz Data
		returnCount = 0;
		while (k != 1){
			if (k > peakValue){
				peakValue = k;
			}
			if (k % 2 == 0){
				k /= 2;
				returnCount++;
			} else {
				k = k * 3 + 1;
				returnCount++;
			}
		}
	}
	
	public static void produceCSV() throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(new File("data.csv"));
		StringBuilder sb = new StringBuilder();
		sb.append("index,steps\n");
		for (int i = 1; i < batchSize; i++){
			sb.append(i + ",");
			sb.append(returnMap[i]);
			sb.append("\n");
		}
		pw.write(sb.toString());
		pw.close();	
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		mapValues();
		produceCSV();
		
		System.out.println("The batch size is " + batchSize);
		System.out.println("The maximum steps to return to zero is " +  returnMax + " in a batch size of " + batchSize);
		System.out.println("The maximum value attained in any return to zero is " + peakMax);
		
		
	}


}
