package org.bambrikii.tiny.db.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.select.ColumnRef;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Filter {
    private ColumnRef l;
    private Object lVal;
    private ComparisonOpEnum op;
    private ColumnRef r;
    private Object rVal;
}
