package org.bambrikii.tiny.db.plan;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bambrikii.tiny.db.model.where.AndPredicate;
import org.bambrikii.tiny.db.model.where.FilterByValuePredicate;
import org.bambrikii.tiny.db.model.where.JoinPredicate;
import org.bambrikii.tiny.db.model.where.OrPredicate;
import org.bambrikii.tiny.db.model.where.WherePredicate;
import org.bambrikii.tiny.db.plan.iterators.FilterIter;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.plan.iterators.join.InnerJoinFilter;
import org.bambrikii.tiny.db.plan.iterators.join.NestedLoopIter;
import org.bambrikii.tiny.db.plan.iterators.join.ValueFilter;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutionPlanFilterFactory {

    public static void create(
            Map<String, Scrollable> scrolls,
            WherePredicate predicate
    ) {
        if (predicate instanceof AndPredicate) {
            ExecutionPlanFilterFactory.and(scrolls, (AndPredicate) predicate);
            return;
        }
        if (predicate instanceof OrPredicate) {
            ExecutionPlanFilterFactory.or(scrolls, (OrPredicate) predicate);
            return;
        }
        if (predicate instanceof FilterByValuePredicate) {
            ExecutionPlanFilterFactory.filter(scrolls, (FilterByValuePredicate) predicate);
            return;
        }
        if (predicate instanceof JoinPredicate) {
            ExecutionPlanFilterFactory.join(scrolls, (JoinPredicate) predicate);
            return;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void join(Map<String, Scrollable> scrolls, JoinPredicate join) {
        var l = join.getLeft();
        var op = join.getOp();
        var r = join.getRight();

        var aliasLeft = l.getTableAlias();
        var aliasRight = r.getTableAlias();
        var scrollLeft = scrolls.get(aliasLeft);
        var scrollRight = scrolls.get(aliasRight);

        if (!(scrollLeft == scrollRight) || !(scrollLeft instanceof NestedLoopIter)) {
            var filter = new NestedLoopIter(aliasLeft, scrollLeft, aliasRight, scrollRight);
            scrolls.put(aliasLeft, filter);
            scrolls.put(aliasRight, filter);
        }
        ((NestedLoopIter) scrolls.get(aliasLeft)).filter(new InnerJoinFilter(l.getTableAlias(), l.getCol(), op, r.getTableAlias(), r.getCol()));

    }

    private static void filter(Map<String, Scrollable> scrolls, FilterByValuePredicate filter) {
        var l = filter.getLeft();
        var op = filter.getOp();
        var val = filter.getRightVal();

        var alias = l.getTableAlias();
        var scroll = scrolls.get(alias);
        if (!(scroll instanceof FilterIter)) {
            scrolls.put(alias, new FilterIter(scroll));
        }
        ((FilterIter) scrolls.get(alias)).filter(new ValueFilter(alias, l.getCol(), op, val));
    }

    private static void and(Map<String, Scrollable> scrolls, AndPredicate predicate) {
        // 1. recursively create child filters first
        // 2. left part
        // 3. right part
        // 4. find all child aliases
        // 5. find scrollable by aliases
        // 6. nested loop with and
        predicate
                .getAnds()
                .forEach(wherePredicate -> create(scrolls, wherePredicate));
    }

    private static void or(Map<String, Scrollable> scrolls, OrPredicate predicate) {
        predicate
                .getOrs()
                .forEach(wherePredicate -> create(scrolls, wherePredicate));
    }
}
