package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.cmd.createtable.CreateTable;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableMessage;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsMessage;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRows;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsParser;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.proc.CommandExecutorFacade;
import org.bambrikii.tiny.db.query.CommandParserFacade;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class SelectRowsTest {
    private QueryExecutorContext ctx;

    @BeforeEach
    void beforeEach() {
        ctx = new QueryExecutorContext(new DiskIO(), new MemIO());
    }

    @Test
    void shouldInsert() throws IOException {
        // given
        var createCmd = new CreateTable();
        var createMsg = new CreateTableMessage();
        var tableName = "build/insert-rel-rows-1";
        createMsg.name(tableName);
        createMsg.addColumn("col1", "str", 0, 0, false, false);
        createMsg.addColumn("col2", "str", 0, 0, false, false);
        createMsg.addColumn("col3", "str", 0, 0, false, false);
        createCmd.exec(createMsg, ctx);

        var insertRows = new InsertRows();
        var insertMsg = new InsertRowsMessage();
        insertMsg.into(tableName);
        insertMsg.columnValue("col1", "val1");
        insertMsg.columnValue("col2", "val2");
        insertMsg.columnValue("col3", "val3");

        // when
        insertRows.exec(insertMsg, ctx);

        // then
        var cmd = new SelectRows();
        var parserFacade = new CommandParserFacade();
        var parser = new SelectRowsParser();
        parserFacade.init(parser);
        var disk = new DiskIO();
        var mem = new MemIO();
        var ctx = new QueryExecutorContext(disk, mem);
        var execFacade = new CommandExecutorFacade(ctx);
        execFacade.init(new CommandStack(parser, new SelectRows()));
        var query = "select t1.col1, t1.col2, t1.col3 from \"" + tableName + "\" t1 where t1.col1 = 'val1'";
        try (
                var bis = new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8));
                var nis = new NavigableStreamReader(bis);
        ) {
            var msg = parserFacade.parse(nis);
            execFacade.exec(msg);
        }
    }
}
