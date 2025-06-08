package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Column {
    private String name;
    private String type;
    private int precision;
    private int scale;
    private int size;
    private boolean unique;
    private boolean nullable;
    private Reference reference;
}
