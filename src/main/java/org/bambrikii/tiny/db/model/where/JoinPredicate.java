package org.bambrikii.tiny.db.model.where;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.select.SelectClause;

@ToString
@Setter
@Getter
public class JoinPredicate implements WherePredicate {
    private SelectClause left;
    private ComparisonOpEnum op;
    private SelectClause right;

    public static JoinPredicate of(SelectClause left, ComparisonOpEnum op, SelectClause right) {
        var predicate = new JoinPredicate();
        predicate.setLeft(left);
        predicate.setOp(op);
        predicate.setRight(right);
        return predicate;
    }
}
