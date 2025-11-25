package org.bambrikii.tiny.db.plan.filters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AndFilter implements AbstractFilter {
    private final List<AbstractFilter> ands = new ArrayList<>();

    public AndFilter or(AbstractFilter filter) {
        ands.add(filter);
        return this;
    }

    @Override
    public boolean match(Row row) {
        return ands
                .stream()
                .allMatch(filter -> filter.match(row));
    }
}
