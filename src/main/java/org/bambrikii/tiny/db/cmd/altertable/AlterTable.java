package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.bambrikii.tiny.db.storagelayout.PhysicalRow;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class AlterTable extends AbstractCommand<AlterTableMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(AlterTableMessage cmd, QueryExecutorContext ctx) {
        var origTableName = cmd.getName();

        var targetStruct = new TableStruct();
        var targetTableName = origTableName + UUID.randomUUID();
        targetStruct.setTable(targetTableName);
        targetStruct.getColumns().addAll(cmd.getAddColumns());
        cmd.getDropColumns().forEach(col -> targetStruct
                .getColumns()
                .removeIf(col2 -> Objects.equals(col.getName(), col2.getName()))
        );

        // 1. create new temp table
        // 2. read original table struct
        // 2. insert old data into new table
        // 3. rename new table

        var storage = ctx.getStorage();
        storage.write(origTableName, targetStruct);
        var tables = List.of(new Join(origTableName, null, origTableName));
        var filters = List.<Filter>of();
        var targetValues = new HashMap<String, Object>();
        // TODO: map to columns

//        InsertRows.insert(storage, tables, filters, targetTableName, InsertRows.resolveValues(targetValues));
        InsertRows.insert(storage, tables, filters, targetTableName, row -> {
            var vals = new HashMap<String, Object>();
            ((PhysicalRow) row)
                    .keys()
                    .forEach(col -> vals.put(col, row.read(col)));
            return vals;
        });

        return OK_COMMAND_RESULT;
    }
}
