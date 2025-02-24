package org.bambrikii.tiny.db.cmd;

public interface ParserInputStream {
    long pos();

    void commit(long mark);

    void rollback(long mark);

    byte advance();

    String string(long start, long end);
}
