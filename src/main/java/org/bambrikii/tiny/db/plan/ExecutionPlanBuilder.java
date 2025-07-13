package org.bambrikii.tiny.db.plan;

import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.JoinTypeEnumComparator;
import org.bambrikii.tiny.db.model.select.ColumnRef;
import org.bambrikii.tiny.db.plan.cursorts.CursorFactory;
import org.bambrikii.tiny.db.plan.cursorts.Scrollable;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ExecutionPlanBuilder {

    public static final JoinTypeEnumComparator JOIN_TYPE_ENUM_COMPARATOR = new JoinTypeEnumComparator();

    private final StorageContext ctx;

    public ExecutionPlanBuilder(StorageContext ctx) {
        this.ctx = ctx;
    }

    public Scrollable iterate(
            List<Join> tables,
            List<Filter> filters
    ) {
        // topo sort filters
        var tablesSorted = topoSort(tables, filters);
        var filtersByAlias = groupFiltersByAlias(filters);
        for (var ts : tablesSorted) {
            var t = ts.getTable();
            var a = ts.getAlias();
            var fs = filtersByAlias.get(a);
        }
        return CursorFactory.createCursor(ctx, tablesSorted, filtersByAlias);
    }

    private HashMap<String, List<ExecutionFilter>> groupFiltersByAlias(List<Filter> filters) {
        var map = new HashMap<String, List<ExecutionFilter>>();
        for (var f : filters) {
            addFilter(map, f.getL(), f);
            addFilter(map, f.getR(), f);
        }
        return map;
    }

    private static void addFilter(HashMap<String, List<ExecutionFilter>> map, ColumnRef col, Filter f) {
        map.compute(col.getAlias(), (s, fs) -> {
            if (fs == null) {
                fs = new ArrayList<>();
            }
//            fs.add(ExecutionFilter.fromFilter(f));
            return fs;
        });
    }

    private static List<Join> topoSort(List<Join> tables, List<Filter> filters) {
        // build map of tables
        var tabs = groupTabsByAlias(tables);
        var sorted = new ArrayList<Join>();
        var aliasCount = new HashMap<String, Integer>();
        for (var f : filters) {
            incTabs(aliasCount, f.getL());
            incTabs(aliasCount, f.getR());
        }
        while (!filters.isEmpty()) {
            var iter = filters.iterator();
            while (iter.hasNext()) {
                var f = iter.next();
                var alias = f.getL().getAlias();
                if (aliasCount.get(alias) > 1) {
                    continue;
                }
                aliasCount.remove(alias);
                var t = tabs.get(alias);
                decTabs(aliasCount, t.getAlias());
                sorted.add(t);
                iter.remove();
            }
        }
        return sorted;
    }

    private static Map<String, Join> groupTabsByAlias(List<Join> tables) {
        var sorted = tables
                .stream()
                .sorted((o1, o2) -> JOIN_TYPE_ENUM_COMPARATOR.compare(o1.getType(), o2.getType()))
                .collect(Collectors.toList());
        var map = new HashMap<String, Join>();
        for (var s : sorted) {
            map.put(s.getAlias(), s);
        }
        return map;
    }

    private static Integer decTabs(HashMap<String, Integer> tabCount, String alias) {
        return tabCount.compute(alias, (BiFunction<String, Integer, Integer>) (s, integer) -> integer - 1);
    }

    private static Integer incTabs(HashMap<String, Integer> tabCount, ColumnRef f) {
        return tabCount.compute(f.getAlias(), (s1, integer) -> integer == null ? 1 : integer + 1);
    }
}
