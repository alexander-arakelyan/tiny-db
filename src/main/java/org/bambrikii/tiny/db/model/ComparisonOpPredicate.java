package org.bambrikii.tiny.db.model;

public interface ComparisonOpPredicate {
    boolean test(Object left, Object right);
}
