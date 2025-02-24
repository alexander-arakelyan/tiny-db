package org.bambrikii.tiny.db.parser;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.parser.predicates.*;

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
}
