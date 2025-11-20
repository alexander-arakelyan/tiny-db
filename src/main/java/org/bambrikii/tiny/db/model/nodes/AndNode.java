package org.bambrikii.tiny.db.model.nodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ToString
@Setter
@Getter
public class AndNode implements WhereNode {
    private List<WhereNode> ands = new ArrayList<>();

    public static AndNode of(WhereNode predicate) {
        var and = new AndNode();
        and.getAnds().add(predicate);
        return and;
    }

    public static AndNode of(Collection<WhereNode> predicates) {
        var and = new AndNode();
        and.getAnds().addAll(predicates);
        return and;
    }

    public WhereNode and(WhereNode predicate) {
        ands.add(predicate);
        return this;
    }
}
