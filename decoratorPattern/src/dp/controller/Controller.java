// Hunter Knott, CS 3450, Utah Valley University

package dp.controller;
import java.io.*;
import java.util.function.Function;

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
		private static Writer so;
		private Output objectToDecorate;

		public BracketOutput(StreamOutput streamOutput) {
			super(so);
			BracketOutput.so = streamOutput.getSink();
			this.objectToDecorate = streamOutput;
		}

		@Override
	    public void write(Object o) {
	        String text = o.toString();
	        String[] lines = text.split("\\r?\\n");
	        try {
	            for (String line : lines) {
	                objectToDecorate.write("[" + line + "]\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	// NumberedOutput: this precedes each write with the current line number (1-based) right justified
	// in a field of width 5, followed by a colon and a space. (Don’t add a newline.)
	class NumberedOutput extends OutputDecorator {
		private static Writer so;
		private Output objectToDecorate;
		private int lineNumber = 1;
		
		public NumberedOutput(StreamOutput streamOutput) {
			super(so);
			NumberedOutput.so = streamOutput.getSink();
			this.objectToDecorate = streamOutput;
		}
		
		@Override
		public void write(Object o) {
			try {
				String text = o.toString();
				String[] lines = text.split("\\r?\\n");
				for (String line : lines) {
					objectToDecorate.write(String.format("%5d: %s\n", lineNumber++, line));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// TeeOutput: writes to two streams at a time; the one it wraps, plus one it receives as a
	// constructor argument
	class TeeOutput extends OutputDecorator {
		private static Writer  so;
		private Output objectToDecorate;
		private final Writer teeStream;
		
		public TeeOutput(StreamOutput streamOutput, Writer teeStream) throws IOException {
			super(so);
			this.teeStream = teeStream;
			this.objectToDecorate = streamOutput;
		}
		
		@Override
		public void write(Object o) throws IOException {
			String text = o.toString();
			try {
				objectToDecorate.write(text);
				objectToDecorate.write("\n");
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
	
	// FilterOutput: writes only those objects that satisfy a certain condition (unary predicate),
	// received as a constructor parameter
	class FilterOutput extends OutputDecorator {
		private static Writer so;
		private Output objectToDecorate;
		private Function<String, Boolean> test;
		
		public FilterOutput(StreamOutput streamOutput, Function<String, Boolean> predicate) {
			super(so);
			this.objectToDecorate = streamOutput;
			this.test = predicate;
		}
		
		@Override
	    public void write(Object o) {
	        String text = o.toString();
	        String[] lines = text.split("\\r?\\n");

	        for (String line : lines) {
	            if (test.apply(line)) {
	                try {
	                    objectToDecorate.write(line);
	                    objectToDecorate.write("\n");
	                } catch (IOException e) {
	                    throw new RuntimeException(e);
	                }
	            }
	        }
	    }
	}
	
	public void run() {
		try (BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in))) {
			FileWriter outputStream = new FileWriter("output.dat");
	        StreamOutput streamOutput = new StreamOutput(outputStream);
			
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
						streamOutput = new BracketOutput(streamOutput);
						break;
					case "2":
						streamOutput = new NumberedOutput(streamOutput);
						break;
					case "3":
						System.out.print("Give a file name for output to go: ");
						String newFile = scanner.readLine();
						FileWriter teeFileWriter = new FileWriter(newFile);
						streamOutput = new TeeOutput(streamOutput, teeFileWriter);
						break;
					case "4":
						System.out.println("Choose a predicate:");
						System.out.println(" (1) Line is not empty");
						System.out.println(" (2) Line length is greater than 40");
						System.out.println(" (3) Lines that have a given input");
						System.out.println(" (4) Lines that have a # symbol");
						System.out.print("Enter a choice: ");
						String predicateChoice = scanner.readLine();
						
						Function<String, Boolean> test;
						switch (predicateChoice) {
							case "1":
								test = str -> !str.isEmpty();
								streamOutput = new FilterOutput(streamOutput, test);
								break;
							case "2":
								test = str -> str.length() > 40;
								streamOutput = new FilterOutput(streamOutput, test);
								break;
							case "3":
								System.out.print("Choose a word to search:");
								String toFind = scanner.readLine();
								test = str -> str.contains(toFind);
								streamOutput = new FilterOutput(streamOutput, test);
								break;
							case "4":
								test = str -> str.contains("#");
								streamOutput = new FilterOutput(streamOutput, test);
								break;
							default:
								System.out.println("Invalid choice. Using default");
								test = obj -> true;
								streamOutput = new FilterOutput(streamOutput, test);
						}
						break;
					case "5":
						System.out.println("Applying...");
						
						System.out.println("Default readable file is named decorator.dat");
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