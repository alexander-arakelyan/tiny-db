package org.bambrikii.tiny.db.algo.rel.mem;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.storage.mem.MemIO;

@RequiredArgsConstructor
public class MemRelTableStructWriter implements AutoCloseable {
    private final MemIO io;
    private final String name;

    public void open() {
    }

    public TableStruct read() {
        return io.read("information_schema.mem." + name);
    }

    public boolean write(TableStruct struct) {
        return io.write("information_schema.mem." + name, struct);
    }

    public boolean drop(String name) {
        return io.drop("information_schema.mem." + name);
    }

    @Override
    public void close() throws Exception {
    }
}
