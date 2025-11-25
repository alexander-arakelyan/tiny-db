package org.bambrikii.tiny.db.plan.impl;

import org.bambrikii.tiny.db.model.nodes.FilterByValueNode;
import org.bambrikii.tiny.db.plan.PlanBuilder;
import org.bambrikii.tiny.db.plan.filters.AbstractFilter;
import org.bambrikii.tiny.db.plan.filters.ValueFilter;
import org.bambrikii.tiny.db.plan.iterators.DefaultIter;

import java.util.Set;
import java.util.function.BiConsumer;

public class FilterByValue implements AbstractFilterStrategy<FilterByValueNode> {
    private final PlanBuilder factory;

    public FilterByValue(PlanBuilder factory) {
        this.factory = factory;
    }

    @Override
    public Set<String> apply(FilterByValueNode filter, BiConsumer<String, AbstractFilter> filterBiConsumer) {
        var l = filter.getLeft();
        var op = filter.getOp();
        var val = filter.getRightVal();

        var alias = l.getTableAlias();
        var scroll = factory.find(alias);
        if (!(scroll instanceof DefaultIter)) {
            factory.merge(alias, new DefaultIter(scroll));
        }
        filterBiConsumer.accept(alias, new ValueFilter(alias, l.getCol(), op, val));
        return Set.of(alias);
    }
}
