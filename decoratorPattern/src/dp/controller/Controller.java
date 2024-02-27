package dp.controller;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	// BracketOutput: Surrounds each line with square brackets, and adds a newline to each.
	class BracketOutput extends OutputDecorator {

		public BracketOutput(Writer stream) {
			super(stream);
		}

		@Override
		public void write(Object o) {
			super.so.write("[" + o.toString() + "]\n");
		}
	}
	
	// NumberedOutput: this precedes each write with the current line number (1-based) right justified
	// in a field of width 5, followed by a colon and a space. (Don’t add a newline.)
	class NumberedOutput extends OutputDecorator {
		private int lineNumber = 1;
		
		public NumberedOutput(Writer stream) {
			super(stream);
		}
		
		@Override
		public void write(Object o) {
			super.so.write(String.format("%5d: %s", lineNumber++, o.toString()));
		}
	}
	
	// TeeOutput: writes to two streams at a time; the one it wraps, plus one it receives as a
	// constructor argument
	class TeeOutput extends OutputDecorator {
		private final Writer secondStream;
		String otherName = "output2.txt";
		
		public TeeOutput(Writer stream) throws IOException {
			super(stream);
			secondStream = new FileWriter(otherName);
		}
		
		@Override
		public void write(Object o) {
			super.so.write(o.toString());
			try {
				secondStream.write(o.toString());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public interface predicate {
		public boolean execute(Object o);
	}
	
	// NumberedOutput: this precedes each write with the current line number (1-based) right justified
	// in a field of width 5, followed by a colon and a space. (Don’t add a newline.)
	class FilterOutput extends OutputDecorator {
		
		public FilterOutput(Writer stream) {
			super(stream);
		}
		
		@Override
		public void write(Object o) {
			
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
			
			Writer outputStream = new FileWriter("output.txt");
			StreamOutput streamOutput = new StreamOutput(outputStream);
			
			while (true) {
				OutputDecorator dec = null;
				
				System.out.println("Output Decorator Options:\n"
						+ " (1) Bracket Decorator\n"
						+ " (2) Numbered Decorator\n"
						+ " (3) Tee Decorator\n"
						+ " (4) Filter Decorator\n"
						+ " (5) Quit\n");
				System.out.print("Select an option: ");
				String choice = scanner.readLine();
				
				switch (choice) {
					case "1":
						dec = new BracketOutput(outputStream);
					case "2":
						dec = new NumberedOutput(outputStream);
					case "3":
						dec = new TeeOutput(outputStream);
					case "4":
						dec = new FilterOutput(outputStream);
					case "5":
						System.out.println("Quitting...");
						scanner.close();
						outputStream.close();
						return;
					default:
						System.out.println("Input invalid. Enter a number between 1 and 5");
				}
				
				// Add in decorator to some sequence
			}
		} catch (IOException e) {
			System.err.println("Error reading input: " + e.getMessage());
		}
	}
}