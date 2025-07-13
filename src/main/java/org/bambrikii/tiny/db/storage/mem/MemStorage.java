package org.bambrikii.tiny.db.storage.mem;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.storage.AbstractStorage;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class MemStorage implements AbstractStorage {
    private final MemIO io;

    public MemStorage() {
        io = new MemIO();
    }

    @Override
    public void write(String key, Object obj) {
        io.write(key, obj);
    }

    @Override
    public <T> T read(String key) {
        return io.read(key);
    }

    @Override
    public void append(String key, Object obj) {
        io.append(key, obj);
    }

    @Override
    public void drop(String key) {
        io.drop(key);
    }

    @Override
    public void delete(String key, Predicate<Boolean> filter) {
        io.delete(key, filter);
    }
}
