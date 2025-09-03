package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.cmd.createtable.CreateTable;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableMessage;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsMessage;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsertRowsTest {
    private QueryExecutorContext ctx;

    @BeforeEach
    void beforeEach() {
        ctx = new QueryExecutorContext(new DiskIO(), new MemIO());
    }

    @Test
    void shouldInsert() {
        // given
        var createCmd = new CreateTable();
        var createMsg = new CreateTableMessage();
        createMsg.name("build/insert-rel-rows-1");
        createMsg.addColumn("col1", "str", 0, 0, false, false);
        createMsg.addColumn("col2", "str", 0, 0, false, false);
        createMsg.addColumn("col3", "str", 0, 0, false, false);
        createCmd.exec(createMsg, ctx);

        var cmd = new InsertRows();
        var msg = new InsertRowsMessage();
        msg.into("build/insert-rel-rows-1");
        msg.columnValue("col1", "val1");
        msg.columnValue("col2", "val2");
        msg.columnValue("col3", "val3");

        // when
        cmd.exec(msg, ctx);

        // then
        // TODO:
    }
}
