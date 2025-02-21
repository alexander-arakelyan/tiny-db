package org.bambrikii.tiny.db.cmd.shutdownproc;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.createtable.NavigableStreamReader;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;

public class ShutdownProcParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(NavigableStreamReader nsr) {
        if (nsr.readWord("shutdown") && nsr.readString(";")) {
            return new ShutdownProcCommand();
        }
        return NO_COMMAND;
    }
}
