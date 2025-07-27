package org.bambrikii.tiny.db.storagelayout.relio;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.utils.TableStructDecorator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.bambrikii.tiny.db.storagelayout.relio.RelTableFileUtils.PAGE_FILE_NAME;

/**
 * table files format:
 */
@RequiredArgsConstructor
public class RelTableScanIO implements Scrollable {
    private final DiskIO io;
    private final String name;
    private List<Path> pages;
    private int pageN;
    private RelTableStructReadIO structIo;
    private RelTablePageReadIO pageIo;
    private TableStruct struct;

    @SneakyThrows
    @Override
    public void open() {
        structIo = new RelTableStructReadIO(io, name);
        structIo.open();
        struct = structIo.read();
        pages = Files
                .list(Path.of(name))
                .filter(name -> PAGE_FILE_NAME.matcher(name.toString()).matches())
                .collect(Collectors.toList());
        pageN = 0;
    }

    @Override
    public Row next() {
        if (pageIo == null) {
            if (pageN >= pages.size()) {
                return null;
            }
            tryClosePage();
            openPage();
        }
        Row row;
        while ((row = pageIo.next()) == null && ++pageN < pages.size()) {
            tryClosePage();
            openPage();
        }
        return row;
    }

    private void openPage() {
        pageIo = new RelTablePageReadIO(io, pages.get(pageN), new TableStructDecorator(struct));
        pageIo.open();
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
        if (pageIo != null) {
            pageIo.close();
        }
        pageIo = null;
    }
}
