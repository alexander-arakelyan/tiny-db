package org.bambrikii.tiny.db.cmd.updaterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class UpdateRows extends AbstractCommand<UpdateRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(UpdateRowsMessage cmd, QueryExecutorContext ctx) {
        var key = cmd.getName();

        ctx.getStorage().delete(key, aBoolean -> false); // TODO: apply filter
        ctx.getStorage().append(key, cmd.getValues());

        return OK_COMMAND_RESULT;
    }
}
