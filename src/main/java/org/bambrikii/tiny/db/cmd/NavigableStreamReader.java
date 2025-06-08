package org.bambrikii.tiny.db.cmd;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.InputStreamReader;

public class NavigableStreamReader extends InputStreamReader implements ParserInputStream {
    private byte[] buf;
    private int pos = 0;
    private int reads = 0;

    public NavigableStreamReader(InputStream in) {
        super(in);
        buf = new byte[0];
    }

    @Override
    public int pos() {
        return pos;
    }

    @Override
    public void rollback(int mark) {
        ensureBuffer(mark + 1);
        pos = mark;
    }

    @SneakyThrows
    @Override
    public byte val() {
        if (pos + 1 > reads) {
            read0(pos);
        }
        return buf[pos];
    }

    @SneakyThrows
    @Override
    public void next() {
        pos++;
        read0(pos);
    }

    @SneakyThrows
    private void read0(int size) {
        var b = read();
        reads++;
        ensureBuffer(reads);
        buf[reads - 1] = (byte) b;
    }

    private void ensureBuffer(int size) {
        if (size < buf.length) {
            return;
        }
        var tmp = new byte[size * 2];
        System.arraycopy(buf, 0, tmp, 0, buf.length);
        buf = tmp;
    }

    @Override
    public byte[] bytes(int start, int end) {
        var res = new byte[end - start];
        System.arraycopy(buf, start, res, 0, end - start);
        return res;
    }

    @Override
    public String toString() {
        return String.format("%s[%s, %d, %d]", this.getClass().getSimpleName(), new String(buf), pos, reads);
    }
}
