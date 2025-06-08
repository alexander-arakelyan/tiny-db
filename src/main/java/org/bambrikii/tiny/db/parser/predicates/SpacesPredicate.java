package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class SpacesPredicate extends ParserPredicate {
    private final ParserPredicate next;

    public SpacesPredicate(ParserPredicate next) {
        this.next = next;
    }

    @Override
    protected boolean doTest(ParserInputStream input) {
        byte ch = input.val();
        while ((ch == ' ')) {
            input.next();
            ch = input.val();
        }
        return next.test(input);
    }

    public static SpacesPredicate spaces(ParserPredicate next) {
        return new SpacesPredicate(next);
    }
}
