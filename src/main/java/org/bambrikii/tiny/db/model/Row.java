package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Row {
    private String rowId;

    public abstract Object read(String col);
}
