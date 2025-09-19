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
import org.bambrikii.tiny.db.plan.iterators.join.JoinFilter;
import org.bambrikii.tiny.db.plan.iterators.join.JoinIter;
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
        } else if (predicate instanceof OrPredicate) {
            ExecutionPlanFilterFactory.or(scrolls, (OrPredicate) predicate);
        } else if (predicate instanceof FilterByValuePredicate) {
            ExecutionPlanFilterFactory.filter(scrolls, (FilterByValuePredicate) predicate)
        } else if (predicate instanceof JoinPredicate) {
            ExecutionPlanFilterFactory.join(scrolls, (JoinPredicate) predicate);
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    private static void join(Map<String, Scrollable> scrolls, JoinPredicate join) {
        var l = join.getLeft();
        var op = join.getOp();
        var r = join.getRight();

        var aliasLeft = l.getTableAlias();
        var aliasRight = r.getTableAlias();
        var scrollLeft = scrolls.get(aliasLeft);
        var scrollRight = scrolls.get(aliasRight);

        if (!((scrollLeft == scrollRight) && (scrollLeft instanceof JoinIter))) {
            var filter = new JoinIter(aliasLeft, scrollLeft, aliasRight, scrollRight);
            scrolls.put(aliasLeft, filter);
            scrolls.put(aliasRight, filter);
        }
        ((JoinIter) scrolls.get(aliasLeft)).filter(new JoinFilter(l.getTableAlias(), l.getCol(), op, r.getTableAlias(), r.getCol()));

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
