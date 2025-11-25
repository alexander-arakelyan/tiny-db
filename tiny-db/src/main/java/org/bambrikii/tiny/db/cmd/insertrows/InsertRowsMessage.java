package org.bambrikii.tiny.db.cmd.insertrows;

import lombok.Getter;
import lombok.ToString;
import org.bambrikii.tiny.db.cmd.shared.AbstractQueryMessage;
import org.bambrikii.tiny.db.model.clauses.FromClause;
import org.bambrikii.tiny.db.model.clauses.WhereClause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter
public class InsertRowsMessage implements AbstractQueryMessage {
    private String into;
    private final Map<String, Object> targetValues = new HashMap<>();
    private final List<FromClause> from = new ArrayList<>();
    private final List<WhereClause> where = new ArrayList<>();

    public void into(String into) {
        this.into = into;
    }

    public InsertRowsMessage columnValue(String col, Object val) {
        targetValues.put(col, val);
        return this;
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
