package org.bambrikii.tiny.db.storagelayout.relio;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.disk.FileOps;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.storagelayout.PhysicalRow;
import org.bambrikii.tiny.db.utils.RelColumnType;
import org.bambrikii.tiny.db.utils.TableStructDecorator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static org.bambrikii.tiny.db.io.disk.FileOps.ROW_ID_COLUMN_NAME;

public class RelTablePageWriteIO extends RelTablePageReadIO {
    public static final int PAGE_SIZE_LIMIT = 1024 * 1;
    private boolean dirty;

    public RelTablePageWriteIO(DiskIO io, Path path, TableStructDecorator structDecorator) {
        super(io, path, structDecorator);
    }

    @SneakyThrows
    public void open() {
        var exists = Files.exists(path);
        this.raf = io.openReadWrite(this.path.toString());
        this.channel = raf.getChannel();
        this.ops = new FileOps(channel);
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
        ops.writeStr(ROW_ID_COLUMN_NAME);
        DbLogger.log(this, "Column name %s written", ROW_ID_COLUMN_NAME);
        for (int i = 0; i < columns.size(); i++) {
            ops.writeColSeparator();
            var name = columns.get(i).getName();
            ops.writeStr(name);
            DbLogger.log(this, "Column name %s written", name);
        }
        ops.writeLineSeparator();
    }

    private void writeRows() {
        DbLogger.log(this, "Writing rows: %s", rows);
        rows.forEach(this::writeRow);
    }

    private void writeRow(Row row) {
        ops.writeRowId(row.getRowId());
        ops.writeDeleted(row.isDeleted());
        for (var col : structDecorator.getColumns()) {
            ops.writeColSeparator();
            var name = col.getName();
            var val = row.read(name);
            writeVal(col, val);
        }
        ops.writeLineSeparator();
    }

    public String insertRow(String rowId, Map<String, Object> vals) {
        if (rows.size() >= PAGE_SIZE_LIMIT) {
            return null;
        }
        var row = new PhysicalRow();
        var rowid = ops.writeRowId(rowId);
        row.setRowId(rowid);
        row.setDeleted(false);
        for (var col : structDecorator.getColumns()) {
            var name = col.getName();
            var val = vals.get(name);
            row.write(name, val);
        }
        rows.add(row);
        this.dirty = true;
        return rowid;
    }

    public boolean updateRow(String rowId, Map<String, Object> vals) {
        for (var row : rows) {
            if (Objects.equals(row.getRowId(), rowId)) {
                for (var entry : vals.entrySet()) {
                    row.write(entry.getKey(), entry.getValue());
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
            ops.writeObj(val);
        }
        if (type == RelColumnType.INT) {
            ops.writeInt((int) val);
        }
        if (type == RelColumnType.STRING) {
            ops.writeStr((String) val);
        }
    }

    @SneakyThrows
    public boolean write() {
        if (!dirty) {
            return false;
        }
        raf.seek(0);
        raf.setLength(0);
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
        channel.close();
        raf.close();
    }
}
