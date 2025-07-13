package org.bambrikii.tiny.db.plan.filters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefClause implements ExecutionFilterClause {
    private final String alias;
    private final String col;
}
