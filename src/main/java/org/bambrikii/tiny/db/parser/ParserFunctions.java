package org.bambrikii.tiny.db.parser;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.parser.predicates.*;

import java.util.List;
import java.util.function.Consumer;

public class ParserFunctions {
    private ParserFunctions() {
    }

    public static final ParserPredicate<String> TRUE_PREDICATE = new ParserPredicate<>() {
        @Override
        public boolean test(ParserInputStream input, String output) {
            return true;
        }
    };
    public static final ParserPredicate<String> FALSE_PREDICATE = new ParserPredicate<String>() {
        @Override
        public boolean test(ParserInputStream input, String output) {
            return false;
        }
    };

    public static <C> ParserPredicate<String> word(ParserPredicate<String> next) {
        return new SpacePredicate(new AnyWordPredicate(next));
    }

    public static <C> ParserPredicate<String> word(Consumer<String> next) {
        return new SpacePredicate(new AnyWordPredicate(assignString(next)));
    }


    public static ParserPredicate<String> assignString(Consumer<String> next) {
        return (input, output) -> {
            next.accept(output);
            return true;
        };
    }

    public static ParserPredicate<String> or(ParserPredicate<String>... next) {
        return new SpacePredicate((input, output) -> {
            var mark = input.pos();
            for (var next1 : next) {
                if (next1.test(input, output)) {
                    return true;
                }
            }
            input.rollback(mark);
            return false;
        });
    }

    public static ParserPredicate<String> number(Consumer<Integer> onSuccess) {
        return new SpacePredicate(new NumberPredicate((input, output) -> {
            onSuccess.accept(output);
            return true;
        }));
    }

    public static ParserPredicate<String> unordered(ParserPredicate<String>... next) {
        return new SpacePredicate(new AnyOrderPredicate(next));
    }

    public static ParserPredicate<String> optional(ParserPredicate<String> next) {
        return new SpacePredicate(new OptionalPredicate(next));
    }

    public static ParserPredicate<String> order(ParserPredicate... next) {
        return new SpacePredicate(new AndPredicate(next));
    }

    public static ParserPredicate<String> any(ParserPredicate next) {
        return new SpacePredicate((input, output) -> {
            var mark = input.pos();
            int count = 0;
            while (next.test(input, output)) {
                count++;
            }
            if (count > 0) {
                return true;
            }

            input.rollback(mark);
            return false;
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
