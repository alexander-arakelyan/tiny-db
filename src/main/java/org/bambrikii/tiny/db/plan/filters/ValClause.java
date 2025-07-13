package org.bambrikii.tiny.db.plan.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValClause implements ExecutionFilterClause {
    private Object val;
}
