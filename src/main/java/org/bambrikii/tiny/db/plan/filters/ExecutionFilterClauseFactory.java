package org.bambrikii.tiny.db.plan.filters;

import org.bambrikii.tiny.db.model.select.ColumnRef;

public class ExecutionFilterClauseFactory {
    private ExecutionFilterClauseFactory() {
    }

    public static ExecutionFilterClause build(ColumnRef ref, Object val) {
        if (val != null) {
            return new ValClause(val);
        }
        if (ref.getName() != null) {
            return new RefClause(ref.getAlias(), ref.getName());
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
