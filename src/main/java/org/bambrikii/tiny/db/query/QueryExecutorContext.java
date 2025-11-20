package org.bambrikii.tiny.db.query;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.mem.MemIO;

@Getter
public class QueryExecutorContext extends AbstractExecutorContext {
    private boolean shouldRun = true;

    public QueryExecutorContext(DiskIO disk, MemIO mem) {
        super(disk, mem);
    }

    public boolean shouldRun() {
        return shouldRun;
    }

    public void shutdown() {
        this.shouldRun = false;
    }
}
