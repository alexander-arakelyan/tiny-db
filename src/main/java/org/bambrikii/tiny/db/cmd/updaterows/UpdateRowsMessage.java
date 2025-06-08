package org.bambrikii.tiny.db.cmd.updaterows;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.AbstractMessage;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UpdateRowsMessage implements AbstractMessage {
    private String name;
    private final Map<String, Object> values = new HashMap<>();

    public void name(String name) {
        this.name = name;
    }

    public void put(String col, Object val) {
        values.put(col, val);
    }
}
