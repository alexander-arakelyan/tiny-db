package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

@RequiredArgsConstructor
public class GroupByIter implements Scrollable {
    private final Scrollable child;

    @Override
    public void open() {

    }

    @Override
    public Row next() {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void close() {

    }
}
