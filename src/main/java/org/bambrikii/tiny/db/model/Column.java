package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Column {
    private String name;
    private String type;
    private boolean unique;
    private boolean nullable;
    private Reference reference;
}
