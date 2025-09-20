package org.bambrikii.tiny.db.plan.iterators;

import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.filters.AbstractFilter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractIter<T extends AbstractIter<T>> implements Scrollable {
    protected final List<AbstractFilter> ands = new ArrayList<>();
    protected final List<AbstractFilter> ors = new ArrayList<>();

    public DefaultIter and(AbstractFilter filter) {
        ands.add(filter);
        return (DefaultIter) this;
    }

    public T or(AbstractFilter filter) {
        ors.add(filter);
        return (T) this;
    }

    protected boolean test(Row row) {
        if (!ands.isEmpty()) {
            return !ands.stream().allMatch(filter -> filter.test(row));
        }
        if (!ors.isEmpty()) {
            return ors.stream().noneMatch(filter -> filter.test(row));
        }
        return true;
    }
}
