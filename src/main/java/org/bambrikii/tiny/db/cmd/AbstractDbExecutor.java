package org.bambrikii.tiny.db.cmd;

public interface AbstractDbExecutor<C extends AbstractCommand, T extends AbstractExecutorContext> {
    CommandResult tryExec(AbstractCommand cmd, AbstractExecutorContext ctx);

    CommandResult exec(C cmd, T ctx);
}
