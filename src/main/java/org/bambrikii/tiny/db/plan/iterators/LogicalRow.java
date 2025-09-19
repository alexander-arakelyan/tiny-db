package org.bambrikii.tiny.db.plan.iterators;

import lombok.ToString;
import org.bambrikii.tiny.db.model.Row;

import java.util.HashMap;
import java.util.Map;

@ToString
public class LogicalRow extends Row {
    private final Map<String, Row> rows = new HashMap<>();

    @Override
    public Object read(String tab, String col) {
        return rows.get(tab).read(tab, col);
    }

    @Override
    public void write(String tab, String col, Object val) {
        rows.get(tab).write(tab, col, val);
    }

    public void combine(String alias, Row row) {
        rows.putIfAbsent(alias, row);
    }

    public void combine(String leftAlias, Row leftRow, String rightAlias, Row rightRow) {
        combine(leftAlias, leftRow);
        combine(rightAlias, rightRow);
    }
}
