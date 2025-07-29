package org.bambrikii.tiny.db.io.disk;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class DiskIO {

    @SneakyThrows
    public void append(String key, byte[] data) {
        try (var os = new FileOutputStream(key, true)) {
            os.write(data);
        }
    }

    private static String buildPartName(Filter filter1) {
        return filter1 == null
                ? "default"
                : filter1.getL() + filter1.getOp().name() + filter1.getR();
    }

    @SneakyThrows
    public void write(String key, byte[] obj) {
        try (var os = new FileOutputStream(key)) {
            os.write(obj);
        }
    }

    @SneakyThrows
    public byte[] read(String key) {
        var bytes = new byte[1024];
        try (var is = new FileInputStream(key)) {
            is.read(bytes);
        }
        return bytes;
    }

    @SneakyThrows
    public boolean drop(String key) {
        return Files.deleteIfExists(Path.of(key));
    }

    public void delete(String key, Predicate<Boolean> filter) {

    }

    @SneakyThrows
    public RandomAccessFile openRead(String name) {
        return new RandomAccessFile(name, "r");
    }

    @SneakyThrows
    public RandomAccessFile openReadWrite(String name) {
        return new RandomAccessFile(name, "rw");
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
}
