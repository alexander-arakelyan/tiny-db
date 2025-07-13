package org.bambrikii.tiny.db.storage;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.mem.MemIO;
import org.bambrikii.tiny.db.storage.tables.RelTableScanIO;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class StorageContext {
    private final DiskIO disk;
    private final MemIO mem;

    public boolean write(String name, Function<DiskIO, Boolean> d, Function<MemIO, Boolean> m) {
        if (!d.apply(disk)) {
            return false;
        }
        return m.apply(mem);
    }

    public void drop(String key) {
        disk.drop(key);
        mem.drop(key);
    }

    public void append(String key, Map<String, Object> values) {
        disk.append(key, values);
        mem.append(key, values);
    }

    public <T> T read(String key, Function<DiskIO, T> d, Function<MemIO, T> m) {
        var res = m.apply(mem);
        if (res != null) {
            return res;
        }
        return d.apply(disk);
    }

    public void delete(String key, Predicate<Boolean> filter) {
        disk.delete(key, filter);
        mem.delete(key, filter);
    }

    public Scrollable open(String table) {
        Scrollable read = mem.read(table);
        if (read != null) {
            return read;
        }

        return new RelTableScanIO(disk, table);
    }
}

