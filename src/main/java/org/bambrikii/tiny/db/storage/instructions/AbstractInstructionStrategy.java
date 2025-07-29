package org.bambrikii.tiny.db.storage.instructions;

import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.plan.iterators.Scrollable;

import java.util.Map;
import java.util.function.Function;

public interface AbstractInstructionStrategy {
    boolean write(TableStruct struct);

    boolean drop(String name);

    Scrollable scan(String name);

    void insert(String name, Row row, Function<Row, Map<String, Object>> valuesResolver);

    void update(String name, Row row, Function<Row, Map<String, Object>> valuesResolver);

    void delete(String name, String rowId);
}
