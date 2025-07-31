package org.bambrikii.tiny.db.algo.relio;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.disk.FileOps;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.TableStruct;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;

import static org.bambrikii.tiny.db.algo.relio.RelTableFileUtils.buildStructFilePath;

@RequiredArgsConstructor
public class RelTableStructReadIO implements AutoCloseable {
    private final DiskIO io;
    private final String name;
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
     */

    public void open() {
        this.raf = io.openRead(buildStructFilePath(name));
        this.channel = raf.getChannel();
        this.ops = new FileOps(channel);
    }

    public TableStruct read() {
        var struct = new TableStruct();
        struct.setTable(ops.readStr());
        struct.setVersion(ops.readInt());
        var colsCount = ops.readInt();
        var cols = struct.getColumns();
        for (var i = 0; i < colsCount; i++) {
            var col = new Column();
            col.setName(ops.readStr());
            col.setType(ops.readStr());
            col.setSize(ops.readInt());
            col.setUnique(ops.readBool());
            col.setNullable(ops.readBool());
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
