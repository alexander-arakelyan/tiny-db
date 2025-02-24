package org.bambrikii.tiny.db.parser;

import org.bambrikii.tiny.db.cmd.AddColumnCommandable;
import org.bambrikii.tiny.db.cmd.DropColumnCommandable;
import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;
import org.bambrikii.tiny.db.parser.predicates.SequencePredicate;
import org.bambrikii.tiny.db.parser.predicates.SpacePredicate;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.ParserFunctions.*;
import static org.bambrikii.tiny.db.parser.ParserFunctions.optional;

public class CommandParserFunctions {
    private CommandParserFunctions() {
    }

    public static ParserPredicate<String> create(ParserPredicate<String> next) {
        return s(w("create", next));
    }

    public static ParserPredicate<String> table(ParserPredicate<String> next) {
        return s(w("table", next));
    }

    public static ParserPredicate<String> alter(ParserPredicate<String> next) {
        return s(w("alter", next));
    }

    public static ParserPredicate<String> add(ParserPredicate<String> next) {
        return s(w("add", next));
    }

    public static ParserPredicate<String> drop(ParserPredicate<String> next) {
        return s(w("drop", next));
    }

    public static ParserPredicate<String> brackets(ParserPredicate next) {
        return order(
                s(w("(", next::test)),
                s(w(")", (input, output) -> true))
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
        return s(w(",", next));
    }

    public static ParserPredicate<String> parseAddColumn(AddColumnCommandable cmd) {
        return add(brackets(w("values", (colInput, col) -> {
            var colMark = colInput.pos();
            var res = w("values", (typeInput, type) -> {
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

    public static ParserPredicate<String> delete(ParserPredicate<String> next) {
        return s(w("delete", next));
    }

    public static ParserPredicate<String> from(ParserPredicate<String> next) {
        return s(w("from", next));
    }

    public static ParserPredicate<String> where(ParserPredicate<String> next) {
        return s(w("where", next));
    }

    public static ParserPredicate<String> insert(ParserPredicate<String> next) {
        return s(w("insert", next));
    }

    public static ParserPredicate<String> into(ParserPredicate<String> next) {
        return s(w("into", next));
    }

    public static ParserPredicate<String> value(ParserPredicate<String> next) {
        return s(w("values", next));
    }

    public static ParserPredicate<String> and(ParserPredicate<String> next) {
        return s(w("and", next));
    }

    public static ParserPredicate<String> or(ParserPredicate<String> next) {
        return s(w("or", next));
    }

    public static ParserPredicate<String> cond(
            TriConsumer<String, String, String> consumer
    ) {
        return s(w("values", (input, output) -> {
            var mark = input.pos();
            var leftRef = new AtomicReference<String>();
            var left1 = leftPredicate(leftRef::set).test(input, output);
            if (!left1) {
                input.rollback(mark);
                return false;
            }
            var condRef = new AtomicReference<String>();
            var cond1 = condOperator(condRef::set).test(input, output);
            if (!cond1) {
                input.rollback(mark);
                return false;
            }
            var rightRef = new AtomicReference<String>();
            var right1 = rightPredicate(rightRef::set).test(input, output);
            if (!right1) {
                input.rollback(mark);
                return false;
            }
            consumer.accept(leftRef.get(), condRef.get(), rightRef.get());
            return true;
        }));
    }

    public static ParserPredicate<String> leftPredicate(Consumer<String> consumer) {

    }

    public static ParserPredicate<String> condOperator(Consumer<String> consumer) {

    }

    public static ParserPredicate<String> rightPredicate(Consumer<String> consumer) {

    }
}
