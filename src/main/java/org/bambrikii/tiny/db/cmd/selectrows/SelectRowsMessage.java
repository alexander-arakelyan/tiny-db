package org.bambrikii.tiny.db.cmd.selectrows;

import lombok.Getter;
import lombok.ToString;
import org.bambrikii.tiny.db.cmd.shared.AbstractQueryMessage;
import org.bambrikii.tiny.db.model.select.FromClause;
import org.bambrikii.tiny.db.model.select.GroupByClause;
import org.bambrikii.tiny.db.model.select.OrderByClause;
import org.bambrikii.tiny.db.model.select.SelectClause;
import org.bambrikii.tiny.db.model.select.WhereClause;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class SelectRowsMessage implements AbstractQueryMessage {
    private final List<SelectClause> select = new ArrayList<>();
    private final List<FromClause> from = new ArrayList<>();
    private final List<WhereClause> where = new ArrayList<>();
    private final List<OrderByClause> orderBy = new ArrayList<>();
    private final List<GroupByClause> groupBy = new ArrayList<>();

    public void select(SelectClause col) {
        select.add(col);
    }

    public void from(FromClause from) {
        this.from.add(from);
    }

    @Override
    public void where(WhereClause where) {
        this.where.add(where);
    }

    public void orderBy(OrderByClause sort) {
        orderBy.add(sort);
    }
}
