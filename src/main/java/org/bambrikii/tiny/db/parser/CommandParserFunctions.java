package org.bambrikii.tiny.db.parser;

import org.bambrikii.tiny.db.cmd.AddColumnCommandable;
import org.bambrikii.tiny.db.cmd.DropColumnCommandable;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;
import org.bambrikii.tiny.db.parser.predicates.SequencePredicate;
import org.bambrikii.tiny.db.parser.predicates.SpacePredicate;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.ParserFunctions.*;
import static org.bambrikii.tiny.db.parser.ParserFunctions.optional;

public class CommandParserFunctions {
    private CommandParserFunctions() {
    }

    public static ParserPredicate<String> create(ParserPredicate<String> next) {
        return new SpacePredicate(new SequencePredicate("create", next));
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

    public static ParserPredicate<String> parseAddColumn(AddColumnCommandable cmd) {
        return add(brackets(word((colInput, col) -> {
            var colMark = colInput.pos();
            var res = word((typeInput, type) -> {
                var typeMark = typeInput.pos();
                var precision = new AtomicInteger(0);
                var scale = new AtomicInteger(0);
                var nullable = new AtomicBoolean(false);
                var unique = new AtomicBoolean(false);
                var typeRes = order(
                        brackets(order(
                                number(precision::set),
                                optional(comma(number(scale::set)))
                        )),
                        unordered(
                                optional(nullable(nullable::set)),
                                optional(unique(unique::set))
                        )).test(typeInput, null);
                if (typeRes) {
                    cmd.addColumn(col, type, precision.get(), scale.get(), nullable.get(), unique.get());
                } else {
                    typeInput.rollback(typeMark);
                }
                return typeRes;
            }).test(colInput, col);
            if (!res) {
                colInput.rollback(colMark);
            }
            return res;
        })));
    }

    public static ParserPredicate<String> parseDropColumn(DropColumnCommandable cmd) {
        return drop(word(cmd::dropColumn));
    }
}
