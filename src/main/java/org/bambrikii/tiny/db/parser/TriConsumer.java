package org.bambrikii.tiny.db.parser;

public interface TriConsumer<A, B, C> {
    void accept(A a, B b, C c);
}
