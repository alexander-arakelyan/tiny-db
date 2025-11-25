package org.bambrikii.tiny.db.algo.rel.disk;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.storage.disk.DiskIO;

import static org.bambrikii.tiny.db.algo.rel.disk.FileRelTableUtils.buildStructFilePath;

@RequiredArgsConstructor
public class FileRelTableStructReader implements AutoCloseable {
    private final DiskIO io;
    private final String name;
    private FileRelTablePage page;

    /**
     * header - table - len, name
     * columns - count, [len, name, size]
     * values - [rowid, val of size]
     */

    public void open() {
        this.page = io.openRead(buildStructFilePath(name));
    }

    public TableStruct read() {
        var struct = new TableStruct();
        struct.setTable(page.readStr());
        struct.setVersion(page.readInt());
        var colsCount = page.readInt();
        var cols = struct.getColumns();
        for (var i = 0; i < colsCount; i++) {
            var col = new Column();
            col.setName(page.readStr());
            col.setType(page.readStr());
            col.setSize(page.readInt());
            col.setUnique(page.readBool());
            col.setNullable(page.readBool());
            cols.add(col);
        }
        return struct;
    }

    @Override
    public void close() throws Exception {
        if (page == null) {
            return;
        }
        page.close();
    }
}
