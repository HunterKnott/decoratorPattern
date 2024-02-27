package dp.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Controller {
	private static void readFile(InputStream name) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(name))) {
			String line;
			ArrayList<String> lines = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.err.println("Error reading the file: " + e.getMessage());
		}
	}
	
	public void run() {
		try (BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.print("Enter a file to read: ");
			String fileName = scanner.readLine();
			
			InputStream inputStream = Controller.class.getResourceAsStream(fileName);
			if (inputStream != null) {
				System.out.println("Reading file " + fileName);
				readFile(inputStream);
			} else {
				System.out.println("No such file named " + fileName);
			}
		} catch (IOException e) {
			System.err.println("Error reading input: " + e.getMessage());
		}
	}
}