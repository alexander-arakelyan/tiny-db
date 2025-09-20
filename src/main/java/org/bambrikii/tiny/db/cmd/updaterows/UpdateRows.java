package org.bambrikii.tiny.db.cmd.updaterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.Planner;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class UpdateRows extends AbstractCommand<UpdateRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(UpdateRowsMessage cmd, QueryExecutorContext ctx) {
        var key = cmd.getTargetTable();

        var targetTable = cmd.getTargetTable();

        var targetValues = cmd.getTargetValues();

        var from = cmd.getFrom();
        var where = cmd.getWhere();

        var storage = ctx.getStorage();
        var planner = new Planner(storage);
        try (var it = planner.execute(from, where)) {
            Row row;
            while ((row = it.next()) != null) {
                storage.update(targetTable, row, InsertRows.resolveValues(targetTable, targetValues));
            }
        }
        return OK_COMMAND_RESULT;
    }
}
