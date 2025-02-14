package org.bambrikii.tiny.db.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryExecutorContext implements AbstractExecutorContext {
    private boolean shouldRun = true;

    public boolean shouldRun() {
        return shouldRun;
    }

    public void shutdown() {
        this.shouldRun = false;
    }
}
