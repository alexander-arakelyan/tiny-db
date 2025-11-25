package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.List;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CharsRangePredicate extends ParserPredicate {
    protected final ParserPredicate next;
    protected final Consumer<String> consumer;
    private final ByteChecker[] firstPredicates;
    private final ByteChecker[] otherPredicates;

    public CharsRangePredicate(
            List<ByteChecker> firstPredicates,
            List<ByteChecker> otherPredicates,
            ParserPredicate next,
            Consumer<String> consumer
    ) {
        this.firstPredicates = firstPredicates.toArray(new ByteChecker[0]);
        this.otherPredicates = otherPredicates.toArray(new ByteChecker[0]);
        this.next = next;
        this.consumer = consumer;
    }

    @Override
    public boolean doTest(ParserInputStream is) {
        DbLogger.log(this, is, next.toString());
        var mark1 = is.pos();
        var ch = is.val();
        if (!matchesFirst(ch)) {
            return false;
        }
        do {
            is.next();
            ch = is.val();
        } while (matchesFirst(ch) || matchesOther(ch));
        var mark2 = is.pos();
        if (mark2 - mark1 == 0 || !next.test(is)) {
            return false;
        }
        var str = is.bytes(mark1, mark2);
        consumer.accept(new String(str, UTF_8));
        return true;
    }

    private boolean matchesFirst(byte ch) {
        for (var predicate : firstPredicates) {
            if (predicate.test(ch)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesOther(byte ch) {
        for (var predicate : otherPredicates) {
            if (predicate.test(ch)) {
                return true;
            }
        }
        return false;
    }
}
