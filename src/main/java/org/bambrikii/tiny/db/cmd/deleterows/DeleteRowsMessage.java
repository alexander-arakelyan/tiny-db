package org.bambrikii.tiny.db.cmd.deleterows;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.FilterCommandable;
import org.bambrikii.tiny.db.model.Filter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DeleteRowsMessage implements AbstractMessage, FilterCommandable {
    private String name;
    private final List<Filter> filters = new ArrayList<>();

    public void name(String name) {
        this.name = name;
    }

    @Override
    public void filter(Filter filter) {
        filters.add(filter);
    }
}
