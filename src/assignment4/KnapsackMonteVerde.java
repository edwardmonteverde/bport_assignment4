package assignment4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class KnapsackMonteVerde {

	Scanner scan;
	File outFileName;
	Writer writer;
	int card, maxSack, maxValue;
	int[] values;
	int[] weights;
	int[][] matrix;
	LinkedList<Integer> optimalSet, optimalValues;

	public KnapsackMonteVerde(File inFile) throws Exception {
		makeOutFile("knapsackoutput.txt");
		createKnapsackArray(inFile);
		maxValue = makeKnapsack(values, weights, card, maxSack);

		// Print out value/weight array, # of elements, max sack size, and max
		// value
		write("Printing value array:\t" + trimArray(values) + System.lineSeparator());
		write("Printing weight array:\t" + trimArray(weights) + System.lineSeparator());
		write("Number of elements in set is : " + card + System.lineSeparator());
		write("Max sack size is : " + maxSack + System.lineSeparator());
		write("Max value is " + maxValue + System.lineSeparator());

		// Write optimal element set
		write("Optimal element set is " + Arrays.toString(optimalSet.toArray()).replace("[", "").replace("]", "")
				+ System.lineSeparator());

		// Write optimal value set
		write("Optimal value set is " + Arrays.toString(optimalValues.toArray()).replace("[", "").replace("]", "")
				+ System.lineSeparator());

	}

	public static void main(String args[]) {

		try {
			if (args.length > 0) {
				System.out.println("Processing file\n");
				System.out.println("----- PART 2: 0/1 KNAPSACK PROBLEM -----\n");
				new KnapsackMonteVerde(new File(args[0]));

			} else {
				System.out.println("No file enteredddd.");
				System.exit(1);
			}

		} catch (Exception e) {
			System.out.println("Fail " + e);
			e.printStackTrace();

			System.exit(1);
		}

	}

	private void createKnapsackArray(File inputFile) throws Exception {
		try {
			scan = new Scanner(inputFile);
		} catch (Exception e) {
			System.out.println("Failed because" + e);
			System.exit(1);
		}

		// get to end of c's in file and begin reading ints

		Pattern pattern = Pattern.compile("c");
		while (scan.findInLine(pattern) != null)
			scan.nextLine();

		card = Integer.parseInt(scan.next());
		maxSack = Integer.parseInt(scan.next());

		weights = new int[card + 1];
		values = new int[card + 1];

		// populate weights array
		for (int x = 1; x <= card; x++) {
			weights[x] = Integer.parseInt(scan.next());
		}

		// populate values array
		for (int x = 1; x <= card; x++) {
			values[x] = Integer.parseInt(scan.next());
		}

	}

	public int makeKnapsack(int[] valueArray, int[] weightArray, int cardinalityOfElements, int maxSackSize) {

		/*
		 * Cardinality of set of values = card = n Weight of each element
		 * =weights[i] = w(i) Value of each element = values[i] = v(i) Maximum
		 * sack size = maxSack = W
		 */
		matrix = new int[card + 1][maxSack + 1];

		for (int k = 0; k < maxSack; k++) {
			matrix[0][k] = 0;
		}

		for (int i = 1; i <= card; i++) {
			for (int j = 1; j <= maxSack; j++) {
				if (j >= weights[i]) {

					matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i - 1][j - weights[i]] + values[i]);
				} else
					matrix[i][j] = matrix[i - 1][j];
			}
		}
		// TEST TO VERIFY MATRIX PRINTS CORRECTLY
		System.out.println(Arrays.deepToString(matrix).replace("], ", "]\n") + "\n");

		// Get optimal sets
		int j = maxSack;

		optimalSet = new LinkedList<Integer>();
		optimalValues = new LinkedList<Integer>();

		for (int i = card; i > 0; i--) {
			if (matrix[i][j] > matrix[i - 1][j]) {
				optimalSet.add(i);
				optimalValues.add(values[i]);

				j = j - weights[i];
			}
		}

		return matrix[card][maxSack];
	}

	private void write(String string) {
		try {
			writer.write(string);
			writer.flush();
		} catch (IOException e) {
			System.exit(1);
		}

	}

	private String trimArray(int[] array) {
		int[] temp = new int[array.length - 1];
		for (int i = 0; i < array.length - 1; i++) {
			temp[i] = array[i + 1];
		}

		return (Arrays.toString(temp));
	}

	private void makeOutFile(String outFile) {
		outFileName = new File(outFile);
		try {
			writer = new PrintWriter(outFileName);
		} catch (FileNotFoundException e) {
			System.exit(1);
		}
	}
}
