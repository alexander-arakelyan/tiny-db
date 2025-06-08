package org.bambrikii.tiny.db.query;

import lombok.Getter;
import lombok.Setter;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;

@Getter
@Setter
public class QueryExecutorContext extends AbstractExecutorContext {
    private boolean shouldRun = true;

    public boolean shouldRun() {
        return shouldRun;
    }

    public void shutdown() {
        this.shouldRun = false;
    }
}
