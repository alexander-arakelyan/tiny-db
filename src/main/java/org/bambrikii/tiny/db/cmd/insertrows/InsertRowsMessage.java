package org.bambrikii.tiny.db.cmd.insertrows;

import lombok.Getter;
import lombok.ToString;
import org.bambrikii.tiny.db.cmd.AbstractMessage;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class InsertRowsMessage implements AbstractMessage {
    private String name;
    private final List<String> colNames = new ArrayList<>();
    private final List<Object> colVals = new ArrayList<>();

    public void name(String name) {
        this.name = name;
    }

    public void columnName(String s) {
        colNames.add(s);
    }

    public void columnValue(Object s) {
        colVals.add(s);
    }
}
