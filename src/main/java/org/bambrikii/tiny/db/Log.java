package org.bambrikii.tiny.db;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Log {
    private final Path log;

    public Log(String name) {
        this.log = Paths.get(name);
    }

    public void destroy() {
        try {
            Files.delete(log);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void store(String name) {
        this.store(name, null);
    }

    public void store(String name, String val) {
        try (
                FileWriter writer = new FileWriter(log.toFile(), true);
                BufferedWriter bw = new BufferedWriter(writer);
        ) {
            bw.write(name);
            bw.write("=");
            bw.write(val == null ? "" : val);
            bw.write(System.lineSeparator());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void load(State state) {
        try (
                FileReader reader = new FileReader(log.toFile());
                BufferedReader bufferedReader = new BufferedReader(reader);
        ) {
            state.clear();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if ("".equals(line)) {
                    continue;
                }
                String[] nameVal = line.split("=");
                String name = nameVal[0];
                if (nameVal.length == 1) {
                    state.mod(name);
                    continue;
                }
                String value = nameVal[1];
                if ("".equals(value)) {
                    state.mod(name);
                } else {
                    state.mod(name, value);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
