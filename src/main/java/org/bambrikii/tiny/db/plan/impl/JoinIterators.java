package org.bambrikii.tiny.db.plan.impl;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.where.JoinPredicate;
import org.bambrikii.tiny.db.plan.PlanBuilder;
import org.bambrikii.tiny.db.plan.filters.AbstractFilter;
import org.bambrikii.tiny.db.plan.filters.InnerJoinFilter;
import org.bambrikii.tiny.db.plan.iterators.NestedLoopIter;

import java.util.Set;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class JoinIterators implements AbstractFilterStrategy<JoinPredicate> {
    private final PlanBuilder factory;

    @Override
    public Set<String> apply(JoinPredicate join, BiConsumer<String, AbstractFilter> filterBiConsumer) {
        var l = join.getLeft();
        var op = join.getOp();
        var r = join.getRight();

        var aliasLeft = l.getTableAlias();
        var aliasRight = r.getTableAlias();
        var scrollLeft = factory.find(aliasLeft);
        var scrollRight = factory.find(aliasRight);

        if (!(scrollLeft == scrollRight) || !(scrollLeft instanceof NestedLoopIter)) {
            var filter = new NestedLoopIter(aliasLeft, scrollLeft, aliasRight, scrollRight, true);
            factory.merge(aliasLeft, aliasRight, filter);
        }
        filterBiConsumer.accept(aliasLeft, new InnerJoinFilter(l.getTableAlias(), l.getCol(), op, r.getTableAlias(), r.getCol()));
        return Set.of(aliasLeft, aliasRight);
    }
}
