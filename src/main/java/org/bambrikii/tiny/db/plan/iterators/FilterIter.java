package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.Row;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FilterIter implements Scrollable {
    private final Scrollable child;
    private final List<Filter> filters = new ArrayList<>();

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
        // TODO: apply filter
        return row;
    }

    @Override
    public void reset() {
        child.reset();
    }

    @Override
    public void close() {
        child.close();
    }

    public void filter(String col, ComparisonOpEnum op, Object val) {
        filters.add(new Filter(col, op, val));
    }

    // TODO: filter chain with AND, OR, brackets required
    // TODO: otherwise will assume AND by default
    @RequiredArgsConstructor
    static class Filter {
        private final String col;
        private final ComparisonOpEnum op;
        private final Object val;
    }
}
