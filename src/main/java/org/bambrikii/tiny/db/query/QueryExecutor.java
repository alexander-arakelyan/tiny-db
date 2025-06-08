package org.bambrikii.tiny.db.query;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.cmd.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;

public class QueryExecutor {
    private final List<AbstractCommandParser> parsers;

    public QueryExecutor(AbstractCommandParser... parsers) {
        this.parsers = Arrays
                .stream(parsers)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public AbstractCommand parse(ParserInputStream nsr) {
        for (var parser : parsers) {
            var mark = nsr.pos();
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
