package org.bambrikii.tiny.db.plan.filters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.Row;

import static org.bambrikii.tiny.db.plan.filters.FilterValueComparator.VALUE_COMPARATOR;

@Getter
@RequiredArgsConstructor
public class ValueFilter implements AbstractFilter {
    private final String leftTab;
    private final String leftCol;
    private final ComparisonOpEnum op;
    private final Object val;

    @Override
    public boolean match(Row row) {
        var val1 = row.read(leftTab, leftCol);
        return compare(val1, op, val);
    }

    public static boolean compare(Object val1, ComparisonOpEnum op, Object val) {
        switch (op) {
            case EQ:
                return VALUE_COMPARATOR.compare(val1, val) == 0;
            case GT:
                return VALUE_COMPARATOR.compare(val1, val) > 0;
            case GE:
                return VALUE_COMPARATOR.compare(val1, val) >= 0;
            case LT:
                return VALUE_COMPARATOR.compare(val1, val) < 0;
            case LE:
                return VALUE_COMPARATOR.compare(val1, val) <= 0;
            case NE:
                return VALUE_COMPARATOR.compare(val1, val) != 0;
            default:
                throw new IllegalArgumentException("Operator " + op + " is not supported");
        }
    }
}
