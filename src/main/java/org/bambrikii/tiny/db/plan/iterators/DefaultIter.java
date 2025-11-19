package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

@RequiredArgsConstructor
public class DefaultIter extends AbstractIter<DefaultIter> {
    private final Scrollable child;

    @Override

    public void open() {
        child.open();
    }

    @Override
    public Row next() {
        while (true) {
            Row row;
            if ((row = child.next()) == null) {
                return null;
            }
            if (match(row)) {
                return row;
            }
        }
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
