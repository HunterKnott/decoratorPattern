package dp.controller;

import java.io.IOException;

public interface Output {
    void write(Object o) throws IOException;
    void addDecorator(OutputDecorator od);
}

// Every decorator must implement this interface