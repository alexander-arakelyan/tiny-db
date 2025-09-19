package org.bambrikii.tiny.db.model.select;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.where.WherePredicate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WhereClause {
    private WherePredicate predicate;

    public static WhereClause of(WherePredicate predicate) {
        var clause = new WhereClause();
        clause.setPredicate(predicate);
        return clause;
    }
}
