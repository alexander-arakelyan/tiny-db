package org.bambrikii.tiny.db.parser;

import org.bambrikii.tiny.db.cmd.AddColumnCommandable;
import org.bambrikii.tiny.db.cmd.DropColumnCommandable;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.brackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.chars;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.comma;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.number;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.optional;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.ordered;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.spaces;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.unordered;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class CommandParserFunctions {
    private CommandParserFunctions() {
    }

    public static ParserPredicate create(ParserPredicate next) {
        return spaces(chars("create", next));
    }


    public static ParserPredicate table(ParserPredicate next) {
        return spaces(chars("table", next));
    }

    public static ParserPredicate alter(ParserPredicate next) {
        return spaces(chars("alter", next));
    }

    public static ParserPredicate add(ParserPredicate next) {
        return spaces(chars("add", next));
    }

    public static ParserPredicate drop(ParserPredicate next) {
        return spaces(chars("drop", next));
    }

    public static ParserPredicate nullable(Consumer<Boolean> onSuccess) {
        return spaces(chars("nullable", onSuccess));
    }

    public static ParserPredicate unique(Consumer<Boolean> onSuccess) {
        return spaces(chars("unique", onSuccess));
    }

    public static ParserPredicate colDef(AddColumnCommandable cmd) {
        var precision = new AtomicInteger(0);
        var scale = new AtomicInteger(0);
        var nullable = new AtomicBoolean(false);
        var unique = new AtomicBoolean(false);
        var type = new AtomicReference<>("");
        return word(word(optional(ordered(
                                brackets(ordered(
                                        number(precision::set),
                                        optional(comma(number(scale::set)))
                                )),
                                unordered(
                                        optional(nullable(nullable::set)),
                                        optional(unique(unique::set))
                                ))),
                        newValue -> {
                            DbLogger.log(ParserPredicate.class, null, "type " + type + " " + precision.get() + " " + scale.get() + " " + nullable.get());
                            type.set(newValue);
                        }
                ),
                col -> {
                    DbLogger.log(ParserPredicate.class, null, "col " + col + ", type " + type + " " + precision.get() + " " + scale.get() + " " + nullable.get());
                    cmd.addColumn(col, type.get(), precision.get(), scale.get(), nullable.get(), unique.get());
                }
        );
    }

    public static ParserPredicate alterCol(AddColumnCommandable cmd) {
        return spaces(chars("alter", chars("column", colDef(cmd))));
    }

    public static ParserPredicate addCol(AddColumnCommandable cmd) {
        return spaces(chars("alter", chars("column", colDef(cmd))));
    }

    public static ParserPredicate dropCol(DropColumnCommandable cmd) {
        return drop(chars("column", word(TRUE_PREDICATE, cmd::dropCol)));
    }
}
