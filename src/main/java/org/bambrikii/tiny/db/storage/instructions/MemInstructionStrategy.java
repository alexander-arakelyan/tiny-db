package org.bambrikii.tiny.db.storage.instructions;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storagelayout.relio.RelTableMemIO;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class MemInstructionStrategy implements AbstractInstructionStrategy {
    private final MemIO mem;

    @Override
    public boolean write(TableStruct struct) {
        return mem.write(struct.getTable(), struct);
    }

    @Override
    public boolean drop(String name) {
        return mem.drop(name);
    }

    @Override
    public Scrollable scan(String name) {
        return mem.read(name);
    }

    @Override
    public void insert(String name, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        try (var rw = new RelTableMemIO(mem, name)) {
            rw.open();
            rw.insert(valuesResolver.apply(row));
        }
    }

    @Override
    public void update(String name, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        try (var rw = new RelTableMemIO(mem, name)) {
            rw.open();
            rw.update(row.getRowId(), valuesResolver.apply(row));
        }
    }

    @Override
    public void delete(String name, String rowId) {
        try (var rw = new RelTableMemIO(mem, name)) {
            rw.open();
            rw.delete(rowId);
        }
    }
}
