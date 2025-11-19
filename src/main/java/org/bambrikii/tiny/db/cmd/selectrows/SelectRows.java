package org.bambrikii.tiny.db.cmd.selectrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.select.FromClause;
import org.bambrikii.tiny.db.model.select.SelectClause;
import org.bambrikii.tiny.db.model.select.WhereClause;
import org.bambrikii.tiny.db.plan.Planner;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import java.util.List;

public class SelectRows extends AbstractCommand<SelectRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(SelectRowsMessage cmd, QueryExecutorContext ctx) {
        var key = "";
        var select = cmd.getSelect();
        var from = cmd.getFrom();
        var where = cmd.getWhere();
        var orderBy = cmd.getOrderBy();
        var groupBy = cmd.getGroupBy();

        var plan = new Planner(ctx.getStorage());
        var sb = new StringBuilder();
        appendRows(plan, sb, select, from, where);
        return new ScrollableCommandResult(sb.toString());
    }

    private static void appendRows(
            Planner planner,
            StringBuilder sb,
            List<SelectClause> select,
            List<FromClause> from,
            List<WhereClause> where
    ) {
        for (var col : select) {
            sb.append(col).append(",").append(System.lineSeparator());
        }
        try (var it = planner.execute(from, where)) {
            it.open();
            Row row;
            while ((row = it.next()) != null) {
                sb.append(row.getRowId()).append(",");
                for (var col : select) {
                    sb.append(row.read(col.getTableAlias(), col.getCol())).append(",");
                }
                sb.append(System.lineSeparator());
            }
        }
    }
}
