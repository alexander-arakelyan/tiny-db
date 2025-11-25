package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.model.clauses.WhereClause;

public interface WhereCommandable {
    void where(WhereClause filter);
}
