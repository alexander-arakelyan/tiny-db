package org.bambrikii.tiny.db.parser.functions;

import org.bambrikii.tiny.db.parser.predicates.CharsPredicate;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;
import org.bambrikii.tiny.db.parser.predicates.SpacesPredicate;

import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.DEFAULT_STRING_CONSUMER;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.TRUE_PREDICATE;

public class CharsFunctions {
    private CharsFunctions() {
    }

    public static ParserPredicate chars(String str, ParserPredicate next) {
        return chars(str, next, DEFAULT_STRING_CONSUMER);
    }

    public static ParserPredicate chars(String str, Consumer<String> consumer) {
        return chars(str, TRUE_PREDICATE, consumer);
    }

    public static ParserPredicate chars(String str, ParserPredicate next, Consumer<String> consumer) {
        return spaces(new CharsPredicate(str, next, consumer));
    }

    public static SpacesPredicate spaces(ParserPredicate next) {
        return new SpacesPredicate(next);
    }

    public static ParserPredicate comma(ParserPredicate next) {
        return spaces(chars(",", next));
    }
}
