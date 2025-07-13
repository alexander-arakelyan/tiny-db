package org.bambrikii.tiny.db.plan.operators;

import org.bambrikii.tiny.db.model.ComparisonOpEnum;

public class OperatorFactory {
    private OperatorFactory() {
    }

    public static ExecutionOperatable from(ComparisonOpEnum op) {
        switch (op) {
            case EQ:
                return new EqOperator();
            case GE:
                return new GeOperator();
            case GT:
                return new GtOperator();
            case LE:
                return new LeOperator();
            case LT:
                return new LtOperator();
            case NE:
                return new NeOperator();
            case IN:
                return new InOperator();
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
