package org.bambrikii.tiny.db.algo.logio;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.io.disk.DiskIO;

@RequiredArgsConstructor
public class LogTableIO {
    private final DiskIO io;
    private final String name;

    public void append(Object v) {

    }

    public void scan(int start) {

    }
}
