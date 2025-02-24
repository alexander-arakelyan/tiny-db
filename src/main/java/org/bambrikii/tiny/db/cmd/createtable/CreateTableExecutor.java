package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractDbExecutor;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;

public class CreateTableExecutor implements AbstractDbExecutor<CreateTableCommand, AbstractExecutorContext> {
    @Override
    public CommandResult tryExec(AbstractCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof CreateTableCommand
                ? exec((CreateTableCommand) cmd, ctx)
                : null;
    }

    @Override
    public CommandResult exec(CreateTableCommand cmd, AbstractExecutorContext ctx) {

        return null;
    }
}
