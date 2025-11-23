package org.bambrikii.tiny.db.parser.functions;

import org.bambrikii.tiny.db.parser.predicates.NumberPredicate;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;

import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.functions.CharsFunctions.spaces;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.TRUE_PREDICATE;

public class NumberFunctions {
    private NumberFunctions() {
    }

    public static ParserPredicate number(Consumer<Integer> onSuccess) {
        return spaces(new NumberPredicate(TRUE_PREDICATE, onSuccess));
    }

    public static ParserPredicate number(ParserPredicate next, Consumer<Integer> onSuccess) {
        return spaces(new NumberPredicate(next, onSuccess));
    }

}
