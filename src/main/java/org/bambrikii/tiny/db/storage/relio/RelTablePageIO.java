package org.bambrikii.tiny.db.storage.relio;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.storage.PhysicalRow;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.disk.FileOps;
import org.bambrikii.tiny.db.utils.RelColumnTypes;
import org.bambrikii.tiny.db.utils.TableStructDecorator;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RelTablePageIO implements AutoCloseable {
    private final DiskIO io;
    private final String fileName;
    private final TableStructDecorator structDecorator;
    private RandomAccessFile raf;
    private FileChannel channel;
    private FileOps ops;
    private List<String> cols;
    private List<Row> rows;
    private int rowN;

    @SneakyThrows
    public void open() {
        this.raf = io.open(fileName);
        this.channel = raf.getChannel();
        this.ops = new FileOps(channel);
        this.cols = readCols();
        this.rowN = 0;
    }

    @SneakyThrows
    @Override
    public void close() {
        raf.close();
    }

    public Row next() {
        if (rows == null) {
            this.rows = readRows();
        }
        if (rowN >= rows.size()) {
            return null;
        }
        return rows.get(rowN++);
    }

    @SneakyThrows
    private List<String> readCols() {
        var count = ops.readInt();
        var cols = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            cols.add(ops.readStr());
        }
        return cols;
    }

    private List<Row> readRows() {
        var rows = new ArrayList<Row>();
        Row row;
        while ((row = readRow()) != null) {
            rows.add(row);
        }
        return rows;
    }

    private Row readRow() {
        var rowId = ops.readRowId();
        if (rowId == null) {
            return null;
        }
        var row = new PhysicalRow();
        row.setRowId(rowId);
        for (var colName : cols) {
            ops.readColSeparator();
            row.setVal(colName, extractVal(colName, row));
        }
        ops.readLineSeparator();
        return row;
    }

    private Object extractVal(String colName, PhysicalRow row) {
        var col = structDecorator.getColumnByName(colName);
        var type = RelColumnTypes.findByAlias(col.getType());
        if (type == null || type == RelColumnTypes.OBJECT) {
            return ops.readObj();
        }
        if (type == RelColumnTypes.INT) {
            return ops.readInt();
        }
        if (type == RelColumnTypes.STRING) {
            return ops.readStr();
        }
        return null;
    }

}
