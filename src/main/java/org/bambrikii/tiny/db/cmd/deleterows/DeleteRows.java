package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.ExecutionPlanBuilder;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class DeleteRows extends AbstractCommand<DeleteRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(DeleteRowsMessage cmd, QueryExecutorContext ctx) {
        var targetTable = cmd.getTargetTable();

        var columns = cmd.getColumns();
        var filters = cmd.getFilters();
        var tables = cmd.getTables();

        var storage = ctx.getStorage();
        var planBuilder = new ExecutionPlanBuilder(storage);
        try (var it = planBuilder.execute(tables, filters)) {
            Row row;
            while ((row = it.next()) != null) {
                storage.delete(targetTable, row.getRowId());
            }
        }

        return OK_COMMAND_RESULT;
    }
}
