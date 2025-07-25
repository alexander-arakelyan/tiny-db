package org.bambrikii.tiny.db.plan.operators;

import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilterClause;
import org.bambrikii.tiny.db.plan.filters.RefClause;
import org.bambrikii.tiny.db.plan.filters.ValClause;

import java.util.Objects;

public class EqOperator implements ExecutionOperatable {
    @Override
    public boolean test(ExecutionFilterClause l, ExecutionFilterClause r, Row row) {
        var lVal = readVal(l, row);
        var rVal = readVal(r, row);
        return Objects.equals(lVal, rVal);
    }

    private static Object readVal(ExecutionFilterClause l, Row row) {
        if (l instanceof RefClause) {
            var lR = (RefClause) l;
            return row.read(lR.getAlias() + "." + lR.getCol());
        }
        if (l instanceof ValClause) {
            var lV = (ValClause) l;
            return lV.getVal();
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
