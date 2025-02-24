package org.bambrikii.tiny.db.parser;

import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;
import org.bambrikii.tiny.db.parser.predicates.SequencePredicate;
import org.bambrikii.tiny.db.parser.predicates.SpacePredicate;

import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.ParserFunctions.order;

public class CommandParserFunctions {
    private CommandParserFunctions() {
    }

    public static ParserPredicate<String> table(ParserPredicate<String> next) {
        return new SpacePredicate(new SequencePredicate("table", next));
    }

    public static ParserPredicate<String> alter(ParserPredicate<String> next) {
        return new SpacePredicate(new SequencePredicate("alter", next));
    }

    public static ParserPredicate<String> add(ParserPredicate<String> next) {
        return new SpacePredicate(new SequencePredicate("add", next));
    }

    public static ParserPredicate<String> drop(ParserPredicate<String> next) {
        return new SpacePredicate(new SequencePredicate("drop", next));
    }

    public static ParserPredicate<String> brackets(ParserPredicate next) {
        return order(
                new SpacePredicate(new SequencePredicate("(", next::test)),
                new SpacePredicate(new SequencePredicate(")", ((input, output) -> true)))
        );
    }

    public static ParserPredicate<String> nullable(Consumer<Boolean> onSuccess) {
        return new SpacePredicate(new SequencePredicate("nullable", (ParserPredicate<Boolean>) (input, output) -> {
            onSuccess.accept(output);
            return true;
        }));
    }

    public static ParserPredicate<String> unique(Consumer<Boolean> onSuccess) {
        return new SpacePredicate(new SequencePredicate("unique", (ParserPredicate<Boolean>) (input, output) -> {
            onSuccess.accept(output);
            return true;
        }));
    }

    public static ParserPredicate<String> comma(ParserPredicate next) {
        return new SpacePredicate(new SequencePredicate(",", next));
    }
}
