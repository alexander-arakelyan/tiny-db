package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class SpacesPredicate extends ParserPredicate {
    private final ParserPredicate next;

    public SpacesPredicate(ParserPredicate next) {
        this.next = next;
    }

    @Override
    protected boolean doTest(ParserInputStream is) {
        byte ch = is.val();
        while ((ch == ' ')) {
            is.next();
            ch = is.val();
        }
        return next.test(is);
    }
}
