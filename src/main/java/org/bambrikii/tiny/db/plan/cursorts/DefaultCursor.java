package org.bambrikii.tiny.db.plan.cursorts;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.plan.filters.ExecutionFilter;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;
import org.bambrikii.tiny.db.storage.StorageContext;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultCursor implements Scrollable {
    private final StorageContext ctx;
    private final Scrollable scroll;
    private final Map<String, List<ExecutionFilter>> filtersByAlias;


    @Override
    public void open() {
        scroll.open();
    }

    @Override
    public Row next() {
        return scroll.next();
    }

    @Override
    public void reset() {
        scroll.reset();
    }

    @Override
    public void close() {
        scroll.close();
    }
}
