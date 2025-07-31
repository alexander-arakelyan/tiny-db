package org.bambrikii.tiny.db.algo.relio;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.disk.FileOps;
import org.bambrikii.tiny.db.model.TableStruct;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;

import static org.bambrikii.tiny.db.algo.relio.RelTableFileUtils.buildStructFilePath;
import static org.bambrikii.tiny.db.algo.relio.RelTableFileUtils.ensureStructDir;

@RequiredArgsConstructor
public class RelTableStructWriteIO implements AutoCloseable {
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
     */

    public void open() {
        ensureStructDir(fileName);
        this.raf = io.openReadWrite(buildStructFilePath(fileName));
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

    @Override
    public void close() throws Exception {
        if (raf == null) {
            return;
        }
        channel.close();
        raf.close();
    }
}
