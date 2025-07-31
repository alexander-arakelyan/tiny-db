package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.algo.PhysicalRow;
import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class AlterTable extends AbstractCommand<AlterTableMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(AlterTableMessage msg, QueryExecutorContext ctx) {

        var tableName = msg.getName();
        var tmpTableName = tableName + UUID.randomUUID();

        // clone struct
        // remove deleted columns
        // add new columns

        var storage = ctx.getStorage();
        var filters = List.<Filter>of();

        var tmpStruct = copyToTmp(msg, tmpTableName, storage, tableName, filters);
        overwriteOrig(storage, tableName, tmpStruct, tmpTableName, filters);

        return OK_COMMAND_RESULT;
    }

    private void overwriteOrig(StorageContext storage, String tableName, TableStruct tmpStruct, String tmpTableName, List<Filter> filters) {
        storage.drop(tableName);
        var targetStruct = createStrct(tmpStruct, tableName);
        insertRows(storage, createJoin(tmpTableName), filters, targetStruct);
        storage.drop(tmpTableName);
    }

    private static TableStruct copyToTmp(AlterTableMessage cmd, String tmpTableName, StorageContext storage, String tableName, List<Filter> filters) {
        var tmpStruct = alterStruct(cmd, tmpTableName, storage.read(tableName));
        storage.write(tmpStruct);
        insertRows(storage, createJoin(tableName), filters, tmpStruct);
        return tmpStruct;
    }

    private TableStruct createStrct(TableStruct tmpStruct, String tableName) {
        var struct = new TableStruct();
        struct.setTable(tableName);
        struct.getColumns().addAll(tmpStruct.getColumns());
        return struct;
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

    private static TableStruct alterStruct(AlterTableMessage cmd, String targetTableName, TableStruct origStruct) {
        var targetStruct = new TableStruct();
        targetStruct.setTable(targetTableName);
        var origCols = origStruct.getColumns();
        var colsToAdd = cmd.getAddColumns();
        var colsToDrop = cmd.getDropColumns();
        var colsToAddNames = toNames(colsToAdd);
        var colsToDropNames = toNames(colsToDrop);

        var targetCols = targetStruct.getColumns();

        origCols.forEach(column -> {
            var name = column.getName();
            if (!colsToAddNames.contains(name) && colsToDropNames.contains(name)) {
                targetCols.add(column);
            }
        });
        colsToAdd.forEach(column -> {
            if (!colsToDropNames.contains(column.getName())) {
                targetCols.add(column);
            }
        });
        return targetStruct;
    }

    private static Set<String> toNames(List<Column> colsToAdd) {
        return colsToAdd
                .stream()
                .map(Column::getName)
                .collect(Collectors.toSet());
    }
}
