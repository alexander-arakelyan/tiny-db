package org.bambrikii.tiny.db.query;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.ArrayList;
import java.util.List;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;

public class CommandParserFacade {
    private final List<AbstractCommandParser> parsers = new ArrayList<>();

    public CommandParserFacade init(AbstractCommandParser parser) {
        this.parsers.add(parser);
        return this;
    }

    @SneakyThrows
    public AbstractMessage parse(ParserInputStream nsr) {
        for (var parser : parsers) {
            var mark = nsr.pos();
            var cmd = parser.parse(nsr);
            if (cmd == NO_MESSAGE) {
                nsr.rollback(mark);
                continue;
            }
            return cmd;
        }
        return NO_MESSAGE;
    }
}
