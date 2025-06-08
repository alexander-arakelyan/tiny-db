package org.bambrikii.tiny.db.disk;

import org.bambrikii.tiny.db.storage.AbstractStorage;

import java.util.function.Predicate;

public class DiskStorage implements AbstractStorage {
    private final FileIO io;

    public DiskStorage() {
        io = new FileIO();
    }

    private byte[] serialize(Object obj) {
        return null;
    }

    private <T> T deserialize(Object b) {
        return null;
    }

    @Override
    public void write(String key, Object obj) {
        byte[] b = serialize(obj);
        io.write(key, b);
    }

    @Override
    public <T> T read(String key) {
        var b = io.read(key);
        return deserialize(b);
    }

    @Override
    public void append(String key, Object obj) {
        byte[] b = serialize(obj);
        io.append(key, b);
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
