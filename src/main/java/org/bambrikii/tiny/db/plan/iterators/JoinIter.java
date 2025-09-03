package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.Row;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JoinIter implements Scrollable {
    private final String leftAlias;
    private final Scrollable left;
    private final String rightAlias;
    private final Scrollable right;
    private final List<JoinFilter> filters = new ArrayList<>();

    @Override
    public void open() {
        left.open();
        right.open();
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
        // TODO: combine
        var row = new LogicalRow();
        row.combine(leftAlias, l);
        row.combine(rightAlias, r);
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

    public void filter(String left, ComparisonOpEnum op, String right) {
        filters.add(new JoinFilter(left, op, right));
    }

    @RequiredArgsConstructor
    private static class JoinFilter {
        private final String leftCol;
        private final ComparisonOpEnum op;
        private final String rightCol;
    }
}
