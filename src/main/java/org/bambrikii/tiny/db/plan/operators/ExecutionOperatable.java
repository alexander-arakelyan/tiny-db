package org.bambrikii.tiny.db.plan.operators;

import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilterClause;

public interface ExecutionOperatable {
    boolean test(
            ExecutionFilterClause l,
            ExecutionFilterClause r,
            Row row
    );
}
