package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.List;
import java.util.function.Consumer;

public class ParserFunctions {
    private ParserFunctions() {
    }

    public static final ParserPredicate TRUE_PREDICATE = new ParserPredicate() {
        @Override
        protected boolean doTest(ParserInputStream is) {
            return true;
        }
    };

    public static final ParserPredicate FALSE_PREDICATE = new ParserPredicate() {
        @Override
        protected boolean doTest(ParserInputStream is) {
            return false;
        }
    };

    public static final Consumer<Boolean> DEFAULT_BOOLEAN_CONSUMER = bool -> {
    };

    public static <C> ParserPredicate word(ParserPredicate next, Consumer<String> consumer) {
        return spaces(new AnyWordPredicate(next, consumer));
    }

    public static ParserPredicate or(ParserPredicate... next) {
        return spaces(new ParserPredicate() {
            @Override
            protected boolean doTest(ParserInputStream is) {
                for (var next0 : next) {
                    if (next0.test(is)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static ParserPredicate number(Consumer<Integer> onSuccess) {
        return spaces(new NumberPredicate(TRUE_PREDICATE, onSuccess));
    }

    public static ParserPredicate unordered(ParserPredicate... next) {
        return spaces(new AnyOrderPredicate(next));
    }

    public static ParserPredicate optional(ParserPredicate next) {
        return spaces(new OptionalPredicate(next));
    }

    public static ParserPredicate ordered(ParserPredicate... next) {
        return spaces(new AndPredicate(next));
    }

    public static ParserPredicate atLeastOnce(ParserPredicate next) {
        return spaces(new ParserPredicate() {
            @Override
            protected boolean doTest(ParserInputStream is) {
                if (!next.test(is)) {
                    return false;
                }
                int count = 0;
                do {
                    count++;
                } while (comma(next).test(is));
                return count > 0;
            }
        });
    }


    public static ParserPredicate oneOfStrings(List<String> strings, ParserPredicate next, Consumer<String> consumer) {
        return spaces(new OneOfStringsPredicate(strings, next, consumer));
    }

    public static ParserPredicate chars(String str, ParserPredicate next) {
        return spaces(new CharsPredicate(str, next));
    }

    public static ParserPredicate chars(String str, Consumer<Boolean> onMatch) {
        return spaces(new CharsPredicate(str, TRUE_PREDICATE, onMatch));
    }

    public static SpacesPredicate spaces(ParserPredicate next) {
        return new SpacesPredicate(next);
    }

    public static ParserPredicate brackets(ParserPredicate next) {
        return ordered(
                spaces(chars("(", next)),
                spaces(chars(")", DEFAULT_BOOLEAN_CONSUMER))
        );
    }

    public static ParserPredicate singleQuoted(ParserPredicate next) {
        return ordered(
                spaces(chars("'", next)),
                spaces(chars("'", DEFAULT_BOOLEAN_CONSUMER))
        );
    }

    public static ParserPredicate comma(ParserPredicate next) {
        return spaces(chars(",", next));
    }
}
