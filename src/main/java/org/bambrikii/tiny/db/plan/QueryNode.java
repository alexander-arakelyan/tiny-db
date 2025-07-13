package org.bambrikii.tiny.db.plan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.model.Join;
import org.bambrikii.tiny.db.model.select.ColumnRef;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class QueryNode {
    private final String alias;
    private final Join join;
    private final List<ColumnRef> cols = new ArrayList<>();
    private final List<Join> rights = new ArrayList<>();
    private final List<Filter> filters = new ArrayList<>();
}
