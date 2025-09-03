package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.select.FromClause;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;
import org.bambrikii.tiny.db.plan.operators.ComparisonUtils;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Filter3Iter implements Scrollable {
    private final StorageContext ctx;
    private final List<FromClause> tablesSorted;
    private final Map<String, List<ExecutionFilter>> filtersByAlias;
    private final int ind;

    private LogicalRow logicalRow;
    private Scrollable curr;
    private FromClause tb;
    private String alias;
    private List<ExecutionFilter> filters;
    private Scrollable next;
    private Row currRow;
    private boolean currShouldAdvance;

    public void open() {
        this.tb = tablesSorted.get(ind);
        this.alias = tb.getAlias();
        this.filters = filtersByAlias.getOrDefault(alias, new ArrayList<>());
        logicalRow = new LogicalRow();

        curr = ctx.scan(tb.getTable());
        curr.open();
        currShouldAdvance = true;
        if (ind + 1 < tablesSorted.size()) {
            this.next = new Filter3Iter(ctx, tablesSorted, filtersByAlias, ind + 1);
            next.open();
        }
    }

    public Row next() {
        w:
        while (currShouldAdvance) {
            currRow = curr.next();
            if (currRow == null) {
                return null;
            }
            logicalRow.combine(alias, currRow);
            for (var f : filters) {
                if (!ComparisonUtils.matches(logicalRow, f)) {
                    continue w;
                }
            }
            currShouldAdvance = false;
        }

        if (next != null) {
            var nextRow = next.next();
            if (nextRow == null) {
                currShouldAdvance = true;
            } else {
                logicalRow.combine(alias, nextRow);
            }
        }

        return logicalRow;
    }

    @Override
    public void reset() {
        if (next != null) {
            next.reset();
        }
        if (curr != null) {
            curr.reset();
        }
    }

    @SneakyThrows
    public void close() {
        if (next != null) {
            next.close();
        }
        if (curr != null) {
            curr.close();
        }
    }
}

