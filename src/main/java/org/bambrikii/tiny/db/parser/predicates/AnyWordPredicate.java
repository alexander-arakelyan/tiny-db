package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AnyWordPredicate extends ParserPredicate {
    private final ParserPredicate next;
    private final Consumer<String> consumer;

    public AnyWordPredicate(ParserPredicate next, Consumer<String> consumer) {
        this.next = next;
        this.consumer = consumer;
    }

    @Override
    public boolean doTest(ParserInputStream is) {
        DbLogger.log(this, is, next.toString());
        var mark1 = is.pos();
        var ch = is.val();
        if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_'))) {
            return false;
        }
        do {
            is.next();
            ch = is.val();
        } while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_') || (ch >= '0' && ch <= '9'));
        var mark2 = is.pos();
        if (mark2 - mark1 == 0 || !next.test(is)) {
            return false;
        }
        var str = is.bytes(mark1, mark2);
        consumer.accept(new String(str, UTF_8));
        return true;
    }
}
