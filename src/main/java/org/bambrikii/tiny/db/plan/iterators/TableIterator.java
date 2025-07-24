package org.bambrikii.tiny.db.plan.iterators;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.IteratorFactory;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;
import org.bambrikii.tiny.db.plan.operators.ComparisonUtils;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TableIterator implements Scrollable {
    private final StorageContext ctx;
    private final List<Join> tablesSorted;
    private final Map<String, List<ExecutionFilter>> filtersByAlias;
    private final int ind;
    private final LogicalRow logicalRow;

    private Scrollable curr;
    private Join tb;
    private String alias;
    private List<ExecutionFilter> fs;
    private Scrollable next;
    private Row currRow;
    private boolean currShouldAdvance;

    public void open() {
        this.tb = tablesSorted.get(ind);
        this.alias = tb.getAlias();
        this.fs = filtersByAlias.get(alias);

        curr = ctx.scan(tb.getTable());
        currShouldAdvance = true;
        if (ind + 1 < tablesSorted.size()) {
            this.next = IteratorFactory.scan(ctx, tablesSorted, filtersByAlias, ind + 1, logicalRow);
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
            for (var f : fs) {
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

    }

    @SneakyThrows
    public void close() {
        next.close();
        curr.close();
    }
}

