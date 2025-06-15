package org.bambrikii.tiny.db.query;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.disk.DiskStorage;
import org.bambrikii.tiny.db.mem.MemStorage;

@Getter
public class QueryExecutorContext extends AbstractExecutorContext {
    private boolean shouldRun = true;

    public QueryExecutorContext(DiskStorage diskStorage, MemStorage memStorage) {
        super(diskStorage, memStorage);
    }

    public boolean shouldRun() {
        return shouldRun;
    }

    public void shutdown() {
        this.shouldRun = false;
    }
}
