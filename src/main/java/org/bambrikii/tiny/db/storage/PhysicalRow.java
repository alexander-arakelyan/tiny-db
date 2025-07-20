package org.bambrikii.tiny.db.storage;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PhysicalRow extends Row {
    private final Map<String, Object> vals = new HashMap<>();

    public Object read(String columnName) {
        return vals.get(columnName);
    }

    public void setVal(String colName, Object obj) {
        this.vals.put(colName, obj);
    }
}
