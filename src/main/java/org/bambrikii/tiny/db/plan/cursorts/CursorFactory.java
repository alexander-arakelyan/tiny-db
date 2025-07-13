package org.bambrikii.tiny.db.plan.cursorts;

import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.List;
import java.util.Map;

public class CursorFactory {
    private CursorFactory() {

    }

    public static Scrollable create(
            StorageContext ctx,
            List<Join> tablesSorted,
            Map<String, List<ExecutionFilter>> filtersByAlias
    ) {
        return new DefaultCursor(ctx, tablesSorted, filtersByAlias);
    }
}
