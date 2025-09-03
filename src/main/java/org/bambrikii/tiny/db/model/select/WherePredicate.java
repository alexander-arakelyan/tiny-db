package org.bambrikii.tiny.db.model.select;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;

@ToString
@Setter
@Getter
public class WherePredicate {
    private SelectClause left;
    private ComparisonOpEnum op;
    private SelectClause right;
    private Object rightVal;
    private WherePredicate and;
    private WherePredicate or;

    public static WherePredicate of(SelectClause left, ComparisonOpEnum op, SelectClause right) {
        var predicate = new WherePredicate();
        predicate.setLeft(left);
        predicate.setOp(op);
        predicate.setRight(right);
        return predicate;
    }

    public static WherePredicate of(SelectClause left, ComparisonOpEnum op, Object rightVal) {
        var predicate = new WherePredicate();
        predicate.setLeft(left);
        predicate.setOp(op);
        predicate.setRightVal(rightVal);
        return predicate;
    }
}
