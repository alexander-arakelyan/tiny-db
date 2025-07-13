package org.bambrikii.tiny.db.storage;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.disk.DiskStorage;
import org.bambrikii.tiny.db.storage.mem.MemStorage;
import org.bambrikii.tiny.db.storage.tables.RelTableIO;

import java.util.Map;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class StorageContext {
    private final DiskStorage diskStorage;
    private final MemStorage memStorage;

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

    public void read(String key) {
        diskStorage.read(key);
        memStorage.read(key);
    }

    public Scrollable open(String table) {
        return diskStorage.read(table);
    }

    public TableStruct readStruct(Object tableFileRef) {
        return null;
    }
}
