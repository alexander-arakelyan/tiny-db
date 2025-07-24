package org.bambrikii.tiny.db.cmd.deleterows;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.FilterCommandable;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsMessage;

@Getter
public class DeleteRowsMessage extends SelectRowsMessage implements FilterCommandable {
    private String targetTable;

    public void name(String name) {
        this.targetTable = name;
    }
}
