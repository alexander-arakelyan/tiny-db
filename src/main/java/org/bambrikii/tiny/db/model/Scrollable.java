package org.bambrikii.tiny.db.model;

public interface Scrollable extends AutoCloseable {
    void open();

    Row next();

    void reset();

    @Override
    void close();
}
