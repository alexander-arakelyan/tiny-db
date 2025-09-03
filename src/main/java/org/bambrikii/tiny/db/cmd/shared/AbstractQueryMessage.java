package org.bambrikii.tiny.db.cmd.shared;

import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.WhereCommandable;
import org.bambrikii.tiny.db.model.select.FromClause;
import org.bambrikii.tiny.db.model.select.WhereClause;

public interface AbstractQueryMessage extends AbstractMessage, WhereCommandable {
    void from(FromClause from);

    void where(WhereClause where);
}
