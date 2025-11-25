package org.bambrikii.tiny.db.model.clauses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.model.JoinTypeEnum;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FromClause {
    private String table;
    private JoinTypeEnum type;
    private String alias;

    public static FromClause of(String table, JoinTypeEnum type, String alias) {
        return new FromClause(table, type, alias);
    }
}
