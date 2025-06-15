package org.bambrikii.tiny.db.parser;

import org.bambrikii.tiny.db.cmd.AddColumnCommandable;
import org.bambrikii.tiny.db.cmd.DropColumnCommandable;
import org.bambrikii.tiny.db.cmd.FilterCommandable;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsMessage;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.parser.predicates.ColumnReferencePredicate;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.DEFAULT_STRING_TO_BOOLEAN_CONSUMER;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.brackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.chars;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.comma;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.number;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.oneOfStrings;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.optional;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.or;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.ordered;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.spaces;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.times;
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
        return spaces(chars("nullable", s -> onSuccess.accept(true)));
    }

    public static ParserPredicate unique(Consumer<Boolean> onSuccess) {
        return spaces(chars("unique", DEFAULT_STRING_TO_BOOLEAN_CONSUMER.apply(onSuccess)));
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

    public static ParserPredicate filter(Consumer<Filter> consumer) {
        return filter(TRUE_PREDICATE, consumer);
    }

    public static ParserPredicate filter(ParserPredicate next, Consumer<Filter> consumer) {
        var left = new AtomicReference<String>();
        var op = new AtomicReference<ComparisonOpEnum>();
        return colRef(oneOfStrings(
                        ComparisonOpEnum.sqlRepresentations(),
                        colRef(next, left::set),
                        s -> op.set(ComparisonOpEnum.parse(s))
                ),
                s -> consumer.accept(new Filter(left.get(), op.get(), s))
        );
    }

    public static ParserPredicate where(FilterCommandable cmd) {
        return chars("where", filter(cmd::filter));
    }


    public static <C> ParserPredicate colRef(ParserPredicate next, Consumer<String> consumer) {
        return spaces(new ColumnReferencePredicate(next, consumer));
    }

    public static <C> ParserPredicate select(SelectRowsMessage cmd) {
        return chars("select", atLeastOnceCommaSeparated(or(colRef(TRUE_PREDICATE, cmd::column))));
    }

    public static <C> ParserPredicate from(
            SelectRowsMessage cmd,
            AtomicReference<String> joinTableAliasRef
    ) {
        return chars("from", word(word(TRUE_PREDICATE, joinTableAliasRef::set), s -> cmd.table(s, null, joinTableAliasRef.get())));
    }

    public static <C> ParserPredicate join(
            SelectRowsMessage cmd,
            AtomicReference<String> joinTableNameRef,
            AtomicReference<String> joinTableAliasRef
    ) {
        return oneOfStrings(List.of("inner", "left", "right"), chars("join",
                        word(
                                word(
                                        chars("on", filter(TRUE_PREDICATE, cmd::filter)),
                                        joinTableAliasRef::set
                                ),
                                joinTableNameRef::set
                        )
                ),
                joinDir -> cmd.table(joinTableNameRef.get(), joinDir, joinTableAliasRef.get())
        );
    }

    public static <C> ParserPredicate joins(
            SelectRowsMessage cmd,
            AtomicReference<String> joinTableNameRef,
            AtomicReference<String> joinTableAliasRef
    ) {
        return times(join(cmd, joinTableAliasRef, joinTableNameRef));
    }
}
