package org.bambrikii.tiny.db.plan.filters;

import org.bambrikii.tiny.db.model.select.SelectClause;

public class ExecutionFilterClauseFactory {
    private ExecutionFilterClauseFactory() {
    }

    public static ExecutionFilterClause build(SelectClause ref, Object val) {
        if (val != null) {
            return new ValClause(val);
        }
        if (ref.getCol() != null) {
            return new RefClause(ref.getTableAlias(), ref.getCol());
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
