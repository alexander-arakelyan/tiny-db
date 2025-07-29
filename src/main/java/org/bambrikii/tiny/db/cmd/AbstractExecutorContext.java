package org.bambrikii.tiny.db.cmd;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.storage.StorageContext;

@RequiredArgsConstructor
public abstract class AbstractExecutorContext {
    private final DiskIO file;
    private final MemIO mem;

    public StorageContext getStorage() {
        return new StorageContext(file, mem);
    }
}

