package org.bambrikii.tiny.db.cmd.insertrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractDbExecutor;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.*;

public class InsertRowsExecutor implements AbstractDbExecutor<InsertRowsCommand, QueryExecutorContext> {
    @Override
    public CommandResult tryExec(AbstractCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof InsertRowsCommand
                ? exec((InsertRowsCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public CommandResult exec(InsertRowsCommand cmd, QueryExecutorContext ctx) {
        return null;
    }
}
