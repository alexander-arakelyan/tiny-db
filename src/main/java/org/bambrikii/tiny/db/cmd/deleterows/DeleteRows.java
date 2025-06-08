package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class DeleteRows extends AbstractCommand<DeleteRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(DeleteRowsMessage cmd, QueryExecutorContext ctx) {
        var key = cmd.getName();

        ctx.getStorage().delete(key, aBoolean -> false); // TODO: apply filter

        return OK_COMMAND_RESULT;
    }
}
