package org.bambrikii.tiny.db.cmd.selectrows;

import lombok.ToString;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.FilterCommandable;
import org.bambrikii.tiny.db.model.Filter;

import java.util.ArrayList;
import java.util.List;

@ToString
public class SelectRowsMessage implements AbstractMessage, FilterCommandable {
    private final List<String> select = new ArrayList<>();
    private String table;
    private final List<Filter> where = new ArrayList<>();

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
