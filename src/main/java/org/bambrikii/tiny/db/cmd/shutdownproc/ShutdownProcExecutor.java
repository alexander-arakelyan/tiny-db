package org.bambrikii.tiny.db.cmd.shutdownproc;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractDbExecutor;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.*;

public class ShutdownProcExecutor implements AbstractDbExecutor<ShutdownProcCommand, QueryExecutorContext> {
    @Override
    public CommandResult tryExec(AbstractCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof ShutdownProcCommand
                ? exec((ShutdownProcCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public CommandResult exec(ShutdownProcCommand cmd, QueryExecutorContext ctx) {
        ctx.shutdown();
        return new CommandResult();
    }
}
