package org.bambrikii.tiny.db.cmd.droptable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;

public class DropTableCommand implements AbstractCommand {
    private String name;

    public DropTableCommand name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        var exec = new DropTableExecutor();
        return exec.exec(this, ctx);
    }
}
