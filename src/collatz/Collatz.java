package collatz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Collatz {

	// TODO
	// Which Inputs Match Return Step Count? Only i = 5
	// Ratio of index to run size?
	// Peak Values across a run?
	// Crossover Frequencies?
	// BigInteger XD
	
	static int batchSize = 100000; // Size of Mappings
	// Values of Individual Inputs (Rewritten Each Iteration)
	static int returnCount = 0; // Magnitude of Return Time
	static int returnRunCount = 1; // Magnitude of Run of Equal Return Steps
	static int peakValue = 1; // Magnitude of Peak
	static int peakRunCount = 1; // Magnitude of Run of Equal Peak Values
	// Max Values of All Inputs
	static int returnMax = 0; // Largest Steps to return to 1
	static int peakMax = 1; // Largest Peak Value
	// Mappings of Input Values to Properties
	static int[] returnMap = new int[batchSize + 1]; // ReturnCount Mapping (Input as Index, Return Step Count as Value)
	static int[] peakMap = new int[batchSize + 1]; // Peak Mapping (Input as Index, Peak of Sequence as Value)
	static HashMap<Integer,Integer> returnRunMap = new HashMap<>(); // Run Mapping (Input as Key, Amount of Consecutive Same Return Step Count as Value)
	static HashMap<Integer,Integer> peakRunMap = new HashMap<>(); // Peak Run Mapping (Input as Key, Amount of Consecutive Equal Peak of Sequence as Value)
	// Max Values of Mapped Properties
	static int returnRunMax = 1; // Largest Run of Consecutive Inputs With Same Return Steps
	static int peakRunMax = 1; // Largest Run of Consecutive Inputs with Same Peak Value
	
	public static void collatzCount(int k) { // Generate Collatz Data
		returnCount = 0;
		peakValue = 1;
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
		mapRuns();
		mapPeakRuns();
	}
	
	static void mapRuns(){ // Initialize Map of runs of consecutive inputs/same output sets
		for (int i = 1; i < batchSize; i++){
			returnRunCount = 1;
			if (returnMap[i] == returnMap[i + 1]){
				int startIndex = i;
				while (returnMap[i] == returnMap[i + 1]){
					returnRunCount++;
					i++;
				}
				if (returnRunCount > returnRunMax){ // Update Max Return Run if Return Run Count exceeds
					returnRunMax = returnRunCount;
				}
				returnRunMap.put(startIndex, returnRunCount);
			}
		}
	}
	
	static void mapPeakRuns(){ // Initialize Map of runs of consecutive inputs/peak value sets
		for (int i = 1; i < batchSize; i++){
			peakRunCount = 1;
			if (peakMap[i] == peakMap[i + 1]){
				int startIndex = i;
				while (peakMap[i] == peakMap[i + 1]){
					peakRunCount++;
					i++;
				}
				if (peakRunCount > peakRunMax){ // Update Max Peak Run if Peak Run Count exceeds
					peakRunMax = peakRunCount;
				}
				peakRunMap.put(startIndex, peakRunCount);
			}
		}
	}
	
	public static void produceCSV() throws FileNotFoundException{ // Print Data to CSV
		PrintWriter pw = new PrintWriter(new File("data.csv"));
		StringBuilder sb = new StringBuilder();
		sb.append("index,steps,peak\n");
		for (int i = 1; i < batchSize; i++){
			sb.append(i + "," + returnMap[i] + "," + peakMap[i] + "\n");
		}
		pw.write(sb.toString());
		pw.close();	
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		mapValues();
		produceCSV();
		
		System.out.println("The batch size is " + batchSize);
		System.out.println("The maximum steps to return to one is " +  returnMax + " in a batch size of " + batchSize);
		System.out.println("The maximum value attained in any return to one is " + peakMax);
		System.out.println("The maximum run of consecutive inputs with same steps of return is " + returnRunMax);
		System.out.println("The maximum run of consecutive inputs with same peak values is " + peakRunMax);	
	}
}
