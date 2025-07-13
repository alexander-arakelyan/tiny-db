package org.bambrikii.tiny.db.storage.tables;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.storage.disk.FileIO;

@RequiredArgsConstructor
public class RelTableIO {
    private final FileIO io;
    private final String name;

    public void append(Object[] vals) {

    }

    public void read() {

    }

    public void update(String rowId) {

    }

}
