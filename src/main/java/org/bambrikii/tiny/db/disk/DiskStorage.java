package org.bambrikii.tiny.db.disk;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.storage.AbstractStorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Predicate;

public class DiskStorage implements AbstractStorage {
    private final FileIO io;

    public DiskStorage() {
        io = new FileIO();
    }

    @SneakyThrows
    private byte[] serialize(Object obj) {
        try (var baos = new ByteArrayOutputStream();
             var oos = new ObjectOutputStream(baos)
        ) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    @SneakyThrows
    private <T> T deserialize(byte[] bytes) {
        try (var bais = new ByteArrayInputStream(bytes);
             var ois = new ObjectInputStream(bais)
        ) {
            return (T) ois.readObject();
        }
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
