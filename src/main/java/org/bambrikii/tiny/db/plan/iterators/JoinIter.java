package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

@RequiredArgsConstructor
public class JoinIter implements Scrollable {
    private final String leftAlias;
    private final Scrollable leftIter;
    private final String rightAlias;
    private final Scrollable rightIter;

    @Override
    public void open() {
        leftIter.open();
        rightIter.open();
    }

    @Override
    public Row next() {
        Row l;
        if ((l = leftIter.next()) == null) {
            return null;
        }
        Row r;
        if ((r = rightIter.next()) == null) {
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
        leftIter.reset();
        rightIter.reset();
    }

    @Override
    public void close() {
        leftIter.close();
        rightIter.close();
    }
}
