package org.bambrikii.tiny.db.storagelayout.relio;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.utils.TableStructDecorator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bambrikii.tiny.db.storagelayout.relio.RelTableFileUtils.PAGE_FILE_NAME;
import static org.bambrikii.tiny.db.storagelayout.relio.RelTableFileUtils.buildPageFilePath;

@RequiredArgsConstructor
public class RelTableWriteIO implements AutoCloseable {
    private final DiskIO io;
    private final String name;
    private RelTableStructReadIO structIo;
    private TableStruct struct;
    private List<Path> pagePaths;
    private int pageN;

    @SneakyThrows
    public void open() {
        structIo = new RelTableStructReadIO(io, name);
        structIo.open();
        struct = structIo.read();
        pagePaths = Files
                .list(Path.of(name))
                .map(Path::getFileName)
                .filter(fileName -> PAGE_FILE_NAME.matcher(fileName.toString()).matches())
                .collect(Collectors.toList());
        pageN = 0;
    }

    public String insert(Map<String, Object> vals) {
        if (pagePaths.isEmpty()) {
            appendPage();
        }
        for (var path : pagePaths) {
            try (var page = new RelTablePageReadWriteIO(io, path, new TableStructDecorator(struct))) {
                page.openReadWrite();
                if (page.getAvailableCapacity() <= 0) {
                    continue;
                }
                var rowId = page.insertRow(null, vals);
                if (!page.write()) {
                    continue;
                }
                return rowId;
            }
        }
        return null;
    }

    private void appendPage() {
        var path = Path.of(buildPageFilePath(name, pageN));
        try (var pageIo = new RelTablePageReadWriteIO(io, path, new TableStructDecorator(struct))) {
            pageIo.openReadWrite();
            pagePaths.add(path);
        }
    }

    public String update(String rowId, Map<String, Object> vals) {
        // find page with rowId
        for (var path : pagePaths) {
            try (var page = new RelTablePageReadWriteIO(io, path, new TableStructDecorator(struct))) {
                page.openReadWrite();
                if (page.getAvailableCapacity() <= 0) {
                    continue;
                }
                page.updateRow(rowId, vals);
                if (!page.write()) {
                    continue;
                }
                return rowId;
            }
        }
        return null;
    }

    public String delete(String rowId) {
        // find page with rowId
        for (var path : pagePaths) {
            try (var page = new RelTablePageReadWriteIO(io, path, new TableStructDecorator(struct))) {
                page.openReadWrite();
                if (page.getAvailableCapacity() <= 0) {
                    continue;
                }
                page.deleteRow(rowId);
                if (!page.write()) {
                    continue;
                }
                return rowId;
            }
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        structIo.close();
    }
}
