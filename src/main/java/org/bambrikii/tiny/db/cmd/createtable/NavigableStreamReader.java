package org.bambrikii.tiny.db.cmd.createtable;

import java.io.InputStream;
import java.io.InputStreamReader;

public class NavigableStreamReader extends InputStreamReader {
    public NavigableStreamReader(InputStream in) {
        super(in);
    }

    public long mark() {
        return 0;
    }

    public void rollback(long mark) {

    }

    public String readWord() {
        return null;
    }

    public String readType() {
        return null;
    }

    public boolean readString(String s) {
        return false;
    }

    public boolean readWord(String w) {
        return false;
    }

    public Boolean readBoolean(boolean b) {
        return b;
    }
}
