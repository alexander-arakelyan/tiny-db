package org.bambrikii.tiny.db.storage.tables;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.disk.FileOps;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * table files format:
 */
@RequiredArgsConstructor
public class RelTableScanIO implements Scrollable {
    private final DiskIO io;
    private final String name;
    private RandomAccessFile raf;
    private FileChannel channel;
    private FileOps ops;
    private List<String> cols;
    private Object vals;

    @Override
    public void open() {
        this.raf = io.open(name + "/data.txt");
        this.channel = raf.getChannel();
        this.ops = new FileOps(channel);
    }

    @Override
    public Row next() {
        // read next row
        return null;
    }

    @Override
    public void reset() {
        // reset to first row
    }

    @SneakyThrows
    @Override
    public void close() {
        raf.close();
    }


    @SneakyThrows
    public void read() {
//        this.name = readName();
        this.cols = readCols();
        this.vals = readVals();
    }

    private String readName() throws IOException {
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

    private Object readVals() {
        for (var col : cols) {
            ops.readObj(col);
        }
        int count = ops.
        return null;
    }
}
