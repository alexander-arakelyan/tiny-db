package org.bambrikii.tiny.db.cmd;

public abstract class AbstractCommand<
        C extends AbstractMessage,
        T extends AbstractExecutorContext
        > {
    public abstract CommandResult exec(C cmd, T ctx);
}
