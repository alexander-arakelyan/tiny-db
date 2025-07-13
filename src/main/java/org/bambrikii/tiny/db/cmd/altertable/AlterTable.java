package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.createtable.CreateTable;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import java.util.Objects;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class AlterTable extends AbstractCommand<AlterTableMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(AlterTableMessage cmd, QueryExecutorContext ctx) {
        var key = cmd.getName();

        var struct = new TableStruct();
        struct.setTable(key);
        struct.getColumns().addAll(cmd.getAddColumns());
        cmd.getDropColumns()
                .forEach(col -> struct.getColumns()
                        .removeIf(col2 -> Objects.equals(col.getName(), col2.getName()))
                );

        ctx.getStorage().write(key, CreateTable.toDisk(struct), CreateTable.toMem(struct));

        return OK_COMMAND_RESULT;
    }
}
