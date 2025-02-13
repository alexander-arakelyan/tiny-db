package org.bambrikii.tiny.db.cmd.proc;

import org.bambrikii.tiny.db.exec.*;

public class ShutdownProcExecutor implements AbstractDbExecutor<ShutdownProcCommand, QueryExecutorContext> {
    @Override
    public ExecutionResult tryExec(AbstractDbCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof ShutdownProcCommand
                ? exec((ShutdownProcCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public ExecutionResult exec(ShutdownProcCommand cmd, QueryExecutorContext ctx) {
        ctx.shutdown();
        return new ExecutionResult();
    }
}
