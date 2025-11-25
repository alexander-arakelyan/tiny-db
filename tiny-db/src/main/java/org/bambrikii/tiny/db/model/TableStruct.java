package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.clauses.WhereClause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter
@Setter
public class TableStruct {
    private String schema;
    private String table;
    private int version = 0;
    private final Map<String, WhereClause> partitions = new HashMap<>();
    private final List<Column> columns = new ArrayList<>();
}
