package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.query.QueryExecutorContext;

public interface AbstractCommand {
    CommandResult exec(AbstractExecutorContext ctx);
}
