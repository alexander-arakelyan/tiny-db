package org.bambrikii.tiny.db.plan.impl;

import org.bambrikii.tiny.db.plan.filters.AbstractFilter;

import java.util.Set;
import java.util.function.BiConsumer;

public interface AbstractFilterStrategy<T> {
    Set<String> apply(T predicates, BiConsumer<String, AbstractFilter> filterBiConsumer);
}
