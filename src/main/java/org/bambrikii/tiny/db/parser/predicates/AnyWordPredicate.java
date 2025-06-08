package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AnyWordPredicate extends ParserPredicate {
    private final ParserPredicate next;
    private final Consumer<String> onMatch;

    public AnyWordPredicate(ParserPredicate next, Consumer<String> onMatch) {
        this.next = next;
        this.onMatch = onMatch;
    }

    @Override
    public boolean doTest(ParserInputStream input) {
        DbLogger.log(this, input, next.toString());
        var mark1 = input.pos();
        var ch = input.val();
        if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_'))) {
            return false;
        }
        do {
            input.next();
            ch = input.val();
        } while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_') || (ch >= '0' && ch <= '9'));
        var mark2 = input.pos();
        if (mark2 - mark1 == 0 || !next.test(input)) {
            return false;
        }
        var str = input.bytes(mark1, mark2);
        onMatch.accept(new String(str, UTF_8));
        return true;
    }
}
