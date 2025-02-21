package org.bambrikii.tiny.db.cmd.updaterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractDbExecutor;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.*;

public class UpdateRowsExecutor implements AbstractDbExecutor<UpdateRowsCommand, QueryExecutorContext> {
    @Override
    public CommandResult tryExec(AbstractCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof UpdateRowsCommand
                ? exec((UpdateRowsCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public CommandResult exec(UpdateRowsCommand cmd, QueryExecutorContext ctx) {
        return null;
    }
}
