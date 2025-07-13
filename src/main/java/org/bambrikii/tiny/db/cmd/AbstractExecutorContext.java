package org.bambrikii.tiny.db.cmd;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.storage.StorageContext;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.mem.MemIO;

@RequiredArgsConstructor
public abstract class AbstractExecutorContext {
    private final DiskIO file;
    private final MemIO mem;

    public StorageContext getStorage() {
        return new StorageContext(file, mem);
    }
}

