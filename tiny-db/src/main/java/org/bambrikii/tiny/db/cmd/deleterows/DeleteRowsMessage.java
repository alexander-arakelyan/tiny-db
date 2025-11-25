package org.bambrikii.tiny.db.cmd.deleterows;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.shared.AbstractQueryMessage;
import org.bambrikii.tiny.db.model.clauses.FromClause;
import org.bambrikii.tiny.db.model.clauses.WhereClause;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DeleteRowsMessage implements AbstractQueryMessage {
    private String targetTable;
    private final List<FromClause> from = new ArrayList<>();
    private final List<WhereClause> where = new ArrayList<>();

    public void name(String name) {
        this.targetTable = name;
    }

    @Override
    public void from(FromClause from) {
        this.from.add(from);
    }

    @Override
    public void where(WhereClause where) {
        this.where.add(where);
    }
}
