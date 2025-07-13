package org.bambrikii.tiny.db.storage.tables;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.storage.disk.FileIO;

@RequiredArgsConstructor
public class LogTableIO {
    private final FileIO io;
    private final String name;

    public void append(Object v) {

    }

    public void scan(int start) {

    }
}
