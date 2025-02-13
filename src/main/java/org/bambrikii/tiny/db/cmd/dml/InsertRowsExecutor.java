package org.bambrikii.tiny.db.cmd.dml;

import org.bambrikii.tiny.db.exec.*;

public class InsertRowsExecutor implements AbstractDbExecutor<InsertRowsCommand, QueryExecutorContext> {
    @Override
    public ExecutionResult tryExec(AbstractDbCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof InsertRowsCommand
                ? exec((InsertRowsCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public ExecutionResult exec(InsertRowsCommand cmd, QueryExecutorContext ctx) {
        return null;
    }
}
