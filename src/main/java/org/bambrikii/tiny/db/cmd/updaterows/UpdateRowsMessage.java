package org.bambrikii.tiny.db.cmd.updaterows;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsMessage;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UpdateRowsMessage extends SelectRowsMessage {
    private String targetTable;
    private final Map<String, Object> targetValues = new HashMap<>();

    public void name(String name) {
        this.targetTable = name;
    }

    public void columnValue(String col, Object val) {
        targetValues.put(col, val);
    }
}
