package org.bambrikii.tiny.db.algo.relio;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.utils.RelColumnType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RelTableScanIOTest {
    @SneakyThrows
    @Test
    void shouldReadAndWriteRelTable() {
        var io = new DiskIO();
        try (var schemaIo = new RelTableStructWriteIO(io, "tbl1")) {
            schemaIo.open();
            var struct = new TableStruct();
            struct.setTable("tbl1");
            struct.setSchema("schema1");
            var col1 = new Column();
            col1.setName("col1");
            col1.setType(RelColumnType.STRING.getAliases().get(0));
            struct.getColumns().add(col1);
            schemaIo.write(struct);
        }

        try (var schemaIo = new RelTableStructReadIO(io, "tbl1")) {
            schemaIo.open();
            var struct = schemaIo.read();
            System.out.println(struct);
        }

        try (var writeIo = new RelTableWriteIO(io, "tbl1")) {
            writeIo.open();
            for (int i = 0; i < 5; i++) {
                writeIo.insert(Map.of("col1", "val" + i));
            }
        }
        try (var scanIo = new RelTableScanIO(io, "tbl1")) {
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
