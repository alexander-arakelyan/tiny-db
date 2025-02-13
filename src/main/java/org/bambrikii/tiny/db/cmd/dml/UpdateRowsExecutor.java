package org.bambrikii.tiny.db.cmd.dml;

import org.bambrikii.tiny.db.exec.*;

public class UpdateRowsExecutor implements AbstractDbExecutor<UpdateRowsCommand, QueryExecutorContext> {
    @Override
    public ExecutionResult tryExec(AbstractDbCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof UpdateRowsCommand
                ? exec((UpdateRowsCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public ExecutionResult exec(UpdateRowsCommand cmd, QueryExecutorContext ctx) {
        return null;
    }
}
