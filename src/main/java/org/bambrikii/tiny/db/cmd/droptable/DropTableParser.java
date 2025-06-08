package org.bambrikii.tiny.db.cmd.droptable;

import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.drop;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.table;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class DropTableParser extends AbstractCommandParser {
    @Override
    public AbstractMessage parse(ParserInputStream input) {
        var cmd = new DropTableMessage();
        return drop(table(word(TRUE_PREDICATE, cmd::name))).test(input)
                ? cmd
                : NO_MESSAGE;
    }
}
