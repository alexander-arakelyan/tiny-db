package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.ParserFunctions.DEFAULT_BOOLEAN_CONSUMER;
import static org.bambrikii.tiny.db.parser.ParserFunctions.TRUE_PREDICATE;

public class SequencePredicate extends ParserPredicate {
    private final String s;
    private final ParserPredicate next;
    private final Consumer<Boolean> onMatch;

    public SequencePredicate(String s, ParserPredicate next) {
        this(s, next, DEFAULT_BOOLEAN_CONSUMER);
    }

    public SequencePredicate(String s, ParserPredicate next, Consumer<Boolean> onMatch) {
        this.s = s;
        this.next = next;
        this.onMatch = onMatch;
    }

    @Override
    public boolean doTest(ParserInputStream input) {
        DbLogger.log(this, input, s);
        var ch = input.val();
        int pos = 0;
        int len = s.length();
        while ((pos < len && ch == s.charAt(pos))) {
            input.next();
            ch = input.val();
            pos++;
        }
        return len == pos && next.test(input);
    }

    public static SequencePredicate chars(String str, ParserPredicate next) {
        return new SequencePredicate(str, next);
    }

    public static SequencePredicate chars(String str, Consumer<Boolean> onMatch) {
        return new SequencePredicate(str, TRUE_PREDICATE, onMatch);
    }
}
