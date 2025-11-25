package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;

import java.util.function.Consumer;

public class OptionalPredicate extends ParserPredicate {
    private final ParserPredicate next;
    private final Consumer<Boolean> conditionMet;

    public OptionalPredicate(ParserPredicate next) {
        this(next, bool -> DbLogger.log(next, "Condition met: " + bool));
    }

    public OptionalPredicate(ParserPredicate next, Consumer<Boolean> conditionMet) {
        this.next = next;
        this.conditionMet = conditionMet;
    }

    @Override
    public boolean doTest(ParserInputStream is) {
        conditionMet.accept(next.test(is));
        return true;
    }
}
