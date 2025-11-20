package org.bambrikii.tiny.db.cmd.shared;

import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.WhereCommandable;
import org.bambrikii.tiny.db.model.JoinTypeEnum;
import org.bambrikii.tiny.db.model.clauses.FromClause;
import org.bambrikii.tiny.db.model.clauses.WhereClause;

public interface AbstractQueryMessage extends AbstractMessage, WhereCommandable {
    void from(FromClause from);

    default void from(String table, JoinTypeEnum dir, String alias) {
        from(FromClause.of(table, dir, alias));
    }

    void where(WhereClause where);
}
