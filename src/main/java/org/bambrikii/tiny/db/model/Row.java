package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Row {
    private String rowId;
    private boolean deleted;

    public abstract Object read(String col);
}
