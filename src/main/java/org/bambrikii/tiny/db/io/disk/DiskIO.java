package org.bambrikii.tiny.db.io.disk;

import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.function.Predicate;

public class DiskIO {
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
        var counter = new ArrayList<Path>();
        Files.walkFileTree(Path.of(key), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                counter.add(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                }
                Files.delete(dir);
                counter.add(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        return !counter.isEmpty();
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
