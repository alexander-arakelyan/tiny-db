package org.bambrikii.tiny.db.plan.iterators;

import org.bambrikii.tiny.db.model.Row;

public interface Scrollable extends AutoCloseable {
    void open();

    Row next();

    void reset();

    @Override
    void close();
}
