package org.bambrikii.tiny.db.cmd;

public interface ParserInputStream extends AutoCloseable {
    int pos();

    void rollback(int mark);

    byte val();

    void next();

    byte[] bytes(int start, int end);
}
