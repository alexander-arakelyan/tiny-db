package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public abstract class ParserPredicate {
    protected abstract boolean doTest(ParserInputStream is);

    public final boolean test(ParserInputStream is) {
        var mark = is.pos();
        if (!doTest(is)) {
            is.rollback(mark);
            return false;
        }
        return true;
    }
}
