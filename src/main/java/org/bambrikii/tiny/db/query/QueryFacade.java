package org.bambrikii.tiny.db.query;

import org.bambrikii.tiny.db.disk.DiskStorage;
import org.bambrikii.tiny.db.mem.MemoryStorage;
import org.bambrikii.tiny.db.storage.AbstractStorage;

public class QueryFacade {
    private final AbstractStorage memoryStorage;
    private final AbstractStorage diskStorage;

    public QueryFacade() {
        memoryStorage = new MemoryStorage();
        diskStorage = new DiskStorage();
    }

    public void insert() {
    }

    public void select() {
    }

    public void update() {
    }

    public void delete() {
    }
}
