package org.bambrikii.tiny.db.cmd.altertable;

import lombok.Setter;
import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.Column;

import java.util.ArrayList;
import java.util.List;

public class AlterTableCommand implements AbstractCommand {
    @Setter
    private String name;
    private final List<Column> addColumns = new ArrayList<>();
    private final List<Column> dropColumns = new ArrayList<>();

    public void addColumn(String name, String type, int precision, int scale, boolean nullable, boolean unique) {
        var col = new Column();
        col.setName(name);
        col.setType(type);
        col.setPrecision(precision);
        col.setScale(scale);
        col.setNullable(nullable);
        col.setUnique(unique);
        addColumns.add(col);
    }

    public void dropColumn(String name) {
        var col = new Column();
        col.setName(name);
        dropColumns.add(col);
    }

    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        return null;
    }
}
