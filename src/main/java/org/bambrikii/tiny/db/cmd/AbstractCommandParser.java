package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.cmd.createtable.NavigableStreamReader;

public abstract class AbstractCommandParser {
    public abstract AbstractCommand parse(NavigableStreamReader nsr);
}
