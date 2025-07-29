package org.bambrikii.tiny.db.storage;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.mem.MemIO;
import org.bambrikii.tiny.db.storage.utils.AbstractInstructionStrategy;
import org.bambrikii.tiny.db.storage.utils.DiskInstructionStrategy;
import org.bambrikii.tiny.db.storage.utils.MemInstructionStrategy;

import java.util.Map;
import java.util.function.Function;

public class StorageContext {
    private final DiskInstructionStrategy diskInstructions;
    private final MemInstructionStrategy memInstructions;

    public StorageContext(DiskIO disk, MemIO mem) {
        diskInstructions = new DiskInstructionStrategy(disk, mem);
        memInstructions = new MemInstructionStrategy(mem);
    }

    private AbstractInstructionStrategy chooseStrategy(String name) {
        return name.startsWith("mem.") ? memInstructions : diskInstructions;
    }

    public boolean write(String name, TableStruct struct) {
        return chooseStrategy(name).write(struct);
    }

    public boolean drop(String name) {
        return chooseStrategy(name).drop(name);
    }

    public Scrollable scan(String name) {
        return chooseStrategy(name).scan(name);
    }

    @SneakyThrows
    public void insert(String name, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        chooseStrategy(name).insert(name, row, valuesResolver);
    }

    @SneakyThrows
    public void update(String name, Row row, Function<Row, Map<String, Object>> valuesResolver) {
        chooseStrategy(name).update(name, row, valuesResolver);
    }

    @SneakyThrows
    public void delete(String name, String rowId) {
        chooseStrategy(name).delete(name, rowId);
    }
}

