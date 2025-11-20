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
public class OrNode implements WhereNode {
    private List<WhereNode> ors = new ArrayList<>();

    public static OrNode of(WhereNode predicate) {
        var or = new OrNode();
        or.getOrs().add(predicate);
        return or;
    }

    public static OrNode of(Collection<WhereNode> predicates) {
        var or = new OrNode();
        or.getOrs().addAll(predicates);
        return or;
    }
}
