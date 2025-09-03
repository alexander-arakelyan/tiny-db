package org.bambrikii.tiny.db.model.select;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WhereClause {
    private SelectClause l;
    private Object lVal;
    private ComparisonOpEnum op;
    private SelectClause r;
    private Object rVal;
}
