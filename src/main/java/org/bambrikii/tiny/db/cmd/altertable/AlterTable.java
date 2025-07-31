package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.bambrikii.tiny.db.storage.StorageContext;
import org.bambrikii.tiny.db.algo.PhysicalRow;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class AlterTable extends AbstractCommand<AlterTableMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(AlterTableMessage cmd, QueryExecutorContext ctx) {

        var tableName = cmd.getName();
        var tmpTableName = tableName + UUID.randomUUID();
        var tmpStruct = createStruct(cmd, tmpTableName);

        var storage = ctx.getStorage();
        var struct = storage.read(tableName);
        // clone struct
        // remove deleted columns
        // add new columns
        storage.write(tmpStruct);
        var filters = List.<Filter>of();

        insertRows(storage, createJoin(tableName), filters, tmpStruct);
        storage.drop(tableName);

        var targetStruct = createStruct(cmd, tableName);
        insertRows(storage, createJoin(tmpTableName), filters, tmpStruct);
        storage.drop(tmpTableName);

        return OK_COMMAND_RESULT;
    }

    private static List<Join> createJoin(String tableName) {
        return List.of(new Join(tableName, null, tableName));
    }

    private static void insertRows(StorageContext storage, List<Join> tables, List<Filter> filters, TableStruct struct) {
        InsertRows.insertScrollable(storage, tables, filters, struct.getTable(), row -> {
            var vals = new HashMap<String, Object>();
            ((PhysicalRow) row)
                    .keys()
                    .forEach(col -> vals.put(col, row.read(col)));
            return vals;
        });
    }

    private static TableStruct createStruct(AlterTableMessage cmd, String targetTableName) {
        var targetStruct = new TableStruct();
        targetStruct.setTable(targetTableName);
        targetStruct.getColumns().addAll(cmd.getAddColumns());
        cmd.getDropColumns().forEach(col -> targetStruct
                .getColumns()
                .removeIf(col2 -> Objects.equals(col.getName(), col2.getName()))
        );
        return targetStruct;
    }
}
