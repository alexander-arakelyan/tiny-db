package org.bambrikii.tiny.db.plan.iterators;

import org.bambrikii.tiny.db.model.Row;

import java.util.HashMap;
import java.util.Map;

public class LogicalRow extends Row {
    private final Map<String, Row> rows = new HashMap<>();

    @Override
    public Object read(String path) {
        int pos = path.indexOf(".");
        if (pos < 0) {
            throw new IllegalArgumentException(String.format("Path %s should have \".\" as delimiter", path));
        }
        return read(path.substring(0, pos), path.substring(pos + 1));
    }

    public Object read(String alias, String name) {
        return rows.get(alias).read(name);
    }

    public void combine(String alias, Row row) {
        rows.putIfAbsent(alias, row);
    }
}
