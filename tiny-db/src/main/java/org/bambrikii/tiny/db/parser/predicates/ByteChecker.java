package org.bambrikii.tiny.db.parser.predicates;

@FunctionalInterface
public interface ByteChecker {
    boolean test(byte b);
}
