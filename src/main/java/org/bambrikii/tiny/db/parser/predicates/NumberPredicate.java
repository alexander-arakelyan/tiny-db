package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.function.Consumer;

public class NumberPredicate extends ParserPredicate {
    private final ParserPredicate next;
    private final Consumer<Integer> onMatch;

    public NumberPredicate(ParserPredicate next, Consumer<Integer> onMatch) {
        this.next = next;
        this.onMatch = onMatch;
    }

    @Override
    public boolean doTest(ParserInputStream input) {
        DbLogger.log(this, input, next.toString());
        var mark = input.pos();
        var ch = input.val();
        int pos = 0;
        var val = 0;
        while (ch >= '0' && ch <= '9') {
            val = val * 10 + (ch - '0');
            pos++;
            input.next();
            ch = input.val();
        }
        if (pos == 0 || !next.test(input)) {
            return false;
        }
        onMatch.accept(val);
        return true;
    }
}
