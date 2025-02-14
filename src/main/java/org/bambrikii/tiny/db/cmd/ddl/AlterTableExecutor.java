package org.bambrikii.tiny.db.cmd.ddl;

import org.bambrikii.tiny.db.query.*;

public class AlterTableExecutor implements AbstractDbExecutor<AlterTableCommand, QueryExecutorContext> {
    @Override
    public ExecutionResult tryExec(AbstractDbCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof AlterTableCommand
                ? exec((AlterTableCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public ExecutionResult exec(AlterTableCommand cmd, QueryExecutorContext ctx) {
        return null;
    }
}
