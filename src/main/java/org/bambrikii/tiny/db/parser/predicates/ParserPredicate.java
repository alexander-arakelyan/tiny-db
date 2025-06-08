package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public abstract class ParserPredicate {
    protected abstract boolean doTest(ParserInputStream input);

    public final boolean test(ParserInputStream input) {
        var mark = input.pos();
        if (!doTest(input)) {
            input.rollback(mark);
            return false;
        }
        return true;
    }
}
