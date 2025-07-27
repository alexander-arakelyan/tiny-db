package org.bambrikii.tiny.db.plan.iterators;

import lombok.ToString;
import org.bambrikii.tiny.db.model.Row;

import java.util.HashMap;
import java.util.Map;

@ToString
public class LogicalRow extends Row {
    private final Map<String, Row> rows = new HashMap<>();

    private static int getPos(String path) {
        int pos = path.indexOf(".");
        if (pos < 0) {
            throw new IllegalArgumentException(String.format("Path %s should have \".\" as delimiter", path));
        }
        return pos;
    }

    @Override
    public Object read(String path) {
        int pos = getPos(path);
        return read(path.substring(0, pos), path.substring(pos + 1));
    }

    @Override
    public void write(String path, Object val) {
        int pos = getPos(path);
        rows.get(path.substring(0, pos)).write(path.substring(pos + 1), val);
    }

    public Object read(String alias, String name) {
        return rows.get(alias).read(name);
    }

    public void combine(String alias, Row row) {
        rows.putIfAbsent(alias, row);
    }
}
