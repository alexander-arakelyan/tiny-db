package org.bambrikii.tiny.db.cmd.dml;

import org.bambrikii.tiny.db.query.*;

public class DeleteRowsExecutor implements AbstractDbExecutor<DeleteRowsCommand, QueryExecutorContext> {
    @Override
    public ExecutionResult tryExec(AbstractDbCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof DeleteRowsCommand
                ? exec((DeleteRowsCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public ExecutionResult exec(DeleteRowsCommand cmd, QueryExecutorContext ctx) {

        return null;
    }
}
