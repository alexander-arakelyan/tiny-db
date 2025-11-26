package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.cmd.selectrows.ScrollableCommandResult;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRows;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsParser;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.proc.CommandExecutorFacade;
import org.bambrikii.tiny.db.proc.CommandParserFacade;
import org.bambrikii.tiny.db.proc.QueryExecutorContext;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.mem.MemIO;
import org.bambrikii.tiny.db.utils.TableTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
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
    void shouldSelect() throws IOException {
        // given
        var tableName = "build/clauses-rel-rows-1";
        new TableTestUtils(ctx)
                .dropTable(tableName)
                .createTable(tableName, msg -> {
                    msg.addColumn("col1", "str", 0, 0, false, false);
                    msg.addColumn("col2", "str", 0, 0, false, false);
                    msg.addColumn("col3", "str", 0, 0, false, false);
                })
                .insert(tableName, msg -> {
                    msg.columnValue("col1", "val1");
                    msg.columnValue("col2", "val2");
                    msg.columnValue("col3", "val3");
                });

        // then
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
