package org.bambrikii.tiny.db.cmd.selectrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractDbExecutor;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.*;

public class SelectRowsExecutor implements AbstractDbExecutor<SelectRowsCommand, QueryExecutorContext> {
    @Override
    public CommandResult tryExec(AbstractCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof SelectRowsCommand
                ? exec((SelectRowsCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public CommandResult exec(SelectRowsCommand cmd, QueryExecutorContext ctx) {
        return null;
    }
}
