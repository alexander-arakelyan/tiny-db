package org.bambrikii.tiny.db.plan;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.select.FromClause;
import org.bambrikii.tiny.db.model.select.WhereClause;
import org.bambrikii.tiny.db.plan.cursors.DefaultCursor;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class ExecutionPlanBuilder {
    private final StorageContext ctx;

    public Scrollable execute(
            List<FromClause> from,
            List<WhereClause> where
    ) {
        var scroll = scroll(from, where);
        return new DefaultCursor(ctx, scroll);
    }

    private Scrollable scroll(
            List<FromClause> from,
            List<WhereClause> where
    ) {
        var scrolls = new HashMap<String, Scrollable>();
        for (var clause : from) {
            var alias = clause.getAlias();
            scrolls.computeIfAbsent(alias, (k) -> ctx.scan(clause.getTable()));
        }
        //
        for (var clause : where) {
            ExecutionPlanFilterFactory.create(scrolls, clause.getPredicate());
        }
        return scrolls
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow()
                .getValue();
    }

}
