package org.bambrikii.tiny.db.disk;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Filter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class FileIO {
    public void append(String tableStruct, byte[] data) {

    }

    private static String buildPartName(Filter filter1) {
        return filter1 == null
                ? "default"
                : filter1.getLeft() + filter1.getOp().name() + filter1.getRight();
    }

    public void write(String key, byte[] obj) {

    }

    public byte[] read(String key) {
        return null;
    }

    @SneakyThrows
    public void drop(String key) {
        Files.deleteIfExists(Path.of(key));
    }

    public void delete(String key, Predicate<Boolean> filter) {

    }
}
