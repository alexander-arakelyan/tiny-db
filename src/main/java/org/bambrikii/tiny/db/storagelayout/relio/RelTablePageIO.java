package org.bambrikii.tiny.db.storagelayout.relio;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.storagelayout.PhysicalRow;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.disk.FileOps;
import org.bambrikii.tiny.db.utils.RelColumnType;
import org.bambrikii.tiny.db.utils.TableStructDecorator;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.bambrikii.tiny.db.storage.disk.FileOps.ROW_ID_COLUMN_NAME;

@RequiredArgsConstructor
public class RelTablePageIO implements AutoCloseable {
    public static final int PAGE_SIZE = 1024 * 1;
    private final DiskIO io;
    private final Path path;
    private final TableStructDecorator structDecorator;
    private RandomAccessFile raf;
    private FileChannel channel;
    private FileOps ops;
    private List<Row> rows;
    private int rowN;
    private boolean dirty;

    @SneakyThrows
    public void open(boolean rw) {
        var path2 = path.toString();
        this.raf = rw ? io.openReadWrite(path2) : io.openRead(path2);
        this.channel = raf.getChannel();
        this.ops = new FileOps(channel);
        this.rowN = 0;
        this.dirty = false;
        readHeader();
        readRows();
    }

    @SneakyThrows
    public void open() {
        open(false);
    }

    @SneakyThrows
    public void openReadWrite() {
        open(true);
    }

    @SneakyThrows
    @Override
    public void close() {
        channel.close();
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

    public void readHeader() {
        var columns = structDecorator.getColumns();
        ops.readStr(); // rowid column name
        for (int i = 0; i < columns.size(); i++) {
            ops.readColSeparator();
            ops.readStr();
        }
        ops.readLineSeparator();
    }

    public void writeHeader() {
        ops.writeInt(rows.size());
        var columns = structDecorator.getColumns();
        ops.writeStr(ROW_ID_COLUMN_NAME);
        for (int i = 0; i < columns.size(); i++) {
            ops.writeColSeparator();
            ops.writeStr(columns.get(i).getName());
        }
        ops.writeLineSeparator();
    }

    private List<Row> readRows() {
        var rows = new ArrayList<Row>();
        Row row;
        while ((row = readRow()) != null) {
            rows.add(row);
        }
        return rows;
    }

    private void writeRows() {
        for (var row : rows) {
            writeRow(row);
        }
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
            row.write(col.getName(), readVal(col));
        }
        ops.readLineSeparator();
        return row;
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
        if (rows.size() >= PAGE_SIZE) {
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
        return PAGE_SIZE - rows.size();
    }
}
