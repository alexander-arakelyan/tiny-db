package org.bambrikii.tiny.db.cmd.shutdownproc;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

public class ShutdownProc extends AbstractCommand<ShutdownProcMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(ShutdownProcMessage cmd, QueryExecutorContext ctx) {
        ctx.shutdown();
        return new CommandResult();
    }
}
