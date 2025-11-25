package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.TableStruct;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class CreateTable extends AbstractCommand<CreateTableMessage, AbstractExecutorContext> {
    @Override
    public CommandResult exec(CreateTableMessage cmd, AbstractExecutorContext ctx) {
        var struct = new TableStruct();
        struct.setTable(cmd.getName());
        struct.getColumns().addAll(cmd.getColumns());

        ctx.getStorage().write(struct);

        return OK_COMMAND_RESULT;
    }
}
