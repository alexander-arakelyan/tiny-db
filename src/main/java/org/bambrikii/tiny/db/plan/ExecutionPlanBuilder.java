package org.bambrikii.tiny.db.plan;

import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.JoinTypeEnumComparator;
import org.bambrikii.tiny.db.model.select.FromClause;
import org.bambrikii.tiny.db.model.select.SelectClause;
import org.bambrikii.tiny.db.model.select.WhereClause;
import org.bambrikii.tiny.db.plan.cursorts.DefaultCursor;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;
import org.bambrikii.tiny.db.plan.iterators.FilterIter;
import org.bambrikii.tiny.db.plan.iterators.JoinIter;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExecutionPlanBuilder {

    public static final JoinTypeEnumComparator JOIN_TYPE_ENUM_COMPARATOR = new JoinTypeEnumComparator();

    private final StorageContext ctx;

    public ExecutionPlanBuilder(StorageContext ctx) {
        this.ctx = ctx;
    }

    public Scrollable execute(
            List<FromClause> from,
            List<WhereClause> where
    ) {
        // topo sort filters
        var scroll = scroll(from, where);
        var filtersByAlias = groupFiltersByAlias(where);
        return new DefaultCursor(ctx, scroll, filtersByAlias);
    }

    private Map<String, List<ExecutionFilter>> groupFiltersByAlias(List<WhereClause> filters) {
        var map = new HashMap<String, List<ExecutionFilter>>();
        for (var f : filters) {
            addFilter(map, f.getL(), f);
            addFilter(map, f.getR(), f);
        }
        return map;
    }

    private static void addFilter(HashMap<String, List<ExecutionFilter>> map, SelectClause col, WhereClause f) {
        map.compute(col.getTableAlias(), (s, fs) -> {
            if (fs == null) {
                fs = new ArrayList<>();
            }
//            fs.add(ExecutionFilter.fromFilter(f));
            return fs;
        });
    }

    private Scrollable scroll(
            List<FromClause> from,
            List<WhereClause> where
    ) {
        var scrolls = new HashMap<String, Scrollable>();
        for (var clause : from) {
            var alias = clause.getAlias();
            scrolls.put(alias, ctx.scan(clause.getTable()));
        }
        //
        for (var clause : where) {
            var l = clause.getL();
            var r = clause.getR();
            var lVal = clause.getLVal();
            var rVal = clause.getRVal();
            var op = clause.getOp();
            if (lVal != null) {
                filter(scrolls, r, op, l, lVal);
            } else if (rVal != null) {
                filter(scrolls, l, op, r, rVal);
            } else if (l != null && r != null) {
                join(scrolls, l, op, r);
            }
        }
        var list = new ArrayList<>(new HashSet<>(scrolls.values()));
        while (list.size() > 1) {
            var elem0 = list.remove(0);
            var elem1 = list.remove(0);
            list.add(new JoinIter(null, elem0, null, elem1));
        }
        return list.get(0);
    }

    private static void join(
            HashMap<String, Scrollable> scrolls,
            SelectClause l,
            ComparisonOpEnum op,
            SelectClause r
    ) {
        var aliasLeft = l.getTableAlias();
        var aliasRight = r.getTableAlias();
        var scrollLeft = scrolls.get(aliasLeft);
        var scrollRight = scrolls.get(aliasRight);

        if (!((scrollLeft == scrollRight) && (scrollLeft instanceof JoinIter))) {
            var filter = new JoinIter(aliasLeft, scrollLeft, aliasRight, scrollRight);
            scrolls.put(aliasLeft, filter);
            scrolls.put(aliasRight, filter);
        }
        ((JoinIter) scrolls.get(aliasLeft)).filter(l.getCol(), op, r.getCol());
    }

    private static void filter(
            HashMap<String, Scrollable> scrolls,
            SelectClause l,
            ComparisonOpEnum op,
            SelectClause r,
            Object lVal
    ) {
        var alias = l.getTableAlias();
        var scroll = scrolls.get(alias);
        if (!(scroll instanceof FilterIter)) {
            scrolls.put(alias, new FilterIter(scroll));
        }
        ((FilterIter) scrolls.get(alias)).filter(r.getCol(), op, lVal);
    }

    private static Map<String, FromClause> groupTabsByAlias(List<FromClause> tables) {
        var sorted = tables
                .stream()
                .sorted((o1, o2) -> JOIN_TYPE_ENUM_COMPARATOR.compare(o1.getType(), o2.getType()))
                .collect(Collectors.toList());
        var map = new LinkedHashMap<String, FromClause>();
        for (var s : sorted) {
            map.put(s.getAlias(), s);
        }
        return map;
    }

    private static Integer decTabs(HashMap<String, Integer> tabCount, String alias) {
        return tabCount.compute(alias, (s, n) -> n - 1);
    }

    private static Integer incTabs(HashMap<String, Integer> tabCount, SelectClause f) {
        return tabCount.compute(f.getTableAlias(), (s, n) -> n == null ? 1 : n + 1);
    }
}
