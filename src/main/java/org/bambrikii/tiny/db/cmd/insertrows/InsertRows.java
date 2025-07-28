package org.bambrikii.tiny.db.cmd.insertrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.ExecutionPlanBuilder;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class InsertRows extends AbstractCommand<InsertRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(InsertRowsMessage cmd, QueryExecutorContext ctx) {
        var targetTable = cmd.getTargetTable();

        var targetValues = cmd.getTargetValues();

        var columns = cmd.getColumns();
        var filters = cmd.getFilters();
        var tables = cmd.getTables();

        var storage = ctx.getStorage();
        insert(storage, tables, filters, targetTable, resolveValues(targetValues));

        return OK_COMMAND_RESULT;
    }

    public static void insert(
            StorageContext storage,
            List<Join> tables,
            List<Filter> filters,
            String targetTable,
            Function<Row, Map<String, Object>> valuesResolver
    ) {
        var planBuilder = new ExecutionPlanBuilder(storage);
        try (var it = planBuilder.execute(tables, filters)) {
            Row row;
            while ((row = it.next()) != null) {
                storage.insert(targetTable, row, valuesResolver);
            }
        }
    }

    public static Function<Row, Map<String, Object>> resolveValues(Map<String, Object> targetValues) {
        return row -> {
            var map = new HashMap<String, Object>();
            for (var entry : targetValues.entrySet()) {
                var key1 = entry.getKey();
                var val = entry.getValue();
                map.put(key1, val instanceof String ? row.read((String) val) : val);
            }
            return map;
        };
    }
}
