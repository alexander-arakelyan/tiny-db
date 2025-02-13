package org.bambrikii.tiny.db.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Partition {
    private String column;
    private String from;
    private String to;
    private String node;
}
