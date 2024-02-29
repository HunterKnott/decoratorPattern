package dp.controller;

import java.io.IOException;

public interface Output {
    void write(Object o) throws IOException;
}

// Every decorator must implement this interface