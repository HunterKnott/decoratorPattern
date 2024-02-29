package dp.controller;
import java.io.*;

class StreamOutput implements Output {
	private Writer sink;
	
	public StreamOutput(Writer stream) {
		sink = stream;
	}
	
	// Added later
    public Writer getSink() {
        return sink;
    }

	public void write(Object o) throws IOException {
		try {
			sink.write(o.toString());
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}