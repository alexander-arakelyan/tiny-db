package org.bambrikii.tiny.db.cmd.ddl;

import org.bambrikii.tiny.db.exec.*;

public class DropTableExecutor implements AbstractDbExecutor<DropTableCommand, QueryExecutorContext> {
    @Override
    public ExecutionResult tryExec(AbstractDbCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof DropTableCommand
                ? exec((DropTableCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public ExecutionResult exec(DropTableCommand cmd, QueryExecutorContext ctx) {
        return null;
    }
}
