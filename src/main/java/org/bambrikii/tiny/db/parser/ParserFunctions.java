package org.bambrikii.tiny.db.parser;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.parser.predicates.AndPredicate;
import org.bambrikii.tiny.db.parser.predicates.AnyOrderPredicate;
import org.bambrikii.tiny.db.parser.predicates.AnyWordPredicate;
import org.bambrikii.tiny.db.parser.predicates.NumberPredicate;
import org.bambrikii.tiny.db.parser.predicates.OptionalPredicate;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;
import org.bambrikii.tiny.db.parser.predicates.SpacesPredicate;

import java.util.List;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.CommandParserFunctions.comma;

public class ParserFunctions {
    private ParserFunctions() {
    }

    public static final ParserPredicate TRUE_PREDICATE = new ParserPredicate() {
        @Override
        protected boolean doTest(ParserInputStream input) {
            return true;
        }
    };

    public static final ParserPredicate FALSE_PREDICATE = new ParserPredicate() {
        @Override
        protected boolean doTest(ParserInputStream input) {
            return false;
        }
    };

    public static final Consumer<Boolean> DEFAULT_BOOLEAN_CONSUMER = bool -> {
    };

    public static <C> ParserPredicate word(ParserPredicate next, Consumer<String> onMatch) {
        return new SpacesPredicate(new AnyWordPredicate(next, onMatch));
    }

    public static ParserPredicate or(ParserPredicate... next) {
        return new SpacesPredicate(new ParserPredicate() {
            @Override
            protected boolean doTest(ParserInputStream input) {
                for (var next0 : next) {
                    if (next0.test(input)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static ParserPredicate number(Consumer<Integer> onSuccess) {
        return new SpacesPredicate(new NumberPredicate(TRUE_PREDICATE, onSuccess));
    }

    public static ParserPredicate unordered(ParserPredicate... next) {
        return new SpacesPredicate(new AnyOrderPredicate(next));
    }

    public static ParserPredicate optional(ParserPredicate next) {
        return new SpacesPredicate(new OptionalPredicate(next));
    }

    public static ParserPredicate ordered(ParserPredicate... next) {
        return new SpacesPredicate(new AndPredicate(next));
    }

    public static ParserPredicate atLeastOnce(ParserPredicate next) {
        return new SpacesPredicate(new ParserPredicate() {
            @Override
            protected boolean doTest(ParserInputStream input) {
                if (!next.test(input)) {
                    return false;
                }
                int count = 0;
                do {
                    count++;
                } while (comma(next).test(input));
                return count > 0;
            }
        });
    }

    public static ParserPredicate<String> oneOf(
            List<String> options,
            ParserPredicate<String> next,
            Consumer<String> consumer
    ) {
        return new SpacePredicate((input, output) -> {
            var mark = input.pos();
            for (var str : options) {
                if (w(str, (inp, out) -> {
                    var res = next.test(input, str);
                    if (res) {
                        consumer.accept(str);
                        return true;
                    }
                    inp.rollback(mark);
                    return false;
                }).test(input, output)) {
                    return true;
                }
                input.rollback(mark);
            }
            return false;
        })
    }

    public static SpacePredicate s(SequencePredicate w) {
        return new SpacePredicate(w);
    }

    public static SequencePredicate w(String val, ParserPredicate<String> next) {
        return new SequencePredicate(val, next);
    }
}
