package assignment4;

import java.io.File;

public class MainMonteVerdeAs4 {

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
}
