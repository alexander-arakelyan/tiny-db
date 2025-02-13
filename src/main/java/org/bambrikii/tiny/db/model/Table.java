package org.bambrikii.tiny.db.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Table {
    private Schema schema;
    private final List<Column> columns = new ArrayList<>();
    private final List<Partition> partitions = new ArrayList<>();
}
