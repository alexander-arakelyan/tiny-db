package org.bambrikii.tiny.db.cmd.droptable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractDbExecutor;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.*;

public class DropTableExecutor implements AbstractDbExecutor<DropTableCommand, AbstractExecutorContext> {
    @Override
    public CommandResult tryExec(AbstractCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof DropTableCommand
                ? exec((DropTableCommand) cmd, ctx)
                : null;
    }

    @Override
    public CommandResult exec(DropTableCommand cmd, AbstractExecutorContext ctx) {
        return null;
    }
}
