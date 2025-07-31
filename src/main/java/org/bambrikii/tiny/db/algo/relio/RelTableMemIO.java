package org.bambrikii.tiny.db.algo.relio;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.algo.PhysicalRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class RelTableMemIO implements Scrollable, AutoCloseable {
    private final MemIO io;
    private final String name;
    private List<Row> rows;
    private int rowN;

    public void open() {
        rows = new ArrayList<>();
        this.rowN = 0;
    }

    @Override
    public Row next() {
        return rows.size() < rowN
                ? rows.get(rowN++)
                : null;
    }

    @Override
    public void reset() {
        rowN = 0;
    }

    public void insert(Map<String, Object> vals) {
        var row = new PhysicalRow();
        row.setRowId(UUID.randomUUID().toString());
        writeValues(vals, row);
        rows.add(row);
    }

    public boolean update(String rowId, Map<String, Object> vals) {
        var it = rows.iterator();
        while (it.hasNext()) {
            var row = it.next();
            if (Objects.equals(rowId, row.getRowId())) {
                writeValues(vals, row);
                return true;
            }
        }
        return false;
    }

    private static void writeValues(Map<String, Object> vals, Row row) {
        for (var entry : vals.entrySet()) {
            row.write(entry.getKey(), entry.getValue());
        }
    }

    public boolean delete(String rowId) {
        var it = rows.iterator();
        while (it.hasNext()) {
            var row = it.next();
            if (Objects.equals(rowId, row.getRowId())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public void close() {
        rowN = 0;
        rows = null;
    }
}
