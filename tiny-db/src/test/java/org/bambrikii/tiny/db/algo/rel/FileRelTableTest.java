package org.bambrikii.tiny.db.algo.rel;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.algo.rel.disk.FileRelTable;
import org.bambrikii.tiny.db.algo.rel.disk.FileRelTableStructReader;
import org.bambrikii.tiny.db.algo.rel.disk.FileRelTableStructWriter;
import org.bambrikii.tiny.db.algo.rel.disk.FileRelTableWriter;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.algo.RelColumnType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FileRelTableTest {
    @SneakyThrows
    @Test
    void shouldReadAndWriteRelTable() {
        var io = new DiskIO();
        try (var schemaIo = new FileRelTableStructWriter(io, "build/tbl1-rel-table-scan")) {
            schemaIo.open();
            var struct = new TableStruct();
            struct.setTable("build/tbl1-rel-table-scan");
            struct.setSchema("schema1");
            var col1 = new Column();
            col1.setName("col1");
            col1.setType(RelColumnType.STRING.getAliases().get(0));
            struct.getColumns().add(col1);
            schemaIo.write(struct);
        }

        try (var schemaIo = new FileRelTableStructReader(io, "build/tbl1-rel-table-scan")) {
            schemaIo.open();
            var struct = schemaIo.read();
            System.out.println(struct);
        }

        try (var writeIo = new FileRelTableWriter(io, "build/tbl1-rel-table-scan")) {
            writeIo.open();
            for (int i = 0; i < 5; i++) {
                writeIo.insert(Map.of("col1", "val" + i));
            }
        }
        try (var scanIo = new FileRelTable(io, "build/tbl1-rel-table-scan")) {
            scanIo.open();
            Row row;
            int n = 0;
            while ((row = scanIo.next()) != null) {
                System.out.println("Test row read: " + row);
                n++;
            }
            assertThat(n).isGreaterThan(0);
        }
    }
}
