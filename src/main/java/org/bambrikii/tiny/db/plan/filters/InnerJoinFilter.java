package org.bambrikii.tiny.db.plan.filters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.Row;

@Getter
@RequiredArgsConstructor
public class InnerJoinFilter implements AbstractFilter {
    private final String leftTab;
    private final String leftCol;
    private final ComparisonOpEnum op;
    private final String rightTab;
    private final String rightCol;

    @Override
    public boolean test(Row row) {
        var leftVal = row.read(leftTab, leftCol);
        var rightVal = row.read(rightTab, rightCol);
        return ValueFilter.compare(leftVal, op, rightVal);
    }
}
