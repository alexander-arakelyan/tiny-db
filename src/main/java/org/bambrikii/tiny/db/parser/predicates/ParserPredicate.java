package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public interface ParserPredicate<T> {
    boolean test(ParserInputStream input, T output);
}
