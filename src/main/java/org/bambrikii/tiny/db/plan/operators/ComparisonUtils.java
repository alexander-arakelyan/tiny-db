package org.bambrikii.tiny.db.plan.operators;

import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;

public class ComparisonUtils {
    private ComparisonUtils() {
    }

    public static boolean matches(Row row, ExecutionFilter f) {
        var left = f.getL();
        var op = f.getOp();
        var right = f.getR();

        var opImpl = OperatorFactory.from(op);

        return opImpl.test(left, right, row);
    }

}
