package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractDbExecutor;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.*;

public class DeleteRowsExecutor implements AbstractDbExecutor<DeleteRowsCommand, QueryExecutorContext> {
    @Override
    public CommandResult tryExec(AbstractCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof DeleteRowsCommand
                ? exec((DeleteRowsCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public CommandResult exec(DeleteRowsCommand cmd, QueryExecutorContext ctx) {

        return null;
    }
}
