package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

@RequiredArgsConstructor
public class NestedLoopIter extends AbstractIter<NestedLoopIter> {
    private final String leftAlias;
    private final Scrollable left;
    private final String rightAlias;
    private final Scrollable right;

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
        var row = new LogicalRow();
        row.combine(leftAlias, l, rightAlias, r);
        if (!test(row)) {
            return null;
        }
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
