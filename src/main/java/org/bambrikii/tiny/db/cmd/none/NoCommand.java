package org.bambrikii.tiny.db.cmd.none;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;

public final class NoCommand implements AbstractCommand {
    public static final NoCommand NO_COMMAND = new NoCommand();

    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        return null;
    }
}
