package dp.controller;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
				String text = o.toString();
				String[] lines = text.split("\\r?\\n");
				for (String line : lines) {
					super.so.write("[" + line + "]\n");
				}
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
				String text = o.toString();
				String[] lines = text.split("\\r?\\n");
				for (String line : lines) {
					super.so.write(String.format("%5d: %s\n", lineNumber++, line));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// TeeOutput: writes to two streams at a time; the one it wraps, plus one it receives as a
	// constructor argument
	class TeeOutput extends OutputDecorator {
		private final Writer teeStream;
		
		public TeeOutput(Writer stream, Writer teeStream) throws IOException {
			super(stream);
			this.teeStream = teeStream;
		}
		
		@Override
		public void write(Object o) throws IOException {
			String text = o.toString();
			try {
				super.so.write(text);
				super.so.write("\n");
				teeStream.write(text);
				teeStream.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				teeStream.close();
			}
		}
	}
	
	public interface predicate {
		public boolean execute(Object o);
	}
	
	// NumberedOutput: this precedes each write with the current line number (1-based) right justified
	// in a field of width 5, followed by a colon and a space. (Don’t add a newline.)
	class FilterOutput extends OutputDecorator {
		private final Predicate<Object> predicate;
		
		public FilterOutput(Writer stream, Predicate<Object> predicate) {
			super(stream);
			this.predicate = predicate;
		}
		
		@Override
		public void write(Object o) {
			if (predicate.test(o)) {
				try {
					super.so.write(o.toString());
					super.so.write("\n");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public void run() {
		try (BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in))) {
			Writer outputStream = new FileWriter("output.dat");
			StreamOutput streamOutput = new StreamOutput(outputStream);
			
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
						FileWriter teeFileWriter = new FileWriter(newFile);
						streamOutput = new TeeOutput(outputStream, teeFileWriter);
						break;
					case "4":
						System.out.println("Choose a predicate:");
						System.out.println(" (1) Length greater than 10");
						System.out.println(" (2) Contains 'Python'");
						System.out.print("Enter a choice: ");
						String predicateChoice = scanner.readLine();
						
						Predicate<Object> predicate;
						switch (predicateChoice) {
							case "1":
								predicate = obj -> obj.toString().length() > 10;
								break;
							case "2":
								predicate = obj -> obj.toString().contains("Python");
								break;
							default:
								System.out.println("Invalid choice. Using default");
								predicate = obj -> true;
						}
						
						streamOutput = new FilterOutput(outputStream, predicate);
						break;
					case "5":
						System.out.println("Applying...");
						
						System.out.print("Enter a file to read: ");
						String fileName = scanner.readLine();
						
						InputStream inputStream = Controller.class.getResourceAsStream(fileName);
						if (inputStream != null) {
							System.out.println("Reading file " + fileName);
							String text = readFile(inputStream);
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