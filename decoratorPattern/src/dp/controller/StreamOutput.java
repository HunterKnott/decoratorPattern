package dp.controller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class StreamOutput implements Output {
	private Writer sink;
	private List<OutputDecorator> decorators;
	
	public StreamOutput(Writer stream) {
		sink = stream;
		decorators = new ArrayList<>();
	}
	
	public void addDecorator(OutputDecorator decorator) {
		decorators.add(decorator);
	}
 
	public void write(Object o) throws IOException {
		try {
			String output = o.toString();
			for (OutputDecorator decorator : decorators) {
				output = decorator.decorate(output);
			}
			sink.write(o.toString());
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}