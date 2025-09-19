package org.bambrikii.tiny.db.plan.iterators.join;

import java.util.Comparator;
import java.util.Objects;

class FilterValueComparator implements Comparator {
    public static final FilterValueComparator VALUE_COMPARATOR = new FilterValueComparator();

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return -1;
        }
        if (o1 == o2) {
            return 0;
        }
        if (Objects.equals(o1, o2)) {
            return 0;
        }
        if (o1 instanceof Comparable && o2 instanceof Comparable) {
            return ((Comparable) o1).compareTo(o2);
        }
        return CharSequence.compare(o1.toString(), o2.toString());
    }
}
