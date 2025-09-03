package org.bambrikii.tiny.db.plan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bambrikii.tiny.db.model.select.WhereClause;
import org.bambrikii.tiny.db.model.select.FromClause;
import org.bambrikii.tiny.db.model.select.SelectClause;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class QueryNode {
    private final String alias;
    private final FromClause table;
    private final List<SelectClause> cols = new ArrayList<>();
    private final List<FromClause> rights = new ArrayList<>();
    private final List<WhereClause> filters = new ArrayList<>();
}
