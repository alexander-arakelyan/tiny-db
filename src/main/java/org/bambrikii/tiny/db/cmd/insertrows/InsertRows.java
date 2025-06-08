package org.bambrikii.tiny.db.cmd.insertrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import java.util.HashMap;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class InsertRows extends AbstractCommand<InsertRowsMessage, QueryExecutorContext> {
    @Override
    public CommandResult exec(InsertRowsMessage cmd, QueryExecutorContext ctx) {
        var key = cmd.getName();

        var names = cmd.getColNames();
        var vals = cmd.getColVals();
        var values = new HashMap<String, Object>();
        for (int i = 0; i < names.size(); i++) {
            var name = names.get(i);
            var val = vals.get(i);
            values.put(name, val);
        }

        ctx.getStorage().append(key, values);

        return OK_COMMAND_RESULT;
    }
}
