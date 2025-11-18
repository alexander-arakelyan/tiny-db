package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

public class SpacesPredicate extends ParserPredicate {
    private final ParserPredicate next;

    public SpacesPredicate(ParserPredicate next) {
        this.next = next;
    }

    @Override
    protected boolean doTest(ParserInputStream is) {
        DbLogger.log(this, is, next.toString());
        byte ch = is.val();
        while ((ch == ' ')) {
            is.next();
            ch = is.val();
        }
        return next.test(is);
    }
}
