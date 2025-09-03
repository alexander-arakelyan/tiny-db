package org.bambrikii.tiny.db.model.select;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class QueryClause {
    private final List<SelectClause> select = new ArrayList<>();
    private final List<FromClause> from = new ArrayList<>();
    private final List<WhereClause> where = new ArrayList<>();
    private final List<OrderByClause> orderBy = new ArrayList<>();
    private final List<GroupByClause> groupBy = new ArrayList<>();
}
