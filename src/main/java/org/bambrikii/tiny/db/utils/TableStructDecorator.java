package org.bambrikii.tiny.db.utils;

import lombok.Getter;
import org.bambrikii.tiny.db.model.Column;
import org.bambrikii.tiny.db.model.TableStruct;

import java.util.Map;
import java.util.stream.Collectors;

public class TableStructDecorator {
    @Getter
    private final TableStruct struct;
    private final Map<String, Column> columnsByName;

    public TableStructDecorator(TableStruct struct) {
        this.struct = struct;
        this.columnsByName = struct
                .getColumns()
                .stream()
                .collect(Collectors.toUnmodifiableMap(Column::getName, col -> col, (a, b) -> b));
    }

    public Column getColumnByName(String name) {
        return columnsByName.get(name);
    }
}
