package org.bambrikii.tiny.db.mem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class MemIO {
    private final Map<String, Object> objects = new HashMap<>();

    public <T> T read(String key) {
        return (T) key;
    }

    public void append(String key, Object obj) {

    }

    public void write(String key, Object obj) {
        objects.put(key, obj);
    }

    public void drop(String key) {
        objects.remove(key);
    }

    public void delete(String key, Predicate<Boolean> filter) {
        objects.get(key);
        // TODO: apply filter and delete
    }
}
