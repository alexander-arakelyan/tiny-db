package org.bambrikii.tiny.db.model.where;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ToString
@Setter
@Getter
public class AndPredicate implements WherePredicate {
    private List<WherePredicate> ands = new ArrayList<>();

    public static AndPredicate of(WherePredicate predicate) {
        var and = new AndPredicate();
        and.getAnds().add(predicate);
        return and;
    }

    public static AndPredicate of(Collection<WherePredicate> predicates) {
        var and = new AndPredicate();
        and.getAnds().addAll(predicates);
        return and;
    }

    public WherePredicate and(WherePredicate predicate) {
        ands.add(predicate);
        return this;
    }
}
