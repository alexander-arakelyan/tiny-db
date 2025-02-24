package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.Filter1;

import java.util.ArrayList;
import java.util.List;

public class DeleteRowsCommand implements AbstractCommand {
    private String name;
    private final List<Filter1> filters = new ArrayList<>();

    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        return null;
    }

    public void name(String name) {
        this.name = name;
    }

    public void filter(String left, String op, String right) {
        filters.add(new Filter1(left, op, right));
    }
}
