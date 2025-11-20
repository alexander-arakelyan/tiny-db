package org.bambrikii.tiny.db.storage.disk;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.algo.rel.disk.FileRelTable;
import org.bambrikii.tiny.db.algo.rel.disk.FileRelTableStructReader;
import org.bambrikii.tiny.db.algo.rel.disk.FileRelTableStructWriter;
import org.bambrikii.tiny.db.algo.rel.disk.FileRelTableWriter;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.model.Scrollable;
import org.bambrikii.tiny.db.storage.AbstractInstructionStrategy;
import org.bambrikii.tiny.db.storage.mem.MemIO;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class DiskInstructionStrategy implements AbstractInstructionStrategy {
    private final DiskIO disk;
    private final MemIO mem;

    @Override
    public boolean write(TableStruct struct) {
        var table = struct.getTable();
        try (var rel = new FileRelTableStructWriter(disk, table)) {
            rel.open();
            return rel.write(struct);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to store structure " + table + " on disk.", ex);
        }
    }

    @SneakyThrows
    @Override
    public TableStruct read(String name) {
        try (var structIo = new FileRelTableStructReader(disk, name)) {
            structIo.open();
            return structIo.read();
        }
    }

    @Override
    public boolean drop(String name) {
        // TODO: delete struct files,
        // TODO: delete page files.
        return disk.drop(name) && mem.drop(name);
    }

    @Override
    public Scrollable scan(String name) {
        return new FileRelTable(disk, name);
    }

    @SneakyThrows
    @Override
    public void insert(String name, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        try (var rw = new FileRelTableWriter(disk, name)) {
            rw.open();
            rw.insert(valuesResolver.apply(row));
        }
    }

    @SneakyThrows
    @Override
    public void update(String name, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        try (var rw = new FileRelTableWriter(disk, name)) {
            rw.open();
            rw.update(row.getRowId(), valuesResolver.apply(row));
        }

    }

    @SneakyThrows
    @Override
    public void delete(String name, String rowId) {
        try (var rw = new FileRelTableWriter(disk, name)) {
            rw.open();
            rw.delete(rowId);
        }
    }
}
