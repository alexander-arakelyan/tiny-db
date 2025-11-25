package org.bambrikii.tiny.db.parser.predicates;

import lombok.ToString;
import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.DEFAULT_STRING_CONSUMER;

@ToString
public class CharsPredicate extends ParserPredicate {
    private final String s;
    private final ParserPredicate next;
    private final Consumer<String> consumer;

    public CharsPredicate(String s, ParserPredicate next) {
        this(s, next, DEFAULT_STRING_CONSUMER);
    }

    public CharsPredicate(String s, ParserPredicate next, Consumer<String> consumer) {
        this.s = s;
        this.next = next;
        this.consumer = consumer;
    }

    @Override
    public boolean doTest(ParserInputStream is) {
        DbLogger.log(this, is, s);
        var mark1 = is.pos();
        var ch = is.val();
        int pos = 0;
        int len = s.length();
        while ((pos < len && ch == s.charAt(pos))) {
            is.next();
            ch = is.val();
            pos++;
        }
        if (len != pos) {
            return false;
        }
        var mark2 = is.pos();
        if (!next.test(is)) {
            return false;
        }
        consumer.accept(new String(is.bytes(mark1, mark2), UTF_8));
        return true;
    }
}
