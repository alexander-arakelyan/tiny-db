package org.bambrikii.tiny.db.storage.instructions;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.algo.relio.RelTableMemIO;
import org.bambrikii.tiny.db.algo.relio.RelTableMemStructIO;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class MemInstructionStrategy implements AbstractInstructionStrategy {
    private final MemIO mem;

    @SneakyThrows
    @Override
    public TableStruct read(String name) {
        try (var rw = new RelTableMemStructIO(mem, name)) {
            rw.open();
            return rw.read();
        }
    }

    @SneakyThrows
    @Override
    public boolean write(TableStruct struct) {
        try (var rw = new RelTableMemStructIO(mem, struct.getTable())) {
            rw.open();
            return rw.write(struct);
        }
    }

    @SneakyThrows
    @Override
    public boolean drop(String name) {
        try (var rw = new RelTableMemStructIO(mem, name)) {
            rw.open();
            return rw.drop(name) && mem.drop(name);
        }
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
