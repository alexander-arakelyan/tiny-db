package org.bambrikii.tiny.db.model.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.clauses.SelectClause;

@ToString
@Setter
@Getter
public class FilterByValueNode implements WhereNode {
    private SelectClause left;
    private ComparisonOpEnum op;
    private Object rightVal;

    public static FilterByValueNode of(SelectClause left, ComparisonOpEnum op, Object rightVal) {
        var predicate = new FilterByValueNode();
        predicate.setLeft(left);
        predicate.setOp(op);
        predicate.setRightVal(rightVal);
        return predicate;
    }
}
