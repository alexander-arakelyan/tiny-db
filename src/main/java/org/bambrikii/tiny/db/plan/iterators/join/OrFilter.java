package org.bambrikii.tiny.db.plan.iterators.join;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrFilter implements AbstractFilter {
    private final List<AbstractFilter> ors = new ArrayList<>();

    public OrFilter or(AbstractFilter filter) {
        ors.add(filter);
        return this;
    }

    @Override
    public boolean test(Row row) {
        return ors
                .stream()
                .anyMatch(filter -> filter.test(row));
    }
}
