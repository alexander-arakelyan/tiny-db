package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Row {
    private String rowId;
    private final Map<Column, Object> values = new HashMap<>();
}
