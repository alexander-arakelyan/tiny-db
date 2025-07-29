package org.bambrikii.tiny.db.io.mem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class MemIO {
    private final Map<String, Object> store = new HashMap<>();

    public <T> T read(String key) {
        return (T) store.get(key);
    }

    public boolean write(String key, Object obj) {
        store.put(key, obj);
        return true;
    }

    public boolean drop(String key) {
        return store.remove(key) != null;
    }

    public void delete(String key, Predicate<Boolean> filter) {
        store.get(key);
        // TODO: apply filter and delete
    }
}
