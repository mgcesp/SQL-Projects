import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * Illustrates a sequential read from a text file
 * -- Prabakar 
 */

public class FileScan {

	public FileScan() throws Exception {
		String fileName, lineInfo;
		int lines = 0;
		Scanner console = new Scanner(System.in);

		System.out.print("\nEnter input file name: ");
		fileName = console.nextLine();
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				lineInfo = scanner.nextLine();
				lines++;
				System.out.println("Line " + lines + ": " + lineInfo);
			}
			scanner.close();
			System.out.println("Total no. of lines read: " + lines);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Test
	public static void main(String args[]) throws Exception {
		FileScan test = new FileScan();
		System.exit(0);
	}
}