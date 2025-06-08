package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Reference {
    private String schema;
    private String table;
}
