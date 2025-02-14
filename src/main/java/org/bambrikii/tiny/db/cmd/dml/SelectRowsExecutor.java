package org.bambrikii.tiny.db.cmd.dml;

import org.bambrikii.tiny.db.query.*;

public class SelectRowsExecutor implements AbstractDbExecutor<SelectRowsCommand, QueryExecutorContext> {
    @Override
    public ExecutionResult tryExec(AbstractDbCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof SelectRowsCommand
                ? exec((SelectRowsCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public ExecutionResult exec(SelectRowsCommand cmd, QueryExecutorContext ctx) {
        return null;
    }
}
