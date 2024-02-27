package dp.controller;

import java.io.Writer;

public abstract class OutputDecorator extends StreamOutput {
	public OutputDecorator(Writer stream) { // Is this the right way to do it?
		super(stream);
	}
	
	StreamOutput so;
	public abstract void write(Object o);
}

//1. BracketOutput: Surrounds each line with square brackets, and adds a newline to each.

//2. NumberedOutput: this precedes each write with the current line number (1-based) right justified in a
// field of width 5, followed by a colon and a space. (Don’t add a newline.)

//3. TeeOutput: writes to two streams at a time; the one it wraps, plus one it receives as a
//constructor argument

//4. FilterOutput: writes only those objects that satisfy a certain condition (unary predicate),
//received as a constructor parameter.

//public interface Predicate {
//	 public boolean execute(Object o); // used by FilterOutput
//	}
