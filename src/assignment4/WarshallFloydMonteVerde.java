package assignment4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.regex.Pattern;

public class WarshallFloydMonteVerde {

	Scanner scan;
	File outFileName;
	Writer writer;
	StringBuffer matrixSB, pathSB;
	int numVals, vert1, vert2, weight;
	int[][] matrix, predMatrix;

	public WarshallFloydMonteVerde(File inFile) throws Exception {
		makeOutFile("warshallfloydoutput.txt");
		shortestPath(inFile);

		// System.out.println("Print out initial predecessor matrix\n");
		// System.out.println(trimArray(predMatrix).replace("], ", "]\n") +
		// "\n");

		// System.out.println("Print out FINAL matrix with trim");
		// System.out.println(trimArray(matrix).replace("], ", "]\n") + "\n");
		//
		// System.out.println("Print out FINAL predecessor matrix\n");
		// System.out.println(trimArray(predMatrix).replace("], ", "]\n") +
		// "\n");

	}

	public static void main(String args[]) {

		try {
			if (args.length > 0) {
				System.out.println("Processing file\n");
				new WarshallFloydMonteVerde(new File(args[0]));

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

	private void constructWDAMatrix(File inFile) throws Exception {
		try {
			scan = new Scanner(inFile);
		} catch (Exception e) {
			System.out.println("Failed because" + e);
			System.exit(1);
		}

		// get to end of c's in file and begin reading ints

		Pattern pattern = Pattern.compile("c");
		while (scan.findInLine(pattern) != null)
			scan.nextLine();

		numVals = Integer.parseInt(scan.next());

		matrix = new int[numVals + 1][numVals + 1];
		predMatrix = new int[numVals + 1][numVals + 1];

		for (int i = 1; i <= numVals; i++) {
			matrix[i][i] = 0;
			for (int j = 1; j <= numVals; j++) {
				if (i != j)
					matrix[i][j] = Integer.MAX_VALUE;

			}
		}

		// verify matrix initializes correctly
		// System.out.println("Verify matrix was created correctly\n");
		// System.out.println(Arrays.deepToString(matrix).replace("], ", "]\n")
		// + "\n");

		while (scan.hasNextLine()) {
			vert1 = Integer.parseInt(scan.next());
			vert2 = Integer.parseInt(scan.next());
			weight = Integer.parseInt(scan.next());

			matrix[vert1][vert2] = weight;
			predMatrix[vert1][vert2] = vert1;

		}

		// System.out.println("Print out initial predecessor matrix
		// TEST!!!!!!!!!!!!!!!!!\n");
		// System.out.println(Arrays.deepToString(predMatrix).replace("], ",
		// "]\n") + "\n");
		//
		// // TEST TO VERIFY MATRIX PRINTS CORRECTLY
		//
		// // print without trimming
		// System.out.println("Verify matrix was initialized with values
		// correctly\n");
		// System.out.println(Arrays.deepToString(matrix).replace("], ", "]\n")
		// + "\n");
		//
		// // print with trim
		// System.out.println("Matrix after trimming\n");
		// System.out.println(trimArray(matrix).replace("], ", "]\n") + "\n");
		write("Print initial matrix" + System.lineSeparator());
		write(printMatrix(matrix) + System.lineSeparator());

		write("Print initial predecessor matrix" + System.lineSeparator());
		write(printMatrix(predMatrix) + System.lineSeparator());

	}

	private int[][] shortestPath(File inputFile) throws Exception {

		constructWDAMatrix(inputFile);

		// Step 2: for k = 1..n do {
		for (int k = 1; k <= numVals; k++) {
			// Step 2.1: for i = 1..n do
			for (int i = 1; i <= numVals; i++) {
				// Step 2.2: for j = 1..n do
				for (int j = 1; j <= numVals; j++) {
					if ((matrix[i][k] != Integer.MAX_VALUE) && (matrix[k][j] != Integer.MAX_VALUE)) {
						if ((matrix[i][k] + matrix[k][j]) < matrix[i][j]) {
							matrix[i][j] = (matrix[i][k] + matrix[k][j]);
							predMatrix[i][j] = k;

						}
					}
				}
			}
		}

		// System.out.println("Matrix after trimming\n");
		// System.out.println(trimArray(matrix).replace("], ", "]\n") + "\n");

		write("Print final matrix" + System.lineSeparator());
		write(printMatrix(matrix) + System.lineSeparator());

		write("Print final predecessor matrix" + System.lineSeparator());
		write(printMatrix(predMatrix) + System.lineSeparator());

		write("Print path" + System.lineSeparator());
		write(path(2, 5) + System.lineSeparator());
		// Step 3: return D
		return matrix;

	}

	private String path(int i, int j) {
		pathSB = new StringBuffer();

		// if (i == j)
		// return i + "";
		// else if (predmatrix i j != 0)
		// return path ( i , predmatrix ij _ "" + " -> + j;

		pathSB.append(j + "-");
		while (predMatrix[i][j] != 0) {
			j = predMatrix[i][j];
			pathSB.append(j + "-");
		}

		pathSB.append(i);
		return (pathSB.reverse().toString());

	}

	private String printMatrix(int[][] matrix) {
		matrixSB = new StringBuffer();

		for (int i = 1; i <= numVals; i++) {
			for (int j = 1; j <= numVals; j++) {

				if (matrix[i][j] == Integer.MAX_VALUE)
					matrixSB.append("INF\t");
				else
					matrixSB.append(matrix[i][j] + "\t");
			}
			matrixSB.append(System.lineSeparator());
		}
		return matrixSB.toString();
	}

	private void write(String string) {
		try {
			writer.write(string);
			writer.flush();
		} catch (IOException e) {
			System.exit(1);
		}

	}
	//
	// private String trimArray(int[][] array) {
	// int[][] temp = new int[array.length - 1][array.length - 1];
	// for (int i = 0; i < array.length - 1; i++) {
	// for (int j = 0; j < array.length - 1; j++) {
	// temp[i][j] = array[i + 1][j + 1];
	// }
	// }
	//
	// return (Arrays.deepToString(temp));
	// }

	private void makeOutFile(String outFile) {
		outFileName = new File(outFile);
		try {
			writer = new PrintWriter(outFileName);
		} catch (FileNotFoundException e) {
			System.exit(1);
		}
	}
}

// package assignment4;
//
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.io.PrintWriter;
// import java.io.Writer;
// import java.util.Arrays;
// import java.util.Scanner;
// import java.util.regex.Pattern;
//
// public class WarshallFloydMonteVerde {
//
// Scanner scan;
// File outFileName;
// Writer writer;
// int numVals, vert1, vert2, weight;
// int[][] matrix;
// int[][] temp;
// int[][] newMatrix;
//
// public WarshallFloydMonteVerde(File inFile) throws Exception {
// makeOutFile("warshallfloydoutput.txt");
// shortestPath(inFile);
//
// // Print out value/weight array, # of elements, max sack size, and max
// // value
//
// // write("Printing weight array:\t" +
// // (Arrays.toString(matrix)).replace("[", "").replace("]", "")
// // + System.lineSeparator());
// // write("Number of elements in set is : " + numVals +
// // System.lineSeparator());
//
// }
//
// public static void main(String args[]) {
//
// try {
// if (args.length > 0) {
// System.out.println("Processing file\n");
// System.out.println("----- PART 3: WARSHALL/FLOYD'S ALGORITHM -----\n");
// new WarshallFloydMonteVerde(new File(args[0]));
//
// } else {
// System.out.println("No file enteredddd.");
// System.exit(1);
// }
//
// } catch (Exception e) {
// System.out.println("Fail " + e);
// e.printStackTrace();
//
// System.exit(1);
// }
//
// }
//
// private void constructWDAMatrix(File inFile) throws Exception {
// try {
// scan = new Scanner(inFile);
// } catch (Exception e) {
// System.out.println("Failed because" + e);
// System.exit(1);
// }
//
// // get to end of c's in file and begin reading ints
//
// Pattern pattern = Pattern.compile("c");
// while (scan.findInLine(pattern) != null)
// scan.nextLine();
//
// numVals = Integer.parseInt(scan.next());
//
// matrix = new int[numVals + 1][numVals + 1];
// for (int i = 1; i <= numVals; i++) {
// matrix[i][i] = 0;
// for (int j = 1; j <= numVals; j++) {
// if (i != j)
// matrix[i][j] = 99;
// }
// }
//
// // verify matrix initializes correctly
// System.out.println("Verify matrix was created correctly\n");
// System.out.println(Arrays.deepToString(matrix).replace("], ", "]\n") + "\n");
//
// while (scan.hasNextLine()) {
// vert1 = Integer.parseInt(scan.next());
// vert2 = Integer.parseInt(scan.next());
// weight = Integer.parseInt(scan.next());
//
// matrix[vert1][vert2] = weight;
//
// }
//
// // TEST TO VERIFY MATRIX PRINTS CORRECTLY
//
// // print without trimming
// System.out.println("Verify matrix was initialized with values correctly\n");
// System.out.println(Arrays.deepToString(matrix).replace("], ", "]\n") + "\n");
//
// // print with trim
// System.out.println("Matrix after trimming\n");
// // trimArray(matrix);
// // printFinal(temp));
// System.out.println(trimArray(matrix).replace("], ", "]\n") + "\n");
// }
//
// private void shortestPath(File inputFile) throws Exception {
//
// constructWDAMatrix(inputFile);
//
//// Step 1: D = W
// newMatrix = matrix;
//// Step 2: for k = 1..n do
//// Step 2.1: for i = 1..n do
//// Step 2.2: for j = 1..n do
//// dij = min{dij, dik + dkj );
//// Step 3: return D
//
//
//
// }
//
// private void write(String string) {
// try {
// writer.write(string);
// writer.flush();
// } catch (IOException e) {
// System.exit(1);
// }
//
// }
//
// private String trimArray(int[][] array) {
// temp = new int[array.length - 1][array.length - 1];
// for (int i = 0; i < array.length - 1; i++) {
// for (int j = 0; j < array.length - 1; j++) {
// temp[i][j] = array[i + 1][j + 1];
// }
// }
//
// return (Arrays.deepToString(temp));
// }
//
// public void printFinal(int[][] array) {
// int[][] finalArray = array;
//
// // print out indices
// System.out.print(" \t0\t");
// for (int i = 1; i <= numVals; i++)
// System.out.print("" + i + "\t");
// System.out.println("");
//
// // print out array, skip the first and last rows of empty zeroes
// for (int i = 1; i <= numVals; i++) {
// System.out.print(" " + i + "\t");
// System.out.println(Arrays.toString(finalArray[i]).replace(", ",
// "\t").replace("[", "").replace("]", ""));
//
// }
// }
//
// private void makeOutFile(String outFile) {
// outFileName = new File(outFile);
// try {
// writer = new PrintWriter(outFileName);
// } catch (FileNotFoundException e) {
// System.exit(1);
// }
// }
// }
