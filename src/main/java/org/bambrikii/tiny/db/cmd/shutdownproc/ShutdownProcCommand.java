package org.bambrikii.tiny.db.cmd.shutdownproc;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.NO_COMMAND_RESULT;

public class ShutdownProcCommand implements AbstractCommand {
    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        if (ctx instanceof QueryExecutorContext) {
            ((QueryExecutorContext) ctx).shutdown();
            return new CommandResult();
        }
        return NO_COMMAND_RESULT;
    }
}
