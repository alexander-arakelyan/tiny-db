package org.bambrikii.tiny.db.parser;

import lombok.Getter;
import lombok.Setter;
import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;

@Getter
@Setter
class SampleCommand implements AbstractCommand {
    private String name;

    public void addColumn(String name, String type, int size, boolean nullable, boolean unique) {

    }

    public void dropColumn(String name) {

    }

    @Override
    public CommandResult exec(AbstractExecutorContext ctx) {
        return null;
    }
}
