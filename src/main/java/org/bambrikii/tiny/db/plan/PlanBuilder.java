package org.bambrikii.tiny.db.plan;

import org.bambrikii.tiny.db.model.where.AndPredicate;
import org.bambrikii.tiny.db.model.where.FilterByValuePredicate;
import org.bambrikii.tiny.db.model.where.JoinPredicate;
import org.bambrikii.tiny.db.model.where.OrPredicate;
import org.bambrikii.tiny.db.model.where.WherePredicate;
import org.bambrikii.tiny.db.plan.filters.AbstractFilter;
import org.bambrikii.tiny.db.plan.impl.FilterByValue;
import org.bambrikii.tiny.db.plan.impl.JoinTwoIterators;
import org.bambrikii.tiny.db.plan.impl.LogicalOps;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;

import java.util.Set;
import java.util.function.BiConsumer;

public class PlanBuilder {
    private final PlanContext ctx;
    private final LogicalOps logicalOps;
    private final FilterByValue filterByValue;
    private final JoinTwoIterators joinTwoIterators;

    public PlanBuilder(PlanContext ctx) {
        this.ctx = ctx;
        logicalOps = new LogicalOps(this);
        filterByValue = new FilterByValue(this);
        joinTwoIterators = new JoinTwoIterators(this);
    }

    public Set<String> build(WherePredicate predicate) {
        return build(predicate, logicalOps::andFilter);
    }

    public Set<String> build(WherePredicate predicate, BiConsumer<String, AbstractFilter> consumer) {
        if (predicate instanceof AndPredicate) {
            return logicalOps.apply(((AndPredicate) predicate).getAnds(), logicalOps::andFilter);
        }
        if (predicate instanceof OrPredicate) {
            return logicalOps.apply(((OrPredicate) predicate).getOrs(), logicalOps::orFilter);
        }
        if (predicate instanceof FilterByValuePredicate) {
            return filterByValue.apply((FilterByValuePredicate) predicate, consumer);
        }
        if (predicate instanceof JoinPredicate) {
            return joinTwoIterators.apply((JoinPredicate) predicate, consumer);
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Scrollable find(String alis) {
        return ctx.find(alis);
    }

    public void merge(String alias, Scrollable scrollable) {
        ctx.merge(alias, scrollable);
    }

    public void merge(String alias, String right, Scrollable scrollable) {
        ctx.merge(alias, right, scrollable);
    }
}
