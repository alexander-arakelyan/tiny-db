package org.bambrikii.tiny.db.algo.rel.disk;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.algo.TableStructDecorator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bambrikii.tiny.db.algo.rel.disk.FileRelTableUtils.PAGE_FILE_NAME;
import static org.bambrikii.tiny.db.algo.rel.disk.FileRelTableUtils.buildPageFilePath;

@RequiredArgsConstructor
public class FileRelTableWriter implements AutoCloseable {
    private final DiskIO io;
    private final String name;
    private FileRelTableStructReader structIo;
    private TableStruct struct;
    private List<FileRelTableWritePair> pages;
    private TableStructDecorator structDecorator;

    @SneakyThrows
    public void open() {
        structIo = new FileRelTableStructReader(io, name);
        structIo.open();
        struct = structIo.read();
        structDecorator = new TableStructDecorator(struct);
        pages = Files
                .list(Path.of(name))
                .filter(path -> PAGE_FILE_NAME.matcher(path.getFileName().toString()).matches())
                .map(FileRelTableWritePair::new)
                .sorted(Comparator.comparing(FileRelTableWritePair::getPath))
                .collect(Collectors.toList());
    }

    public String insert(Map<String, Object> vals) {
        if (pages.isEmpty()) {
            addPageFile();
        }
        String rowId = null;
        for (FileRelTableWritePair page0 : pages) {
            var page = ensurePage(page0);
            if (page.getAvailableCapacity() <= 0) {
                continue;
            }
            rowId = page.insertRow(null, vals);
            if (rowId != null) {
                break;
            }
        }
        if (rowId == null) {
            var page = ensurePage(addPageFile());
            rowId = page.insertRow(null, vals);
        }
        return rowId;
    }

    private int nextPageN() {
        return pages.size();
    }

    private FileRelTablePageWriter ensurePage(FileRelTableWritePair pair) {
        var pageIo = pair.getPageIo();
        if (pageIo != null) {
            return pageIo;
        }
        pageIo = new FileRelTablePageWriter(io, pair.getPath(), structDecorator);
        pageIo.open();
        pair.setPageIo(pageIo);
        return pageIo;
    }

    private FileRelTableWritePair addPageFile() {
        var path = Path.of(buildPageFilePath(name, nextPageN()));
        var pair = new FileRelTableWritePair(path);
        pages.add(pair);
        return pair;
    }

    public String update(String rowId, Map<String, Object> vals) {
        // find page with rowId
        for (var pair : pages) {
            var page = ensurePage(pair);
            if (page.getAvailableCapacity() <= 0) {
                continue;
            }
            if (!page.updateRow(rowId, vals)) {
                continue;
            }
            return rowId;
        }
        return null;
    }

    public String delete(String rowId) {
        // find page with rowId
        for (var pair : pages) {
            var page = ensurePage(pair);
            if (page.getAvailableCapacity() <= 0) {
                continue;
            }
            if (!page.deleteRow(rowId)) {
                continue;
            }
            return rowId;
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        for (var pair : pages) {
            var page = pair.getPageIo();
            if (page != null) {
                var written = page.write();
                if (written) {
                    DbLogger.log(this, "Page %s has been written", pair.getPath());
                }
            }
        }
        structIo.close();
    }
}
