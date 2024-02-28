package dp.controller;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	private static String readFile(InputStream name) {
		StringBuilder stringBuilder = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(name))) {
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
		} catch (IOException e) {
			System.err.println("Error reading the file: " + e.getMessage());
		}
		return stringBuilder.toString();
	}
	
	// BracketOutput: Surrounds each line with square brackets, and adds a newline to each.
	class BracketOutput extends OutputDecorator {

		public BracketOutput(Writer stream) {
			super(stream);
		}

		@Override
		public void write(Object o) {
			try {
				super.so.write("[" + o.toString() + "]\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			try {
				super.so.write(String.format("%5d: %s", lineNumber++, o.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// TeeOutput: writes to two streams at a time; the one it wraps, plus one it receives as a
	// constructor argument
	class TeeOutput extends OutputDecorator {
		private final Writer secondStream;
		
		public TeeOutput(Writer stream, String otherName) throws IOException {
			super(stream);
			secondStream = new FileWriter(otherName);
		}
		
		@Override
		public void write(Object o) {
			try {
				super.so.write(o.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			Writer outputStream = new FileWriter("output.dat");
			StreamOutput streamOutput = new StreamOutput(outputStream);
			
//			System.out.print("Test: ");
//			String testText = scanner.readLine();
//			scanner.close();
			
			List<OutputDecorator> decorators = new ArrayList<>();
			
			while (true) {
				System.out.println("Output Decorator Options:\n"
						+ " (1) Bracket Decorator\n"
						+ " (2) Numbered Decorator\n"
						+ " (3) Tee Decorator\n"
						+ " (4) Filter Decorator\n"
						+ " (5) Apply\n");
				System.out.print("Select an option: ");
				String choice = scanner.readLine();
				
				switch (choice) {
					case "1":
						streamOutput = new BracketOutput(outputStream);
						break;
					case "2":
						streamOutput = new NumberedOutput(outputStream);
						break;
					case "3":
						System.out.print("Give a file name for output to go: ");
						String newFile = scanner.readLine();
						streamOutput = new TeeOutput(outputStream, newFile);
						break;
					case "4":
						streamOutput = new FilterOutput(outputStream);
						break;
					case "5":
						System.out.println("Applying...");
						
						System.out.print("Enter a file to read: ");
						String fileName = scanner.readLine();
						
						InputStream inputStream = Controller.class.getResourceAsStream(fileName);
						if (inputStream != null) {
							System.out.println("Reading file " + fileName);
							String text = readFile(inputStream);
							System.out.println(text);
							streamOutput.write(text);
							scanner.close();
							outputStream.close();
						} else {
							System.out.println("No such file named " + fileName);
							return;
						}
						
						return;
					default:
						System.out.println("Input invalid. Enter a number between 1 and 5");
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading input: " + e.getMessage());
		}
	}
}