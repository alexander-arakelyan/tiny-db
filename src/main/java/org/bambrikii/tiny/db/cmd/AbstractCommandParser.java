package org.bambrikii.tiny.db.cmd;

public abstract class AbstractCommandParser<T extends AbstractMessage> {
    public abstract AbstractMessage parse(ParserInputStream nsr);
}
