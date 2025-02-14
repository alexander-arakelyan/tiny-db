package org.bambrikii.tiny.db.storage;

import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.Table;

public interface AbstractStorage {
    void select(Table table, String rowId);

    void insert(Table table, Row row);

    void update(Table table, Row row, String filter);

    void delete(Table table, String filter);
}
