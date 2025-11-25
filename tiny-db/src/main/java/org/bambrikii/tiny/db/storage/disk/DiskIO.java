package org.bambrikii.tiny.db.storage.disk;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.algo.rel.disk.FileRelTablePage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DiskIO {
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
    public FileRelTablePage openRead(String name) {
        var raf = new RandomAccessFile(name, "r");
        var channel = raf.getChannel();
        return new FileRelTablePage(raf, channel);
    }

    @SneakyThrows
    public FileRelTablePage openReadWrite(String name) {
        var raf = new RandomAccessFile(name, "rw");
        var channel = raf.getChannel();
        return new FileRelTablePage(raf, channel);
    }

    @SneakyThrows
    public List<Path> find(Path dir, Predicate<Path> filter) {
        return Files
                .list(dir)
                .filter(filter)
                .collect(Collectors.toList());
    }
}
