package org.bambrikii.tiny.db.utils;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.CommandStack;
import org.bambrikii.tiny.db.cmd.NavigableStreamReader;
import org.bambrikii.tiny.db.cmd.createtable.CreateTable;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableMessage;
import org.bambrikii.tiny.db.cmd.droptable.DropTable;
import org.bambrikii.tiny.db.cmd.droptable.DropTableMessage;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsMessage;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRows;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsMessage;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsParser;
import org.bambrikii.tiny.db.proc.CommandExecutorFacade;
import org.bambrikii.tiny.db.query.CommandParserFacade;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class TableTestUtils {
    private final QueryExecutorContext ctx;

    public TableTestUtils dropTable(String tableName) {
        if (!new File(tableName).exists()) {
            return this;
        }
        var dropCmd = new DropTable();
        var dropMsg = new DropTableMessage();
        dropMsg.name(tableName);
        dropCmd.exec(dropMsg, ctx);
        return this;
    }

    public TableTestUtils createTable(String tableName, Consumer<CreateTableMessage> addColumns) {
        var createCmd = new CreateTable();
        var createMsg = new CreateTableMessage();
        createMsg.name(tableName);
        addColumns.accept(createMsg);
        createCmd.exec(createMsg, ctx);
        return this;
    }

    public TableTestUtils insert(String tableName, Consumer<InsertRowsMessage> insertRowsConsumer) {
        var insertRows = new InsertRows();
        var insertMsg = new InsertRowsMessage();
        insertMsg.into(tableName);
        insertRowsConsumer.accept(insertMsg);
        insertRows.exec(insertMsg, ctx);
        return this;
    }

    public TableTestUtils select(String query, Consumer<CommandResult> consumer) throws IOException {
        var selectRows = new SelectRows();

        var parserFacade = new CommandParserFacade();
        var parser = new SelectRowsParser();
        parserFacade.init(parser);
        var execFacade = new CommandExecutorFacade(ctx);
        execFacade.init(new CommandStack(parser, new SelectRows()));
        try (
                var bis = new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8));
                var nis = new NavigableStreamReader(bis);
        ) {
            var msg = parserFacade.parse(nis);
            System.out.println(msg);
            assertThat(msg).isInstanceOf(SelectRowsMessage.class);

            var res = selectRows.exec((SelectRowsMessage) msg, ctx);
            System.out.println(res);
            consumer.accept(res);
        }
        return this;
    }
}
