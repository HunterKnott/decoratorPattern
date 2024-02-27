package dp.controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Controller {
	private static void readFile(String name) {
		File file = new File(name);
		
		if (file.exists()) {
			try {
				Scanner fileScanner = new Scanner(file);
				
				System.out.println("Contents of " + name + ":");
				while (fileScanner.hasNextLine()) {
					System.out.println(fileScanner.nextLine());
				}
				
				fileScanner.close();
			} catch (FileNotFoundException e) {
				System.err.println("Error reading file: " + e.getMessage());
			}
		} else {
			System.err.println("File not found: " + name);
		}
	}
	
	public void run() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a file name to read with extension: ");
		String fileName = scanner.nextLine();
		System.out.println("You want to read " + fileName);
		scanner.close();
		
		readFile(fileName); // Not working
	}
}