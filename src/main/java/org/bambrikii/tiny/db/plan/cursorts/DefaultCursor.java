package org.bambrikii.tiny.db.plan.cursorts;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.IteratorFactory;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;
import org.bambrikii.tiny.db.plan.iterators.LogicalRow;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultCursor extends org.bambrikii.tiny.db.plan.cursorts.Scrollable {
    private final StorageContext ctx;
    private final List<Join> tablesSorted;
    private final Map<String, List<ExecutionFilter>> filtersByAlias;
    private Scrollable it;
    private String alias;


    @Override
    public void open() {
        var lr = new LogicalRow();

        this.it = IteratorFactory.iterate(ctx, tablesSorted, filtersByAlias, 0, lr);
        var t = tablesSorted.get(0);
        this.alias = t.getAlias();
        it.open();
    }

    @Override
    public Row next() {
        return it.next();
    }

    @Override
    public void close() {
        it.close();
    }
}
