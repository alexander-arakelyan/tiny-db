package org.bambrikii.tiny.db.algo.rel.disk;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.algo.PhysicalRow;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.algo.RelColumnType;
import org.bambrikii.tiny.db.algo.TableStructDecorator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.bambrikii.tiny.db.algo.rel.disk.FileRelTablePage.ROW_ID_COLUMN_NAME;

@RequiredArgsConstructor
public class FileRelTablePageReader implements AutoCloseable {
    protected final DiskIO io;
    protected final Path path;
    protected final TableStructDecorator structDecorator;
    protected FileRelTablePage page;
    protected List<Row> rows;
    protected int rowN;

    @SneakyThrows
    public void open() {
        DbLogger.log(this, "Scanning page %s", path);
        this.rowN = 0;
        var path = this.path.toString();
        this.page = io.openRead(path);
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
        var rowId = page.readStr(); // rowid column name
        if (!ROW_ID_COLUMN_NAME.equals(rowId)) {
            throw new IllegalStateException(String.format("Column name %s not found", path, ROW_ID_COLUMN_NAME));
        }
        DbLogger.log(this, "Column name %s read", path, ROW_ID_COLUMN_NAME);
        for (int i = 0; i < columns.size(); i++) {
            var sep = page.readColSeparator();
            if (!sep) {
                throw new IllegalStateException("Column separator not found");
            }
            var colName = page.readStr();
            DbLogger.log(this, "Column name %s read", colName);
        }
        page.readLineSeparator();
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
        var rowId = page.readRowId();
        if (rowId == null) {
            return null;
        }
        var row = new PhysicalRow();
        row.setRowId(rowId);
        row.setDeleted(page.readDeleted());
        for (var col : structDecorator.getColumns()) {
            page.readColSeparator();
            row.write(getTable(), col.getName(), readVal(col));
        }
        page.readLineSeparator();
        return row;
    }

    private String getTable() {
        return structDecorator.getStruct().getTable();
    }

    private Object readVal(Column col) {
        var type = RelColumnType.findByAlias(col.getType());
        if (type == null || type == RelColumnType.OBJECT) {
            return page.readObj();
        }
        if (type == RelColumnType.INT) {
            return page.readInt();
        }
        if (type == RelColumnType.STRING) {
            return page.readStr();
        }
        return null;
    }

    @SneakyThrows
    @Override
    public void close() {
        page.close();
    }
}
