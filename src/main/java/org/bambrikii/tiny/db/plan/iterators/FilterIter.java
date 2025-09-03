package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

@RequiredArgsConstructor
public class FilterIter implements Scrollable {
    private final Scrollable child;

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
}
