package org.bambrikii.tiny.db.algo.rel.disk;

import lombok.SneakyThrows;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class FileRelTableUtils {
    public static final String STRUCT_EXT = "struct";
    public static final String PAGE_EXT = "page";
    public final static Pattern PAGE_FILE_NAME = Pattern.compile("^.*\\." + PAGE_EXT + "$");

    private FileRelTableUtils() {
    }

    @SneakyThrows
    public static void ensureStructDir(String name) {
        Files.createDirectories(Path.of(name));
    }

    public static String buildStructFilePath(String name) {
        return name
                + FileSystems.getDefault().getSeparator()
                + extractFileName(name)
                + "."
                + STRUCT_EXT;
    }

    private static String extractFileName(String name) {
        return name.substring(name.indexOf(FileSystems.getDefault().getSeparator()) + 1);
    }

    public static String buildPageFilePath(String name, int pageN) {
        return name + FileSystems.getDefault().getSeparator() + extractFileName(name) + "." + pageN + "." + PAGE_EXT;
    }
}
