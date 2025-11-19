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
        var row = rows.get(tab);
        return row == null ? null : row.read(tab, col);
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
        setRowId(extractRpwOd(leftRow) + "-" + extractRpwOd(rightRow));
        setDeleted(extractDeleted(leftRow) && extractDeleted(rightRow));
    }

    private static boolean extractDeleted(Row row) {
        return row != null && row.isDeleted();
    }

    private static String extractRpwOd(Row row) {
        return row == null ? "-" : row.getRowId();
    }
}
