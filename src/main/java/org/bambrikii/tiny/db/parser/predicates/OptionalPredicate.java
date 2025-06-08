package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class OptionalPredicate extends ParserPredicate {
    private final ParserPredicate next;

    public OptionalPredicate(ParserPredicate next) {
        this.next = next;
    }

    @Override
    public boolean doTest(ParserInputStream input) {
        next.test(input);
        return true;
    }
}
