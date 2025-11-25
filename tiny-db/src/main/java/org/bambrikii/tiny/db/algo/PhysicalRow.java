package org.bambrikii.tiny.db.algo;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bambrikii.tiny.db.model.Row;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ToString
@RequiredArgsConstructor
public class PhysicalRow extends Row {
    private final Map<String, Object> vals = new HashMap<>();

    public Set<String> keys() {
        return vals.keySet();
    }

    @Override
    public Object read(String tab, String col) {
        return vals.get(col);
    }

    @Override
    public void write(String tab, String col, Object val) {
        this.vals.put(col, val);
    }
}
