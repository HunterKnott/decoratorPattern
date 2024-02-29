package dp.controller;

import java.io.IOException;
import java.io.Writer;

abstract class OutputDecorator extends StreamOutput {
	StreamOutput so;
	public abstract void write(Object o) throws IOException;
	
	public OutputDecorator(Writer stream) {
		super(stream);
	}
}