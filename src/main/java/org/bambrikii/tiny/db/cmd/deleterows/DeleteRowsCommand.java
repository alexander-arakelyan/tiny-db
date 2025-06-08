package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.FilterCommandable;
import org.bambrikii.tiny.db.model.Filter;

import java.util.ArrayList;
import java.util.List;

public class DeleteRowsCommand implements AbstractCommand, FilterCommandable {
    private String name;
    private final List<Filter> filters = new ArrayList<>();

    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        return null;
    }

    public void name(String name) {
        this.name = name;
    }

    @Override
    public void filter(Filter filter) {
        filters.add(filter);
    }
}
