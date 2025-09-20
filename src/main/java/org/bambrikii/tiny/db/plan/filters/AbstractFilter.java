package org.bambrikii.tiny.db.plan.filters;

import org.bambrikii.tiny.db.model.Row;

public interface AbstractFilter {
    boolean test(Row row);
}
