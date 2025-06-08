package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.Arrays;

public class AndPredicate extends ParserPredicate {
    private final ParserPredicate[] next;

    public AndPredicate(ParserPredicate... next) {
        this.next = next;
    }

    @Override
    public boolean doTest(ParserInputStream input) {
        DbLogger.log(this, input, "" + next.length);
        return Arrays
                .stream(next)
                .allMatch(next1 -> next1.test(input));
    }
}
