package org.bambrikii.tiny.db.cmd.selectrows;

import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.where;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.chars;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.or;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.ordered;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class SelectRowsParser extends AbstractCommandParser {
    @Override
    public AbstractMessage parse(ParserInputStream is) {
        var cmd = new SelectRowsMessage();
        return ordered(
                chars("select", atLeastOnceCommaSeparated(or(
                        word(TRUE_PREDICATE, cmd::select)
                ))),
                chars("from", word(TRUE_PREDICATE, cmd::table)),
                where(cmd)
        ).test(is)
                ? cmd
                : NO_MESSAGE;
    }
}
