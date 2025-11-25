package org.bambrikii.tiny.db.cmd.droptable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class DropTable extends AbstractCommand<DropTableMessage, AbstractExecutorContext> {
    @Override
    public CommandResult exec(DropTableMessage cmd, AbstractExecutorContext ctx) {
        var key = cmd.getName();

        ctx.getStorage().drop(key);

        return OK_COMMAND_RESULT;
    }
}
