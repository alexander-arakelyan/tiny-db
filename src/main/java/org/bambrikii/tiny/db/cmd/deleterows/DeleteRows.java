package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.Planner;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class DeleteRows extends AbstractCommand<DeleteRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(DeleteRowsMessage cmd, QueryExecutorContext ctx) {
        var targetTable = cmd.getTargetTable();

        var tables = cmd.getFrom();
        var filters = cmd.getWhere();

        var storage = ctx.getStorage();
        var planner = new Planner(storage);
        try (var it = planner.execute(tables, filters)) {
            Row row;
            while ((row = it.next()) != null) {
                storage.delete(targetTable, row.getRowId());
            }
        }

        return OK_COMMAND_RESULT;
    }
}
