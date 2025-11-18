package org.bambrikii.tiny.db.algo.relio;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.disk.FileOps;
import org.bambrikii.tiny.db.model.TableStruct;

import static org.bambrikii.tiny.db.algo.relio.RelTableFileUtils.buildStructFilePath;
import static org.bambrikii.tiny.db.algo.relio.RelTableFileUtils.ensureStructDir;

@RequiredArgsConstructor
public class RelTableStructWriteIO implements AutoCloseable {
    private final DiskIO io;
    private final String fileName;
    private FileOps ops;

    /**
     * header - table - len, name
     * columns - count, [len, name, size]
     * values - [rowid, val of size]
     */

    public void open() {
        ensureStructDir(fileName);
        this.ops = io.openReadWrite(buildStructFilePath(fileName));
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
        if (ops == null) {
            return;
        }
        ops.close();
    }
}
