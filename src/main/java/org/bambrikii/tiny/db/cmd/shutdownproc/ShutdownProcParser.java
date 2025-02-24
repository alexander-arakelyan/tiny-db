package org.bambrikii.tiny.db.cmd.shutdownproc;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;

public class ShutdownProcParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream nsr) {
        if (nsr.word("shutdown") && nsr.string(";")) {
            return new ShutdownProcCommand();
        }
        return NO_COMMAND;
    }
}
