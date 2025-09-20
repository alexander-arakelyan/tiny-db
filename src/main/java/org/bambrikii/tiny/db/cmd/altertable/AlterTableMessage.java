package org.bambrikii.tiny.db.cmd.altertable;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.AddColumnCommandable;
import org.bambrikii.tiny.db.cmd.DropColumnCommandable;
import org.bambrikii.tiny.db.model.Column;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AlterTableMessage implements AbstractMessage, AddColumnCommandable, DropColumnCommandable {
    private String name;
    private final List<Column> addColumns = new ArrayList<>();
    private final List<Column> dropColumns = new ArrayList<>();

    public AlterTableMessage name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void addColumn(String name, String type, int scale, int precision, boolean nullable, boolean unique) {
        var col = new Column();
        col.setName(name);
        col.setType(type);
        col.setScale(scale);
        col.setPrecision(precision);
        col.setNullable(nullable);
        col.setUnique(unique);
        addColumns.add(col);
    }

    @Override
    public void dropCol(String name) {
        var col = new Column();
        col.setName(name);
        dropColumns.add(col);
    }
}
