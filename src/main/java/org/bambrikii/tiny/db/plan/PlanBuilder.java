package org.bambrikii.tiny.db.plan;

import org.bambrikii.tiny.db.model.predicates.AndPredicate;
import org.bambrikii.tiny.db.model.predicates.FilterByValuePredicate;
import org.bambrikii.tiny.db.model.predicates.JoinPredicate;
import org.bambrikii.tiny.db.model.predicates.OrPredicate;
import org.bambrikii.tiny.db.model.predicates.WherePredicate;
import org.bambrikii.tiny.db.plan.filters.AbstractFilter;
import org.bambrikii.tiny.db.plan.impl.FilterByValue;
import org.bambrikii.tiny.db.plan.impl.JoinIterators;
import org.bambrikii.tiny.db.plan.impl.LogicalOps;
import org.bambrikii.tiny.db.model.Scrollable;

import java.util.Set;
import java.util.function.BiConsumer;

public class PlanBuilder {
    private final PlanContext ctx;
    private final LogicalOps logicalOps;
    private final FilterByValue filterByValue;
    private final JoinIterators joinIterators;

    public PlanBuilder(PlanContext ctx) {
        this.ctx = ctx;
        logicalOps = new LogicalOps(this);
        filterByValue = new FilterByValue(this);
        joinIterators = new JoinIterators(this);
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
            return joinIterators.apply((JoinPredicate) predicate, consumer);
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
