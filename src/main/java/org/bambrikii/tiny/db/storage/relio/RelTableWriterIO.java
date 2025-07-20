package org.bambrikii.tiny.db.storage.relio;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.disk.FileOps;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.List;

import static org.bambrikii.tiny.db.storage.disk.FileOps.ROW_ID_COLUMN_NAME;

@RequiredArgsConstructor
public class RelTableWriterIO implements AutoCloseable {
    private final DiskIO io;
    private final String name;
    private final Path path;
    private final TableStruct struct;
    private RandomAccessFile raf;
    private FileChannel channel;
    private FileOps ops;

    @SneakyThrows
    public void open() {
        this.raf = io.open(path.toString());
        this.channel = raf.getChannel();
        this.ops = new FileOps(channel);
        var name = struct.getTable();
    }


    public void writeHeader() {
        var columns = struct.getColumns();
        ops.writeStr(ROW_ID_COLUMN_NAME);
        for (int i = 0; i < columns.size(); i++) {
            ops.writeColSeparator();
            ops.writeStr(columns.get(i).getName());
        }
        ops.writeLineSeparator();
    }

    public void writeRow(List<Object> vals) {
        ops.writeRowId();
        for (int i = 0; i < vals.size(); i++) {
            ops.writeColSeparator();
            ops.writeObj(vals.get(i));
        }
        ops.writeLineSeparator();
    }

    public void update(String rowId) {

    }

    @Override
    public void close() throws Exception {
        raf.close();
    }
}
