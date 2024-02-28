package dp.controller;

import java.io.IOException;
import java.io.Writer;

abstract class OutputDecorator extends StreamOutput {
	Writer so;
	public OutputDecorator(Writer stream) { // Is this the right way to do it?
		super(stream);
		this.so = stream;
	}
	
//	public abstract void write(Object o) throws IOException;
	public abstract String decorate(String input);
}
