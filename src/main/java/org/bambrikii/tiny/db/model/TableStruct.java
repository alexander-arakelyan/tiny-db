package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TableStruct {
    private String schema;
    private String table;
    private int version = 0;
    private final Map<String, Filter> partitions = new HashMap<>();
    private final List<Column> columns = new ArrayList<>();
}
