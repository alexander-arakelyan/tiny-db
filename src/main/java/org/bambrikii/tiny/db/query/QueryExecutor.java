package org.bambrikii.tiny.db.query;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.cmd.*;
import org.bambrikii.tiny.db.cmd.altertable.AlterTableParser;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableParser;
import org.bambrikii.tiny.db.cmd.createtable.NavigableStreamReader;
import org.bambrikii.tiny.db.cmd.deleterows.DeleteRowsParser;
import org.bambrikii.tiny.db.cmd.droptable.DropTableParser;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsParser;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsParser;
import org.bambrikii.tiny.db.cmd.updaterows.UpdateRowsParser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;

public class QueryExecutor {
    private final List<AbstractCommandParser> parsers;

    public QueryExecutor(AbstractCommandParser... parsers) {
        this.parsers = Arrays
                .stream(parsers)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public AbstractCommand parse(NavigableStreamReader nsr) {
        for (var parser : parsers) {
            long mark = nsr.mark();
            var cmd = parser.parse(nsr);
            if (cmd == NO_COMMAND) {
                nsr.rollback(mark);
                continue;
            }
            return cmd;
        }
        return NO_COMMAND;
    }
}
