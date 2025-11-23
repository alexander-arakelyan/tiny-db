package org.bambrikii.tiny.db.cmd.shutdownproc;

import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.functions.CharsFunctions.chars;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.TRUE_PREDICATE;

public class ShutdownProcParser extends AbstractCommandParser<ShutdownProcMessage> {
    @Override
    public AbstractMessage parse(ParserInputStream nsr) {
        return chars("shutdown", TRUE_PREDICATE).test(nsr)
                ? new ShutdownProcMessage()
                : NO_MESSAGE;
    }
}
