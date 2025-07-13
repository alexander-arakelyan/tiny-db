package org.bambrikii.tiny.db.cmd.selectrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.ExecutionPlanBuilder;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

public class SelectRows extends AbstractCommand<SelectRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(SelectRowsMessage cmd, QueryExecutorContext ctx) {
        var key = "";
        var columns = cmd.getColumns();
        var filters = cmd.getFilters();
        var tables = cmd.getTables();

        var planBuilder = new ExecutionPlanBuilder(ctx.getStorage());
        var sb = new StringBuilder();
        for (var col : columns) {
            sb.append(col).append(",").append(System.lineSeparator());
        }
        try (var it = planBuilder.execute(tables, filters)) {
            Row row;
            while ((row = it.next()) != null) {
                sb.append(row.getRowId());
                for (var col : columns) {
                    sb.append(row.read(String.format("%s.%s", col.getAlias(), col.getName()))).append(",");
                }
                sb.append(System.lineSeparator());
            }
        }
        return new ScrollableCommandResult(sb.toString());
    }
}
