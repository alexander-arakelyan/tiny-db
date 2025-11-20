package org.bambrikii.tiny.db.model.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.clauses.SelectClause;

@ToString
@Setter
@Getter
public class JoinNode implements WhereNode {
    private SelectClause left;
    private ComparisonOpEnum op;
    private SelectClause right;

    public static JoinNode of(SelectClause left, ComparisonOpEnum op, SelectClause right) {
        var predicate = new JoinNode();
        predicate.setLeft(left);
        predicate.setOp(op);
        predicate.setRight(right);
        return predicate;
    }
}
