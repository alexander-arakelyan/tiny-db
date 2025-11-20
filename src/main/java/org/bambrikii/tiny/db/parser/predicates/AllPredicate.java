package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.Arrays;

public class AllPredicate extends ParserPredicate {
    private final ParserPredicate[] next;

    public AllPredicate(ParserPredicate... next) {
        this.next = next;
    }

    @Override
    public boolean doTest(ParserInputStream is) {
        DbLogger.log(this, is, "" + next.length);
        return Arrays
                .stream(next)
                .allMatch(next2 -> next2.test(is));
    }
}
