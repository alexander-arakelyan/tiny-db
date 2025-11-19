package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

@RequiredArgsConstructor
public class NestedLoopIter extends AbstractIter<NestedLoopIter> {
    private final String outerAlias;
    private final Scrollable outerScroll;
    private final String innerAlias;
    private final Scrollable innerScroll;
    private final boolean innerJoin;
    private Row o;
    private Row i;

    @Override
    public void open() {
        outerScroll.open();
        innerScroll.open();
    }

    @Override
    public Row next() {
        while (true) {
            if (o == null && (o = outerScroll.next()) == null) {
                return null;
            }
            if ((i = innerScroll.next()) == null) {
                o = null;
                innerScroll.reset();
                if (innerJoin) {
                    continue;
                }
            }
            var row = new LogicalRow();
            row.combine(outerAlias, o, innerAlias, i);
            if (match(row)) {
                return row;
            }
        }
    }

    @Override
    public void reset() {
        outerScroll.reset();
        innerScroll.reset();
    }

    @Override
    public void close() {
        outerScroll.close();
        innerScroll.close();
    }
}
