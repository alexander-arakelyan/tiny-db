package org.bambrikii.tiny.db.plan;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.clauses.FromClause;
import org.bambrikii.tiny.db.model.clauses.WhereClause;
import org.bambrikii.tiny.db.plan.iterators.DefaultIter;
import org.bambrikii.tiny.db.model.Scrollable;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.List;

@RequiredArgsConstructor
public class Planner {
    private final StorageContext storageContext;

    public Scrollable execute(List<FromClause> from, List<WhereClause> where) {
        var ctx = new PlanContext();
        var factory = new PlanBuilder(ctx);

        from.forEach(clause -> ctx.merge(clause.getAlias(), storageContext.scan(clause.getTable())));

        for (WhereClause clause : where) {
            factory.build(clause.getNode());
        }

        var scroll = ctx.scan();
        return new DefaultIter(scroll);
    }
}
