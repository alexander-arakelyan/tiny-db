package org.bambrikii.tiny.db.cmd.selectrows;

import lombok.Getter;
import lombok.ToString;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.FilterCommandable;
import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.JoinTypeEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class SelectRowsMessage implements AbstractMessage, FilterCommandable {
    private final List<String> columns = new ArrayList<>();
    private final List<Join> tables = new ArrayList<>();
    private final List<Filter> filters = new ArrayList<>();

    public void column(String col) {
        columns.add(col);
    }

    public void table(String table, String type, String alias) {
        this.tables.add(new Join(table, JoinTypeEnum.parse(type), alias));
    }


    @Override
    public void filter(Filter filter) {
        filters.add(filter);
    }
}
