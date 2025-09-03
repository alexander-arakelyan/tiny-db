package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.model.select.WhereClause;

public interface WhereCommandable {
    void where(WhereClause filter);
}
