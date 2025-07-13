package org.bambrikii.tiny.db.plan.filters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.Filter;

@Getter
@Setter
@NoArgsConstructor
public class ExecutionFilter {
    private ExecutionFilterClause l;
    private ComparisonOpEnum op;
    private ExecutionFilterClause r;
    private boolean applied = false;
    private boolean passed = true;

    public static ExecutionFilter build(Filter f) {
        var f2 = new ExecutionFilter();

        f2.setL(ExecutionFilterClauseFactory.build(f.getL(), f.getLVal()));
        f2.setOp(f.getOp());
        f2.setL(ExecutionFilterClauseFactory.build(f.getR(), f.getRVal()));

        return f2;
    }

    public boolean canApplyNow(String alias) {
        return !applied;
    }

    public void passed(boolean passed) {
        this.applied = true;
        this.passed = passed;
    }
}
