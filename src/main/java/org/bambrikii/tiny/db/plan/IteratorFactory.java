package org.bambrikii.tiny.db.plan;

import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;
import org.bambrikii.tiny.db.plan.iterators.LogicalRow;
import org.bambrikii.tiny.db.plan.iterators.TableIterator;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.List;
import java.util.Map;

public class IteratorFactory {
    private IteratorFactory() {
    }

    public static TableIterator scan(
            StorageContext ctx,
            List<Join> tablesSorted,
            Map<String, List<ExecutionFilter>> filtersByAlias,
            int ind,
            LogicalRow logicalRow
    ) {
        return new TableIterator(ctx, tablesSorted, filtersByAlias, ind, logicalRow);
    }
}
