package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Row {
    private String rowId;
    private final List<Object> values = new ArrayList<>();
}
