package org.bambrikii.tiny.db.cmd.selectrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.select.ColumnRef;
import org.bambrikii.tiny.db.plan.ExecutionPlanBuilder;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import java.util.List;

public class SelectRows extends AbstractCommand<SelectRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(SelectRowsMessage cmd, QueryExecutorContext ctx) {
        var key = "";
        var columns = cmd.getColumns();
        var filters = cmd.getFilters();
        var tables = cmd.getTables();

        var planBuilder = new ExecutionPlanBuilder(ctx.getStorage());
        var sb = new StringBuilder();
        appendCols(columns, sb);
        appendRows(planBuilder, tables, filters, sb, columns);
        return new ScrollableCommandResult(sb.toString());
    }

    private static void appendRows(ExecutionPlanBuilder planBuilder, List<Join> tables, List<Filter> filters, StringBuilder sb, List<ColumnRef> columns) {
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
    }

    private static void appendCols(List<ColumnRef> columns, StringBuilder sb) {
        for (var col : columns) {
            sb.append(col).append(",").append(System.lineSeparator());
        }
    }
}
