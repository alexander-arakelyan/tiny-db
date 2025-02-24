package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.cmd.*;
import org.bambrikii.tiny.db.query.*;

public class AlterTableExecutor implements AbstractDbExecutor<AlterTableCommand, QueryExecutorContext> {
    @Override
    public CommandResult tryExec(AbstractCommand cmd, AbstractExecutorContext ctx) {
        return cmd instanceof AlterTableCommand
                ? exec((AlterTableCommand) cmd, (QueryExecutorContext) ctx)
                : null;
    }

    @Override
    public CommandResult exec(AlterTableCommand cmd, QueryExecutorContext ctx) {
        return null;
    }
}
