package org.bambrikii.tiny.db;

import java.util.HashMap;
import java.util.Map;

public class State {
    private final Map<String, String> state = new HashMap<>();

    public void clear() {
        state.clear();
    }

    public void mod(String name, String value) {
        state.compute(name, (k, v) -> value);
    }

    public void mod(String name) {
        state.remove(name);
    }

    public String find(String name) {
        return state.get(name);
    }
}
