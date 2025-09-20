package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.cmd.createtable.CreateTable;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableMessage;
import org.bambrikii.tiny.db.cmd.droptable.DropTable;
import org.bambrikii.tiny.db.cmd.droptable.DropTableMessage;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsMessage;
import org.bambrikii.tiny.db.cmd.selectrows.ScrollableCommandResult;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRows;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsParser;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.proc.CommandExecutorFacade;
import org.bambrikii.tiny.db.query.CommandParserFacade;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class SelectRowsTest {
    private QueryExecutorContext ctx;

    @BeforeEach
    void beforeEach() {
        ctx = new QueryExecutorContext(new DiskIO(), new MemIO());
    }

    @Test
    void shouldInsert() throws IOException {
        // given
        var tableName = "build/select-rel-rows-1";

        if (new File(tableName).exists()) {
            var dropCmd = new DropTable();
            var dropMsg = new DropTableMessage();
            dropMsg.name(tableName);
            dropCmd.exec(dropMsg, ctx);
        }

        var createCmd = new CreateTable();
        var createMsg = new CreateTableMessage();
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
        var execFacade = new CommandExecutorFacade(ctx);
        execFacade.init(new CommandStack(parser, new SelectRows()));
        var query = "select t1.col1, t1.col2, t1.col3 from \"" + tableName + "\" t1 where t1.col1 = 'val1'";
        try (
                var bis = new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8));
                var nis = new NavigableStreamReader(bis);
        ) {
            var msg = parserFacade.parse(nis);
            var result = execFacade.exec(msg);
            assertThat(result).isInstanceOf(ScrollableCommandResult.class);
            var scrollable = (ScrollableCommandResult) result;
            assertThat(scrollable.getRecordsAsStr()).isNotBlank();
            DbLogger.log(result, result.toString());
        }
    }
}
