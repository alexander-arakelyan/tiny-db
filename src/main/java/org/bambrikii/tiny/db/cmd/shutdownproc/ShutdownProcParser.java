package org.bambrikii.tiny.db.cmd.shutdownproc;

import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.chars;

public class ShutdownProcParser extends AbstractCommandParser<ShutdownProcMessage> {
    @Override
    public AbstractMessage parse(ParserInputStream nsr) {
        return chars("shutdown", TRUE_PREDICATE).test(nsr)
                ? new ShutdownProcMessage()
                : NO_MESSAGE;
    }
}
