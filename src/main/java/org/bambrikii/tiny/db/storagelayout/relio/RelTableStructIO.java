package org.bambrikii.tiny.db.storagelayout.relio;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.disk.FileOps;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;

@RequiredArgsConstructor
public class RelTableStructIO implements AutoCloseable {
    private final DiskIO io;
    private final String fileName;
    private RandomAccessFile raf;
    private FileChannel channel;
    private String tableName;
    private List<String> cols;
    private Object vals;
    private FileOps ops;

    /**
     * header - table - len, name
     * columns - count, [len, name, size]
     * values - [rowid, val of size]
     *
     * @param
     */

    public void open() {
        this.raf = io.openRead(fileName);
        this.channel = raf.getChannel();
        this.ops = new FileOps(channel);
    }

    public boolean write(TableStruct struct) {
        ops.writeStr(struct.getTable());
        ops.writeInt(struct.getVersion());
        ops.writeInt(struct.getColumns().size());
        for (var col : struct.getColumns()) {
            ops.writeStr(col.getName());
            ops.writeStr(col.getType());
            ops.writeInt(col.getSize());
            ops.writeBool(col.isUnique());
            ops.writeBool(col.isNullable());
        }
        return true;
    }

    public TableStruct read() {
        var struct = new TableStruct();
        struct.setTable(ops.readStr(channel));
        struct.setVersion(ops.readInt(channel));
        var colsCount = ops.readInt(channel);
        var cols = struct.getColumns();
        for (var i = 0; i < colsCount; i++) {
            var col = new Column();
            col.setName(ops.readStr(channel));
            col.setType(ops.readStr(channel));
            col.setSize(ops.readInt(channel));
            col.setUnique(ops.readBool(channel));
            col.setNullable(ops.readBool(channel));
            cols.add(col);
        }
        return struct;
    }

    @Override
    public void close() throws Exception {
        if (raf == null) {
            return;
        }
        channel.close();
        raf.close();
    }
}
