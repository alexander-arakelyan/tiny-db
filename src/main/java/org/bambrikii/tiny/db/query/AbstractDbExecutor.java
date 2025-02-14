package org.bambrikii.tiny.db.query;

public interface AbstractDbExecutor<C extends AbstractDbCommand, T extends AbstractExecutorContext> {
    ExecutionResult tryExec(AbstractDbCommand cmd, AbstractExecutorContext ctx);

    ExecutionResult exec(C cmd, T ctx);
}
