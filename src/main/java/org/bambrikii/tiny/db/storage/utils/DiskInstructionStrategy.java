package org.bambrikii.tiny.db.storage.utils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.mem.MemIO;
import org.bambrikii.tiny.db.storagelayout.relio.RelTableScanIO;
import org.bambrikii.tiny.db.storagelayout.relio.RelTableStructWriteIO;
import org.bambrikii.tiny.db.storagelayout.relio.RelTableWriteIO;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class DiskInstructionStrategy implements AbstractInstructionStrategy {
    private final DiskIO disk;
    private final MemIO mem;

    @Override
    public boolean write(TableStruct struct) {
        try (var rel = new RelTableStructWriteIO(disk, struct.getTable())) {
            return rel.write(struct) && mem.write("information_schema." + struct.getTable(), struct);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean drop(String name) {
        return disk.drop(name) && mem.drop(name);
    }

    @Override
    public Scrollable scan(String name) {
        return new RelTableScanIO(disk, name);
    }

    @SneakyThrows
    @Override
    public void insert(String name, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        try (var rw = new RelTableWriteIO(disk, name)) {
            rw.open();
            rw.insert(valuesResolver.apply(row));
        }
    }

    @SneakyThrows
    @Override
    public void update(String name, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        try (var rw = new RelTableWriteIO(disk, name)) {
            rw.open();
            rw.update(row.getRowId(), valuesResolver.apply(row));
        }

    }

    @SneakyThrows
    @Override
    public void delete(String name, String rowId) {
        try (var rw = new RelTableWriteIO(disk, name)) {
            rw.open();
            rw.delete(rowId);
        }
    }
}
