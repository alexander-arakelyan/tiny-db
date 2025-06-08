package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.DEFAULT_BOOLEAN_CONSUMER;

public class CharsPredicate extends ParserPredicate {
    private final String s;
    private final ParserPredicate next;
    private final Consumer<Boolean> consumer;

    public CharsPredicate(String s, ParserPredicate next) {
        this(s, next, DEFAULT_BOOLEAN_CONSUMER);
    }

    public CharsPredicate(String s, ParserPredicate next, Consumer<Boolean> consumer) {
        this.s = s;
        this.next = next;
        this.consumer = consumer;
    }

    @Override
    public boolean doTest(ParserInputStream is) {
        DbLogger.log(this, is, s);
        var ch = is.val();
        int pos = 0;
        int len = s.length();
        while ((pos < len && ch == s.charAt(pos))) {
            is.next();
            ch = is.val();
            pos++;
        }
        return len == pos && next.test(is);
    }
}
