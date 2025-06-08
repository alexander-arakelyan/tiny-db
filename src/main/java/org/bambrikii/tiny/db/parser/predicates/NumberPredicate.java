package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.function.Consumer;

public class NumberPredicate extends ParserPredicate {
    private final ParserPredicate next;
    private final Consumer<Integer> consumer;

    public NumberPredicate(ParserPredicate next, Consumer<Integer> consumer) {
        this.next = next;
        this.consumer = consumer;
    }

    @Override
    public boolean doTest(ParserInputStream is) {
        DbLogger.log(this, is, next.toString());
        var mark = is.pos();
        var ch = is.val();
        int pos = 0;
        var val = 0;
        while (ch >= '0' && ch <= '9') {
            val = val * 10 + (ch - '0');
            pos++;
            is.next();
            ch = is.val();
        }
        if (pos == 0 || !next.test(is)) {
            return false;
        }
        consumer.accept(val);
        return true;
    }
}
