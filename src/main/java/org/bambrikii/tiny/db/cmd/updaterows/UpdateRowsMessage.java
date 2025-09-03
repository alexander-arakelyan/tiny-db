package org.bambrikii.tiny.db.cmd.updaterows;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.shared.AbstractQueryMessage;
import org.bambrikii.tiny.db.model.select.FromClause;
import org.bambrikii.tiny.db.model.select.WhereClause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class UpdateRowsMessage implements AbstractQueryMessage {
    private String targetTable;
    private final Map<String, Object> targetValues = new HashMap<>();
    private final List<FromClause> from = new ArrayList<>();
    private final List<WhereClause> where = new ArrayList<>();

    public void table(String table) {
        this.targetTable = table;
    }

    public void columnValue(String col, Object val) {
        targetValues.put(col, val);
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
