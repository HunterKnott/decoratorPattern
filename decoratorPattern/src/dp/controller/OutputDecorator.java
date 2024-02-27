package dp.controller;

import java.io.Writer;

abstract class OutputDecorator extends StreamOutput {
	public OutputDecorator(Writer stream) { // Is this the right way to do it?
		super(stream);
	}
	
	StreamOutput so;
	public abstract void write(Object o);
}
