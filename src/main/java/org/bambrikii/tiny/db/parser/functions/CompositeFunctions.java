package org.bambrikii.tiny.db.parser.functions;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.parser.predicates.AllPredicate;
import org.bambrikii.tiny.db.parser.predicates.AnyOrderPredicate;
import org.bambrikii.tiny.db.parser.predicates.ChainPredicate;
import org.bambrikii.tiny.db.parser.predicates.CharsPredicate;
import org.bambrikii.tiny.db.parser.predicates.ConstantResultPredicate;
import org.bambrikii.tiny.db.parser.predicates.OneOfStringsPredicate;
import org.bambrikii.tiny.db.parser.predicates.OptionalPredicate;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;
import org.bambrikii.tiny.db.parser.predicates.SpacesPredicate;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.bambrikii.tiny.db.parser.functions.NumberFunctions.number;
import static org.bambrikii.tiny.db.parser.functions.QuotedFunctions.quotedString;

public class CompositeFunctions {
    private CompositeFunctions() {
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
    public static final Consumer<String> DEFAULT_STRING_CONSUMER = str -> {
    };
    public static final Function<Consumer<Boolean>, Consumer<String>> DEFAULT_STRING_TO_BOOLEAN_CONSUMER = booleanConsumer -> s -> booleanConsumer.accept(true);

    public static <C> ParserPredicate word(ParserPredicate next, Consumer<String> consumer) {
        return spaces(WordFunctions.word(next, consumer));
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

    public static ParserPredicate times(ParserPredicate next) {
        return times(0, Integer.MAX_VALUE, next);
    }

    public static ParserPredicate times(int min, int max, ParserPredicate next) {
        return spaces(new ParserPredicate() {
            @Override
            protected boolean doTest(ParserInputStream is) {
                int n = 0;
                while (next.test(is)) {
                    n++;
                }
                return n >= min && n <= max;
            }
        });
    }

    public static ParserPredicate unordered(ParserPredicate... next) {
        return spaces(new AnyOrderPredicate(next));
    }

    public static ParserPredicate optional(ParserPredicate next) {
        return spaces(new OptionalPredicate(next));
    }

    public static ParserPredicate ordered(ParserPredicate... next) {
        return spaces(new AllPredicate(next));
    }

    public static ParserPredicate atLeastOnceCommaSeparated(ParserPredicate next) {
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

    public static ParserPredicate brackets(ParserPredicate next) {
        return ordered(
                spaces(chars("(", next)),
                spaces(chars(")", DEFAULT_STRING_CONSUMER))
        );
    }

    public static ParserPredicate optionalBrackets(ParserPredicate next, Consumer<Boolean> bracketsConsumer) {
        return or(
                spaces(chars("(", ChainPredicate.link(
                        next,
                        spaces(chars(")", ConstantResultPredicate.of(TRUE_PREDICATE, bracketsConsumer, false)))
                ))),
                ConstantResultPredicate.of(next, bracketsConsumer, false)
        );
    }

    public static ParserPredicate comma(ParserPredicate next) {
        return spaces(chars(",", next));
    }

    public static ParserPredicate value(ParserPredicate next, Consumer<Integer> integerConsumer, Consumer<String> stringConsumer) {
        return or(
                number(next, integerConsumer),
                quotedString(next, stringConsumer)
        );
    }
}
