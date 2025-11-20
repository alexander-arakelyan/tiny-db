package org.bambrikii.tiny.db.algo.rel.disk;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.storage.disk.DiskIO;

import static org.bambrikii.tiny.db.algo.rel.disk.FileRelTableUtils.buildStructFilePath;
import static org.bambrikii.tiny.db.algo.rel.disk.FileRelTableUtils.ensureStructDir;

@RequiredArgsConstructor
public class FileRelTableStructWriter implements AutoCloseable {
    private final DiskIO io;
    private final String fileName;
    private FileRelTablePage page;

    /**
     * header - table - len, name
     * columns - count, [len, name, size]
     * values - [rowid, val of size]
     */

    public void open() {
        ensureStructDir(fileName);
        this.page = io.openReadWrite(buildStructFilePath(fileName));
    }

    public boolean write(TableStruct struct) {
        page.writeStr(struct.getTable());
        page.writeInt(struct.getVersion());
        page.writeInt(struct.getColumns().size());
        for (var col : struct.getColumns()) {
            page.writeStr(col.getName());
            page.writeStr(col.getType());
            page.writeInt(col.getSize());
            page.writeBool(col.isUnique());
            page.writeBool(col.isNullable());
        }
        return true;
    }

    @Override
    public void close() throws Exception {
        if (page == null) {
            return;
        }
        page.close();
    }
}
