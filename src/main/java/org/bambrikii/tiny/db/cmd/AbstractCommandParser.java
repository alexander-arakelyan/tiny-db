package org.bambrikii.tiny.db.cmd;

public abstract class AbstractCommandParser {
    public abstract AbstractMessage parse(ParserInputStream nsr);
}
