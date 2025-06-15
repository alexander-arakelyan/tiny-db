package org.bambrikii.tiny.db.cmd.selectrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class SelectRows extends AbstractCommand<SelectRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(SelectRowsMessage cmd, QueryExecutorContext ctx) {
        var key = "";
        var select = cmd.getColumns();
        var where = cmd.getFilters();
        var join = cmd.getTables();


        ctx.getStorage().read(key);

        return OK_COMMAND_RESULT;
    }
}
