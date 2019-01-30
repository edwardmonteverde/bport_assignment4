package assignment4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class TopoSortMonteVerde {

	Scanner scan;
	File outFileName;
	Writer writer;
	int numVerts, vert1, vert2, output, v;
	// LinkedList<Integer> edges;
	int[] inDegreeArr;
	LinkedList<Integer>[] adjList;
	StringBuilder testSB, outputSB;

	public TopoSortMonteVerde(File inFile) throws Exception {
		makeOutFile("TopoSortOutput.txt");
		sort(inFile);

	}

	public static void main(String args[]) {

		try {
			if (args.length > 0) {
				System.out.println("Processing file\n");
				new TopoSortMonteVerde(new File(args[0]));

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

	private void createDigraph(File inputFile) {
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

		numVerts = Integer.parseInt(scan.next());
		inDegreeArr = new int[numVerts + 1];
		adjList = new LinkedList[numVerts + 1];
		for (int i = 1; i <= numVerts; i++)
			adjList[i] = new LinkedList<Integer>();

		while (scan.hasNextInt()) {

			vert1 = Integer.parseInt(scan.next());
			vert2 = Integer.parseInt(scan.next());

			if (!(adjList[vert1].contains(vert2))) {
				adjList[vert1].addLast(vert2);
				inDegreeArr[vert2]++;
			}

		}
	}

	private void sort(File inputFile) {
		createDigraph(inputFile);

		/*
		 * TEST PRINT OUT TO VERIFY CORRECT INITIALIZATION
		 */

		testSB = new StringBuilder();

		for (int i = 1; i <= numVerts; i++) {
			// System.out.println(i + " points to:\t" + adjList[i].toString());
			testSB.append(i + " points to:\t" + adjList[i].toString() + System.lineSeparator());

		}

		testSB.append("InDegree Array:\t");

		for (int i = 1; i <= numVerts; i++) {
			testSB.append("[" + i + "] = " + inDegreeArr[i] + "\t");
		}

		System.out.println(testSB);

		/*
		 * END TEST PRINTS
		 */

		Stack<Integer> stack = new Stack<Integer>();

		for (int i = 1; i <= numVerts; i++) {
			if (inDegreeArr[i] == 0) {
				stack.push(i);
			}
		}

		int i = 1;
		outputSB = new StringBuilder();
		outputSB.append("TOPOLOGICAL SORTING:\t");

		while (!stack.isEmpty()) {

			output = stack.pop();
			outputSB.append(output + " ");

			i++;

			for (int x = 0; x < adjList[output].size(); x++) {
				v = adjList[output].get(x);
				inDegreeArr[v]--;
				if (inDegreeArr[v] == 0)
					stack.push(v);
			}
		}

		if (i > numVerts) {

			write(testSB.toString() + System.lineSeparator());
			write(outputSB.toString());
		} else {
			write("No output available, graph is cyclic");
		}

	}

	private void write(String string) {
		try {
			writer.write(string);
			writer.flush();
		} catch (IOException e) {
			System.exit(1);
		}

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
