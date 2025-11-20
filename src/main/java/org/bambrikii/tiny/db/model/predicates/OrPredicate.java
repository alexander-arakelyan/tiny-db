package org.bambrikii.tiny.db.model.predicates;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ToString
@Setter
@Getter
public class OrPredicate implements WherePredicate {
    private List<WherePredicate> ors = new ArrayList<>();

    public static OrPredicate of(WherePredicate predicate) {
        var or = new OrPredicate();
        or.getOrs().add(predicate);
        return or;
    }

    public static OrPredicate of(Collection<WherePredicate> predicates) {
        var or = new OrPredicate();
        or.getOrs().addAll(predicates);
        return or;
    }
}
