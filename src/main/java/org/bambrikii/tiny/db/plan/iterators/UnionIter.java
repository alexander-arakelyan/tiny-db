package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

@RequiredArgsConstructor
public class UnionIter implements Scrollable {
    private final Scrollable[] children;

    @Override
    public void open() {

    }

    @Override
    public Row next() {
        for (var it : children) {
            Row row;
            if ((row = it.next()) != null) {
                return row;
            }
        }
        return null;
    }

    @Override
    public void reset() {
        for (var it : children) {
            it.reset();
        }
    }

    @Override
    public void close() {
        for (var it : children) {
            it.close();
        }
    }
}
