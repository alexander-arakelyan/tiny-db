package org.bambrikii.tiny.db.plan.cursorts;

import org.bambrikii.tiny.db.plan.iterators.LogicalRow;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;

public final class EmptyCursor implements Scrollable {
    public static final EmptyCursor EMPTY_CURSOR = new EmptyCursor();

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public LogicalRow next() {
        return null;
    }

    @Override
    public void reset() {

    }
}
