package org.bambrikii.tiny.db.model.clauses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.nodes.WhereNode;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WhereClause {
    private WhereNode node;

    public static WhereClause of(WhereNode predicate) {
        var clause = new WhereClause();
        clause.setNode(predicate);
        return clause;
    }
}
