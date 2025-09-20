package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.cmd.createtable.CreateTable;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableMessage;
import org.bambrikii.tiny.db.cmd.droptable.DropTable;
import org.bambrikii.tiny.db.cmd.droptable.DropTableMessage;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsMessage;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class DropTableTest {
    private QueryExecutorContext ctx;

    @BeforeEach
    void beforeEach() {
        ctx = new QueryExecutorContext(new DiskIO(), new MemIO());
    }

    @Test
    void shouldAlter() {
        // given
        var name = "build/drop-rel-table-1";

        var createCmd = new CreateTable();
        var createMsg = new CreateTableMessage();
        createMsg.name(name);
        createMsg.addColumn("col1", "str", 0, 0, false, false);
        createMsg.addColumn("col2", "str", 0, 0, false, false);
        createCmd.exec(createMsg, ctx);

        var insertRowsCmd = new InsertRows();
        var insertRowsMsg = new InsertRowsMessage();
        insertRowsMsg.into(name);
        insertRowsMsg.columnValue("col1", "val1");
        insertRowsMsg.columnValue("col2", "val2");
        insertRowsCmd.exec(insertRowsMsg, ctx);

        var cmd = new DropTable();
        var msg = new DropTableMessage();
        msg.name(name);

        // when
        cmd.exec(msg, ctx);

        // then
        assertThat(Files.exists(Path.of(name))).isFalse();
    }
}
