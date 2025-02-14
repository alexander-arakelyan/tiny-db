package org.bambrikii.tiny.db.disk;

import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.Table;
import org.bambrikii.tiny.db.storage.AbstractStorage;

public class DiskStorage implements AbstractStorage {
    private final FileIO fileIO;

    public DiskStorage() {
        fileIO = new FileIO();
    }

    @Override
    public void insert(Table table, Row row) {
        fileIO.append(table, row.getValues());
    }

    @Override
    public void update(Table table, Row row, String filter) {
        fileIO.update(table, row, filter);
    }

    @Override
    public void delete(Table table, String filter) {
        fileIO.markDeleted(table, filter);
    }

    @Override
    public void select(Table table, String filter) {
        fileIO.filter(table, filter);
    }
}
