package org.bambrikii.tiny.db.model.clauses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SelectClause {
    private String tableAlias;
    private String col;
}
