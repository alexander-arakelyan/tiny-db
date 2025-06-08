package org.bambrikii.tiny.db.storage;

import org.bambrikii.tiny.db.disk.DiskStorage;
import org.bambrikii.tiny.db.mem.MemStorage;

import java.util.Map;
import java.util.function.Predicate;

public class StorageFacade {
    private DiskStorage diskStorage;
    private MemStorage memStorage;

    public void write(String key, Object obj) {
        diskStorage.write(key, obj);
        memStorage.write(key, obj);
    }

    public void drop(String key) {
        diskStorage.drop(key);
        memStorage.drop(key);
    }

    public void append(String key, Map<String, Object> values) {
        diskStorage.append(key, values);
        memStorage.append(key, values);
    }

    public void delete(String key, Predicate<Boolean> filter) {
        diskStorage.delete(key, filter);
        memStorage.delete(key, filter);
    }
}
