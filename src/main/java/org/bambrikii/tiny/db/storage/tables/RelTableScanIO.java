package org.bambrikii.tiny.db.storage.tables;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.disk.FileOps;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * table files format:
 */
@RequiredArgsConstructor
public class RelTableScanIO implements Scrollable {
    private final static Pattern PAGE_FILE_NAME = Pattern.compile("^data.*\\.txt$");
    private final DiskIO io;
    private final String name;
    private FileOps ops;
    private List<String> cols;
    private List<Path> pages;
    private int pageN;
    private RelTablePageIO page;

    @SneakyThrows
    @Override
    public void open() {
        this.pages = Files.list(Path.of(name))
                .filter(path -> PAGE_FILE_NAME.matcher(path.getFileName().toString()).matches())
                .collect(Collectors.toList());
        pageN = 0;
    }

    @Override
    public Row next() {
        if (page == null) {
            if (pageN >= pages.size()) {
                return null;
            }
            this.page = openPage();
        }
        Row row;
        while ((row = page.next()) == null && ++pageN < pages.size()) {
            tryClosePage();
            this.page = openPage();
        }
        return row;
    }

    private RelTablePageIO openPage() {
        return new RelTablePageIO(io, name, pages.get(pageN));
    }

    @SneakyThrows
    public void read() {
    }

    @Override
    public void reset() {
        pageN = 0;
        tryClosePage();
    }

    @SneakyThrows
    @Override
    public void close() {
        tryClosePage();
    }

    private void tryClosePage() {
        if (page != null) {
            page.close();
        }
        page = null;
    }
}
