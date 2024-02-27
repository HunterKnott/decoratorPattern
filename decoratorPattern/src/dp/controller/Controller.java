package dp.controller;
import java.util.Scanner;

public class Controller {
	public void run() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a file name to read (must be .txt): ");
		String fileName = scanner.nextLine();
		System.out.println("You want to read " + fileName);
		scanner.close();
	}
}