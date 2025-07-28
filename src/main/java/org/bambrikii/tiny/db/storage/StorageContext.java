package org.bambrikii.tiny.db.storage;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.mem.MemIO;
import org.bambrikii.tiny.db.storagelayout.relio.RelTableScanIO;
import org.bambrikii.tiny.db.storagelayout.relio.RelTableWriteIO;

import java.io.ObjectInputFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    public Scrollable scan(String table) {
        Scrollable read = mem.read(table);
        if (read != null) {
            return read;
        }

        return new RelTableScanIO(disk, table);
    }

    @SneakyThrows
    public void insert(String targetTable, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        try (var rw = new RelTableWriteIO(disk, targetTable)) {
            rw.open();
            rw.insert(valuesResolver.apply(row));
        }
    }

    @SneakyThrows
    public void update(String targetTable, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        try (var rw = new RelTableWriteIO(disk, targetTable)) {
            rw.open();
            rw.update(row.getRowId(), valuesResolver.apply(row));
        }
    }

    public <T> T read(String key, Function<DiskIO, T> d, Function<MemIO, T> m) {
        var res = m.apply(mem);
        if (res != null) {
            return res;
        }
        return d.apply(disk);
    }

    @SneakyThrows
    public void delete(String targetTable, String rowId) {
        try (var rw = new RelTableWriteIO(disk, targetTable)) {
            rw.open();
            rw.delete(rowId);
        }
    }
}

