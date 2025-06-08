package org.bambrikii.tiny.db.cmd.shutdownproc;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.SequencePredicate.chars;

public class ShutdownProcParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream nsr) {
        return chars("shutdown", TRUE_PREDICATE).test(nsr)
                ? new ShutdownProcCommand()
                : NO_COMMAND;
    }
}
