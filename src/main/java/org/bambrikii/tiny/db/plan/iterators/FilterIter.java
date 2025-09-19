package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.iterators.join.AbstractFilter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FilterIter implements Scrollable {
    private final Scrollable child;
    private final List<AbstractFilter> filters = new ArrayList<>();

    @Override

    public void open() {
        child.open();
    }

    @Override
    public Row next() {
        Row row;
        if ((row = child.next()) == null) {
            return null;
        }
        if (!filters
                .stream()
                .allMatch(filter -> filter.test(row))
        ) {
            return null;
        }
        return row;
    }

    public FilterIter filter(AbstractFilter filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public void reset() {
        child.reset();
    }

    @Override
    public void close() {
        child.close();
    }
}
