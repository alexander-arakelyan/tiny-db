package org.bambrikii.tiny.db.cmd;

import java.io.InputStream;
import java.io.InputStreamReader;

public class NavigableStreamReader extends InputStreamReader implements ParserInputStream {
    public NavigableStreamReader(InputStream in) {
        super(in);
    }

    @Override
    public long pos() {
        return 0;
    }

    @Override
    public void commit(long mark) {

    }

    @Override
    public void rollback(long mark) {

    }
}
