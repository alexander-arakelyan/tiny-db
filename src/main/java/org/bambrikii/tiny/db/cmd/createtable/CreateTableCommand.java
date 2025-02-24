package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.Column;

import java.util.ArrayList;
import java.util.List;

public class CreateTableCommand implements AbstractCommand {
    private String name;
    private final List<Column> columns = new ArrayList<>();
    private final List<Column> dropColumns = new ArrayList<>();

    public CreateTableCommand name(String name) {
        this.name = name;
        return this;
    }

    public CreateTableCommand addColumn(String name, String type, Integer size, boolean nullable, boolean unique) {
        Column col = new Column();
        col.setName(name);
        col.setType(type);
        col.setNullable(nullable);
        col.setUnique(unique);
        columns.add(col);
        return this;
    }

    public CreateTableCommand dropColumn(String name) {
        var col = new Column();
        col.setName(name);
        dropColumns.add(col);
        return this;
    }

    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        var exec = new CreateTableExecutor();
        return exec.exec(this, ctx);
    }
}
