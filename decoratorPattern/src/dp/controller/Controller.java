package dp.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
	private static void printMenu() {
		System.out.println("Output Decorator Options:\n"
				+ " (1) Bracket Decorator\n"
				+ " (2) Numbered Decorator\n"
				+ " (3) Tee Decorator\n"
				+ " (4) Filter Decorator\n"
				+ " (5) Quit\n");
	}
	
	private static void makeDecorator(String type) {
		if (type.equals("1")) {
			// BracketOutput: Surrounds each line with square brackets, and adds a newline to each.
			
			class BracketOutput extends OutputDecorator {

				public BracketOutput(Writer stream) {
					super(stream);
				}

				@Override
				public void write(Object o) {
					
					
				}
			}
		}
		else if (type.equals("2")) {
			// NumberedOutput: this precedes each write with the current line number (1-based) right justified
			// in a field of width 5, followed by a colon and a space. (Don’t add a newline.)
			
			class NumberedOutput extends OutputDecorator {
				
				public NumberedOutput(Writer stream) {
					super(stream);
				}
				
				@Override
				public void write(Object o) {
					
				}
			}
		}
		else if (type.equals("3")) {
			// TeeOutput: writes to two streams at a time; the one it wraps, plus one it receives as a
			// constructor argument
			
			class TeeOutput extends OutputDecorator {
				
				public TeeOutput(Writer stream) {
					super(stream);
				}
				
				@Override
				public void write(Object o) {
					
				}
			}
		}
		else if (type.equals("4")) {
			// FilterOutput: writes only those objects that satisfy a certain condition (unary predicate),
			// received as a constructor parameter.
			
			class FilterOutput extends OutputDecorator {
				
				public FilterOutput(Writer stream) {
					super(stream);
				}
				
				@Override
				public void write(Object o) {
					
				}
			}
		}
		else {
			System.out.println("Invalid type");
			return;
		}
	}
	
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
			if (inputStream != null) { // Eventually move this to after the decorators are chosen
				System.out.println("Reading file " + fileName);
				readFile(inputStream);
			} else {
				System.out.println("No such file named " + fileName);
				return;
			}
			
			while (true) {
				printMenu();
				System.out.print("Select an option: ");
				String choice = scanner.readLine();
				
				List<String> decNumbers = Arrays.asList("1", "2", "3", "4");
				if (decNumbers.contains(choice)) {
					System.out.println("Adding Decorator");
				}
				else if (choice.equals("5")) {
					System.out.println("Quitting...");
					scanner.close();
					return;
				}
				else {
					System.out.println("Input not valid. Enter a number between 1 and 5");
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading input: " + e.getMessage());
		}
	}
}