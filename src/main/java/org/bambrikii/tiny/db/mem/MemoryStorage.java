package org.bambrikii.tiny.db.mem;

import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.Table;
import org.bambrikii.tiny.db.storage.AbstractStorage;

public class MemoryStorage implements AbstractStorage {
    private final MemoryIO memoryIO;

    public MemoryStorage() {
        memoryIO = new MemoryIO();
    }

    @Override
    public void select(Table table, String filter) {
        memoryIO.filter(table, filter);
    }

    @Override
    public void insert(Table table, Row row) {
        memoryIO.insert(table, row);
    }

    @Override
    public void update(Table table, Row row, String filter) {
        memoryIO.update(table, row, filter);
    }

    @Override
    public void delete(Table table, String filter) {
        memoryIO.delete(table, filter);
    }
}
