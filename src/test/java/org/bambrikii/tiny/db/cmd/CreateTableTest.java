package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.cmd.createtable.CreateTable;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableMessage;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateTableTest {
    private QueryExecutorContext ctx;

    @BeforeEach
    void beforeEach() {
        ctx = new QueryExecutorContext(new DiskIO(), new MemIO());
    }

    @Test
    void shouldCreate() {
        // given
        var cmd = new CreateTable();
        var msg = new CreateTableMessage();
        msg.name("build/create-rel-table-1");
        msg.addColumn("col1", "str", 0, 0, false, false);
        msg.addColumn("col2", "str", 0, 0, false, false);
        msg.addColumn("col3", "str", 0, 0, false, false);

        // when
        cmd.exec(msg, ctx);

        // then
        // TODO:
    }
}
