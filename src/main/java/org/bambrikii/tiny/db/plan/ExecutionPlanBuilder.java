package org.bambrikii.tiny.db.plan;

import org.bambrikii.tiny.db.model.select.WhereClause;
import org.bambrikii.tiny.db.model.select.FromClause;
import org.bambrikii.tiny.db.model.JoinTypeEnumComparator;
import org.bambrikii.tiny.db.model.select.SelectClause;
import org.bambrikii.tiny.db.plan.cursorts.DefaultCursor;
import org.bambrikii.tiny.db.plan.cursorts.Scrollable;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.ArrayList;
import java.util.HashMap;
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
        var tablesSorted = topoSort(from, where);
        var filtersByAlias = groupFiltersByAlias(where);
        for (var ts : tablesSorted) {
            var t = ts.getTable();
            var a = ts.getAlias();
            var fs = filtersByAlias.get(a);
        }
        return new DefaultCursor(ctx, tablesSorted, filtersByAlias);
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

    private static List<FromClause> topoSort(List<FromClause> tables, List<WhereClause> filters) {
        // build map of tables
        var tabs = groupTabsByAlias(tables);
        var sorted = new ArrayList<FromClause>();
        var tabsToAdd = new HashMap<>(tabs);
        var aliasesIncluded = new HashMap<String, Integer>();
        for (var f : filters) {
            incTabs(aliasesIncluded, f.getL());
            incTabs(aliasesIncluded, f.getR());
        }
        while (!filters.isEmpty()) {
            var iter = filters.iterator();
            while (iter.hasNext()) {
                var f = iter.next();
                var alias = f.getL().getTableAlias();
                if (aliasesIncluded.get(alias) > 1) {
                    continue;
                }
                aliasesIncluded.remove(alias);
                var t = tabs.get(alias);
                decTabs(aliasesIncluded, t.getAlias());
                tabsToAdd.remove(alias);
                sorted.add(t);
                iter.remove();
            }
        }
        sorted.addAll(tabsToAdd.values());

        return sorted;
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
