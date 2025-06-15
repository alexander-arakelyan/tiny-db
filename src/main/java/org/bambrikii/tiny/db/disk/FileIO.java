package org.bambrikii.tiny.db.disk;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Filter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class FileIO {
    @SneakyThrows
    public void append(String key, byte[] data) {
        try (var os = new FileOutputStream(key, true)) {
            os.write(data);
        }
    }

    private static String buildPartName(Filter filter1) {
        return filter1 == null
                ? "default"
                : filter1.getLeft() + filter1.getOp().name() + filter1.getRight();
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
    public void drop(String key) {
        Files.deleteIfExists(Path.of(key));
    }

    public void delete(String key, Predicate<Boolean> filter) {

    }
}
