package org.bambrikii.tiny.db.storagelayout;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bambrikii.tiny.db.model.Row;

import java.util.HashMap;
import java.util.Map;

@ToString
@RequiredArgsConstructor
public class PhysicalRow extends Row {
    private final Map<String, Object> vals = new HashMap<>();

    public Object read(String columnName) {
        return vals.get(columnName);
    }

    @Override
    public void write(String col, Object val) {
        this.vals.put(col, val);
    }
}
