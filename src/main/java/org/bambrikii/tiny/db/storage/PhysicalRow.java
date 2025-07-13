package org.bambrikii.tiny.db.storage;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;

import java.util.Objects;

@RequiredArgsConstructor
public class PhysicalRow extends Row {
    private final TableStruct tableStruct;

    public Object read(String columnName) {
        if (colNums.containsKey(columnName)) {
            return read(columnName));
        }
        var columns = tableStruct.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            var col = columns.get(i);
            if (Objects.equals(col.getName(), columnName)) {
                colNums.putIfAbsent(columnName, i);
                return read(i);
            }
        }
        throw new UnsupportedOperationException("Column " + columnName + " not found.");
    }
}
