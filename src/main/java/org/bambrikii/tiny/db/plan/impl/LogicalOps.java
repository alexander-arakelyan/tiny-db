package org.bambrikii.tiny.db.plan.impl;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.predicates.WherePredicate;
import org.bambrikii.tiny.db.plan.PlanBuilder;
import org.bambrikii.tiny.db.plan.filters.AbstractFilter;
import org.bambrikii.tiny.db.plan.iterators.AbstractIter;
import org.bambrikii.tiny.db.plan.iterators.NestedLoopIter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class LogicalOps implements AbstractFilterStrategy<List<WherePredicate>> {
    private final PlanBuilder factory;

    public void orFilter(String alias, AbstractFilter filter) {
        ((AbstractIter<?>) factory.find(alias)).or(filter);
    }

    public void andFilter(String alias, AbstractFilter filter) {
        ((AbstractIter<?>) factory.find(alias)).and(filter);
    }

    @Override
    public Set<String> apply(List<WherePredicate> predicates, BiConsumer<String, AbstractFilter> filterBiConsumer) {
        var res = new HashSet<String>();
        String left = null;
        for (var pred : predicates) {
            var aliases = factory.build(pred, filterBiConsumer);
            for (var right : aliases) {
                if (left == null) {
                    left = right;
                    res.add(right);
                } else if (!res.contains(right)) {
                    var l = factory.find(left);
                    var r = factory.find(right);
                    if (!Objects.equals(l, r)) {
                        factory.merge(left, right, new NestedLoopIter(left, l, right, r, true));
                    }
                    res.add(right);
                } else {
                    // already merged
                }
            }
        }
        return res;
    }
}
