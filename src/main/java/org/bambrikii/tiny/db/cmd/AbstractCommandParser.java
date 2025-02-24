package org.bambrikii.tiny.db.cmd;

public abstract class AbstractCommandParser {
    public abstract AbstractCommand parse(ParserInputStream nsr);
}
