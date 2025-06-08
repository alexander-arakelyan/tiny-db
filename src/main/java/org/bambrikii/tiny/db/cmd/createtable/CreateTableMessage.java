package org.bambrikii.tiny.db.cmd.createtable;

import lombok.Getter;
import lombok.ToString;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.AddColumnCommandable;
import org.bambrikii.tiny.db.model.Column;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class CreateTableMessage implements AbstractMessage, AddColumnCommandable {
    private String name;
    private final List<Column> columns = new ArrayList<>();
    private final List<Column> dropColumns = new ArrayList<>();

    public CreateTableMessage name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void addColumn(String name, String type, int scale, int precision, boolean nullable, boolean unique) {
        Column col = new Column();
        col.setName(name);
        col.setType(type);
        col.setScale(scale);
        col.setPrecision(precision);
        col.setNullable(nullable);
        col.setUnique(unique);
        columns.add(col);
    }

    public CreateTableMessage dropColumn(String name) {
        var col = new Column();
        col.setName(name);
        dropColumns.add(col);
        return this;
    }
}
