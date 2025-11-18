package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

public abstract class ParserPredicate {
    protected abstract boolean doTest(ParserInputStream is);

    public final boolean test(ParserInputStream is) {
        DbLogger.log(this, is, this.toString());
        var mark = is.pos();
        if (!doTest(is)) {
            is.rollback(mark);
            return false;
        }
        return true;
    }
}
