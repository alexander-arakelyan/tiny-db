package org.bambrikii.tiny.db.algo.rel.disk;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.algo.PhysicalRow;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.algo.RelColumnType;
import org.bambrikii.tiny.db.algo.TableStructDecorator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static org.bambrikii.tiny.db.algo.rel.disk.FileRelTablePage.ROW_ID_COLUMN_NAME;

public class FileRelTablePageWriter extends FileRelTablePageReader {
    public static final int PAGE_SIZE_LIMIT = 1024;
    private boolean dirty;

    public FileRelTablePageWriter(DiskIO io, Path path, TableStructDecorator structDecorator) {
        super(io, path, structDecorator);
    }

    @SneakyThrows
    @Override
    public void open() {
        var exists = Files.exists(path);
        this.page = io.openReadWrite(this.path.toString());
        this.rowN = 0;
        this.dirty = false;
        if (exists) {
            readHeader();
            readRows();
        } else {
            rows = new ArrayList<>();
        }
    }

    public void writeHeader() {
        var columns = structDecorator.getColumns();
        page.writeStr(ROW_ID_COLUMN_NAME);
        DbLogger.log(this, "Column name %s written", ROW_ID_COLUMN_NAME);
        for (Column column : columns) {
            page.writeColSeparator();
            var name = column.getName();
            page.writeStr(name);
            DbLogger.log(this, "Column name %s written", name);
        }
        page.writeLineSeparator();
    }

    private void writeRows() {
        DbLogger.log(this, "Writing rows: %s", rows);
        rows.forEach(this::writeRow);
    }

    private void writeRow(Row row) {
        page.writeRowId(row.getRowId());
        page.writeDeleted(row.isDeleted());
        for (var col : structDecorator.getColumns()) {
            page.writeColSeparator();
            var name = col.getName();
            var val = row.read(getTable(), name);
            writeVal(col, val);
        }
        page.writeLineSeparator();
    }

    public String insertRow(String rowId, Map<String, Object> vals) {
        if (rows.size() >= PAGE_SIZE_LIMIT) {
            return null;
        }
        var row = new PhysicalRow();
        var rowid = page.writeRowId(rowId);
        row.setRowId(rowid);
        row.setDeleted(false);
        for (var col : structDecorator.getColumns()) {
            var name = col.getName();
            var val = vals.get(name);
            row.write(getTable(), name, val);
        }
        rows.add(row);
        this.dirty = true;
        return rowid;
    }

    public boolean updateRow(String rowId, Map<String, Object> vals) {
        for (var row : rows) {
            if (Objects.equals(row.getRowId(), rowId)) {
                for (var entry : vals.entrySet()) {
                    row.write(getTable(), entry.getKey(), entry.getValue());
                }
                dirty = true;
                return true;
            }
        }
        return false;
    }

    public boolean deleteRow(String rowId) {
        var iter = rows.iterator();
        while (iter.hasNext()) {
            var next = iter.next();
            if (Objects.equals(next.getRowId(), rowId)) {
                iter.remove();
                this.dirty = true;
                return true;
            }
        }
        return false;
    }

    private void writeVal(Column col, Object val) {
        var type = RelColumnType.findByAlias(col.getType());
        if (type == null || type == RelColumnType.OBJECT) {
            page.writeObj(val);
        }
        if (type == RelColumnType.INT) {
            page.writeInt((int) val);
        }
        if (type == RelColumnType.STRING) {
            page.writeStr((String) val);
        }
    }

    @SneakyThrows
    public boolean write() {
        if (!dirty) {
            return false;
        }
        page.seek(0);
        page.setLength(0);
        writeHeader();
        writeRows();
        return true;
    }

    public int getAvailableCapacity() {
        return PAGE_SIZE_LIMIT - rows.size();
    }

    @SneakyThrows
    @Override
    public void close() {
        page.close();
    }
}
