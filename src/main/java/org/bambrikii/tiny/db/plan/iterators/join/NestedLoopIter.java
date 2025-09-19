package org.bambrikii.tiny.db.plan.iterators.join;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.iterators.LogicalRow;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class NestedLoopIter implements Scrollable {
    private final String leftAlias;
    private final Scrollable left;
    private final String rightAlias;
    private final Scrollable right;
    private final List<AbstractFilter> filters = new ArrayList<>();

    @Override
    public void open() {
        left.open();
        right.open();
    }

    public NestedLoopIter filter(AbstractFilter filter) {
        this.filters.add(filter);
        return this;
    }

    protected boolean canPass(LogicalRow row) {
        return filters
                .stream()
                .allMatch(filter -> filter.test(row));
    }

    @Override
    public Row next() {
        Row l;
        if ((l = left.next()) == null) {
            return null;
        }
        Row r;
        if ((r = right.next()) == null) {
            return null;
        }
        // TODO: filter
        var row = new LogicalRow();
        if (!canPass(row)) {
            return null;
        }
        row.combine(leftAlias, l, rightAlias, r);
        return row;
    }

    @Override
    public void reset() {
        left.reset();
        right.reset();
    }

    @Override
    public void close() {
        left.close();
        right.close();
    }
}
