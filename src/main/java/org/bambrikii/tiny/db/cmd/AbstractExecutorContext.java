package org.bambrikii.tiny.db.cmd;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.disk.DiskStorage;
import org.bambrikii.tiny.db.mem.MemStorage;
import org.bambrikii.tiny.db.storage.StorageContext;

@RequiredArgsConstructor
public abstract class AbstractExecutorContext {
    private final DiskStorage diskStorage;
    private final MemStorage memStorage;

    public StorageContext getStorage() {
        return new StorageContext(diskStorage, memStorage);
    }
}

