package org.bambrikii.tiny.db.cmd.insertrows;

import lombok.ToString;
import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;

import java.util.ArrayList;
import java.util.List;

@ToString
public class InsertRowsCommand implements AbstractCommand {
    private String table;
    private final List<String> columnNames = new ArrayList<>();
    private final List<Object> columnValues = new ArrayList<>();

    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        return null;
    }

    public void table(String table) {
        this.table = table;
    }

    public void columnName(String s) {
        columnNames.add(s);
    }

    public void columnValue(Object s) {
        columnValues.add(s);
    }
}
