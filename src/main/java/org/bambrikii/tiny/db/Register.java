package org.bambrikii.tiny.db;

import java.nio.file.Paths;

public class Register {
    private final State state;
    private final Log log;

    public Register(String name) {
        String logFileName = name + ".log";
        this.log = new Log(logFileName);
        this.state = new State();
        if (Paths.get(logFileName).toFile().exists()) {
            this.log.load(this.state);
        }
    }

    public String find(String name) {
        return this.state.find(name);
    }

    public void mod(String name, String val) {
        this.log.store(name, val);
        this.state.mod(name, val);
    }

    public void mod(String name) {
        this.log.store(name);
        this.state.mod(name);
    }

    public void destroy() {
        this.log.destroy();
        this.state.clear();
    }
}
