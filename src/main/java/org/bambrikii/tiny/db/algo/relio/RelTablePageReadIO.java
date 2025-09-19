package org.bambrikii.tiny.db.algo.relio;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.disk.FileOps;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.algo.PhysicalRow;
import org.bambrikii.tiny.db.utils.RelColumnType;
import org.bambrikii.tiny.db.utils.TableStructDecorator;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.bambrikii.tiny.db.io.disk.FileOps.ROW_ID_COLUMN_NAME;

@RequiredArgsConstructor
public class RelTablePageReadIO implements AutoCloseable {
    protected final DiskIO io;
    protected final Path path;
    protected final TableStructDecorator structDecorator;
    protected RandomAccessFile raf;
    protected FileChannel channel;
    protected FileOps ops;
    protected List<Row> rows;
    protected int rowN;

    @SneakyThrows
    public void open() {
        DbLogger.log(this, "Scanning page %s", path);
        this.rowN = 0;
        var path = this.path.toString();
        this.raf = io.openRead(path);
        this.channel = raf.getChannel();
        this.ops = new FileOps(channel);
        readHeader();
        readRows();
    }

    public Row next() {
        if (rows == null) {
            readRows();
        }
        if (rowN >= rows.size()) {
            return null;
        }
        return rows.get(rowN++);
    }

    public void readHeader() {
        var columns = structDecorator.getColumns();
        var rowId = ops.readStr(); // rowid column name
        if (!ROW_ID_COLUMN_NAME.equals(rowId)) {
            throw new IllegalStateException(String.format("Column name %s not found", path, ROW_ID_COLUMN_NAME));
        }
        DbLogger.log(this, "Column name %s read", path, ROW_ID_COLUMN_NAME);
        for (int i = 0; i < columns.size(); i++) {
            var sep = ops.readColSeparator();
            if (!sep) {
                throw new IllegalStateException("Column separator not found");
            }
            var colName = ops.readStr();
            DbLogger.log(this, "Column name %s read", colName);
        }
        ops.readLineSeparator();
    }

    void readRows() {
        var rows = new ArrayList<Row>();
        Row row;
        while ((row = readRow()) != null) {
            rows.add(row);
        }
        this.rows = rows;
    }

    private Row readRow() {
        var rowId = ops.readRowId();
        if (rowId == null) {
            return null;
        }
        var row = new PhysicalRow();
        row.setRowId(rowId);
        row.setDeleted(ops.readDeleted());
        for (var col : structDecorator.getColumns()) {
            ops.readColSeparator();
            row.write(getTable(), col.getName(), readVal(col));
        }
        ops.readLineSeparator();
        return row;
    }

    private String getTable() {
        return structDecorator.getStruct().getTable();
    }

    private Object readVal(Column col) {
        var type = RelColumnType.findByAlias(col.getType());
        if (type == null || type == RelColumnType.OBJECT) {
            return ops.readObj();
        }
        if (type == RelColumnType.INT) {
            return ops.readInt();
        }
        if (type == RelColumnType.STRING) {
            return ops.readStr();
        }
        return null;
    }

    @SneakyThrows
    @Override
    public void close() {
        channel.close();
        raf.close();
    }
}
