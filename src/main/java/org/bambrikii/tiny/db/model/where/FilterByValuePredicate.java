package org.bambrikii.tiny.db.model.where;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.select.SelectClause;

@ToString
@Setter
@Getter
public class FilterByValuePredicate implements WherePredicate {
    private SelectClause left;
    private ComparisonOpEnum op;
    private Object rightVal;

    public static FilterByValuePredicate of(SelectClause left, ComparisonOpEnum op, Object rightVal) {
        var predicate = new FilterByValuePredicate();
        predicate.setLeft(left);
        predicate.setOp(op);
        predicate.setRightVal(rightVal);
        return predicate;
    }
}
