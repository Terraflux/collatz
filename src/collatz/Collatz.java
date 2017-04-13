package collatz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Collatz {

	// TODO
	// Which keys match their values? Only i = 5
	// How many consecutive keys have the same value?
	// Ratio of index to run size?
	// BiMap for run data?
	// Peak Values?
	// Peak Values across a run?
	
	static int batchSize = 100000;
	static int[] mapping = new int[batchSize + 1];

	public static void main(String[] args) throws FileNotFoundException {

		int collatzMax = 0;
		// Initialize the mapping
		for (int i = 1; i < batchSize; i++) {
			mapping[i] = collatzCount(i, 0);
			if (collatzCount(i, 0) > collatzMax){
				collatzMax = collatzCount(i, 0);
			}
		}
		

		// Count runs of subsequent numbers
		int maxRun = 1;
		int maxRunIndex = 1;
		for (int i = 1; i < batchSize; i++) {
			int run = subsequentCount(i, 1);
			if (run > 1) {
				if (run == 20){
					System.out.println("At " + i + " a run of 10 occurs");
				}
				if (run > maxRun){
					maxRun = run;
					maxRunIndex = i;
				}
				i += run - 1;
			}
		}
		System.out.println("At " + maxRunIndex + " a run of " + maxRun + " occurs");
		
		// Produce the CSV Data File
		PrintWriter pw = new PrintWriter(new File("data.csv"));
		StringBuilder sb = new StringBuilder();
		sb.append("index,steps\n");
		for (int i = 1; i < batchSize; i++){
			sb.append(i + ",");
			sb.append(mapping[i]);
			sb.append("\n");
		}
		pw.write(sb.toString());
		pw.close();	
	}

	public static int collatzCount(int k, int count) {
		if (k == 1) {
			return count;
		} else if (k % 2 == 0) {
			return collatzCount(k / 2, count + 1);
		} else {
			return collatzCount(k * 3 + 1, count + 1);
		}
	}
	
	public static int subsequentCount(int index, int count){
		if (mapping[index] == (mapping[index + 1])){
			return subsequentCount(index + 1, count + 1);
		} else {
			return count;
		}
	}
}
