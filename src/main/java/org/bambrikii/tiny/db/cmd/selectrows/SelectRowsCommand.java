package org.bambrikii.tiny.db.cmd.selectrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.FilterCommandable;
import org.bambrikii.tiny.db.model.Filter;

import java.util.ArrayList;
import java.util.List;

public class SelectRowsCommand implements AbstractCommand, FilterCommandable {
    private final List<String> select = new ArrayList<>();
    private String table;
    private final List<Filter> where = new ArrayList<>();

    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        return null;
    }

    public void select(String col) {
        select.add(col);
    }

    public void table(String table) {
        this.table = table;
    }

    @Override
    public void filter(Filter filter) {
        where.add(filter);
    }
}
