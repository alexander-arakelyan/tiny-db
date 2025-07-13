package org.bambrikii.tiny.db.storage.tables;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.disk.FileOps;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RelTablePageIO implements AutoCloseable {
    private final DiskIO io;
    private final String name;
    private final Path path;
    private RandomAccessFile raf;
    private FileChannel channel;
    private FileOps ops;
    private List<String> cols;
    private List<Row> rows;
    private int rowN;

    @SneakyThrows
    public void open() {
        this.raf = io.open(path.toString());
        this.channel = raf.getChannel();
        this.ops = new FileOps(channel);
        var name = readName();
        this.cols = readCols();
        this.rowN = 0;
    }

    @SneakyThrows
    @Override
    public void close() {
        raf.close();
    }

    public Row next() {
        if (rows == null) {
            this.rows = readRows();
        }
        return null;
    }

    private String readName() {
        return ops.readStr(channel);
    }

    @SneakyThrows
    private List<String> readCols() {
        var count = ops.readInt(channel);
        var cols = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            cols.add(ops.readStr(channel));
        }
        return cols;
    }

    private List<Row> readRows() {
        var rows = new ArrayList<Row>();
        Row row;
        while ((row = readRow()) != null) {
            rows.add(row);
        }
        return rows;
    }

    private Row readRow() {
        for (var col : cols) {
            col
            ops.readObj(col);
            raf.read()
        }
        int count = ops.

    }
}
