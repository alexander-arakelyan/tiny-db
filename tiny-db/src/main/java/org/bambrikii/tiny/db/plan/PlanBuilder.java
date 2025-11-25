package org.bambrikii.tiny.db.plan;

import org.bambrikii.tiny.db.model.nodes.AndNode;
import org.bambrikii.tiny.db.model.nodes.FilterByValueNode;
import org.bambrikii.tiny.db.model.nodes.JoinNode;
import org.bambrikii.tiny.db.model.nodes.OrNode;
import org.bambrikii.tiny.db.model.nodes.WhereNode;
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

    public Set<String> build(WhereNode predicate) {
        return build(predicate, logicalOps::andFilter);
    }

    public Set<String> build(WhereNode predicate, BiConsumer<String, AbstractFilter> consumer) {
        if (predicate instanceof AndNode) {
            return logicalOps.apply(((AndNode) predicate).getAnds(), logicalOps::andFilter);
        }
        if (predicate instanceof OrNode) {
            return logicalOps.apply(((OrNode) predicate).getOrs(), logicalOps::orFilter);
        }
        if (predicate instanceof FilterByValueNode) {
            return filterByValue.apply((FilterByValueNode) predicate, consumer);
        }
        if (predicate instanceof JoinNode) {
            return joinIterators.apply((JoinNode) predicate, consumer);
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
