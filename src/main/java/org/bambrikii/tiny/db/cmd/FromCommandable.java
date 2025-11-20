package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.model.clauses.FromClause;

public interface FromCommandable {
    void from(FromClause filter);
}
