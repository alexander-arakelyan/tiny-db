package org.bambrikii.tiny.db.cmd.ddl;

import org.bambrikii.tiny.db.query.*;

public class CreateTableExecutor implements AbstractDbExecutor<CreateTableCommand, QueryExecutorContext> {
    @Override
    public ExecutionResult tryExec(AbstractDbCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof CreateTableCommand
                ? exec((CreateTableCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public ExecutionResult exec(CreateTableCommand cmd, QueryExecutorContext ctx) {

        return null;
    }
}
