package org.bambrikii.tiny.db.parser.impl;

import org.bambrikii.tiny.db.cmd.AddColumnCommandable;
import org.bambrikii.tiny.db.cmd.DropColumnCommandable;
import org.bambrikii.tiny.db.cmd.WhereCommandable;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsMessage;
import org.bambrikii.tiny.db.cmd.shared.AbstractQueryMessage;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.JoinTypeEnum;
import org.bambrikii.tiny.db.model.select.SelectClause;
import org.bambrikii.tiny.db.model.select.WhereClause;
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
                            DbLogger.log(ParserPredicate.class, "type {} {} {} {}", type, precision.get(), scale.get(), nullable.get());
                            type.set(newValue);
                        }
                ),
                col -> {
                    DbLogger.log(ParserPredicate.class, "col {}, type {}, precision {}, scale {} ", col, type, precision.get(), scale.get(), nullable.get());
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

    public static ParserPredicate filter(Consumer<WhereClause> consumer) {
        return filter(TRUE_PREDICATE, consumer);
    }

    public static ParserPredicate filter(ParserPredicate next, Consumer<WhereClause> consumer) {
        var left = new AtomicReference<SelectClause>();
        var leftVal = new AtomicReference<>();
        var op = new AtomicReference<ComparisonOpEnum>();
        var right = new AtomicReference<SelectClause>();
        var rightVal = new AtomicReference<>();
        return or(
                colRef(
                        oneOfStrings(
                                ComparisonOpEnum.sqlRepresentations(),
                                or(
                                        value(next, rightVal::set, rightVal::set),
                                        colRef(next, right::set)

                                ),
                                s -> op.set(ComparisonOpEnum.parse(s))
                        ),
                        s -> consumer.accept(new WhereClause(left.get(), leftVal.get(), op.get(), s, rightVal.get()))
                ),
                value(
                        oneOfStrings(
                                ComparisonOpEnum.sqlRepresentations(),
                                or(
                                        value(next, rightVal::set, rightVal::set),
                                        colRef(next, right::set)

                                ),
                                s -> op.set(ComparisonOpEnum.parse(s))
                        ),
                        n -> consumer.accept(new WhereClause(null, n, op.get(), right.get(), rightVal.get())),
                        s -> consumer.accept(new WhereClause(null, s, op.get(), right.get(), rightVal.get()))
                )
        );
    }

    public static ParserPredicate where(WhereCommandable cmd) {
        return chars("where", filter(cmd::where));
    }


    public static <C> ParserPredicate colRef(ParserPredicate next, Consumer<SelectClause> consumer) {
        var colRef = new AtomicReference<String>();
        return spaces(
                word(chars(".",
                                word(next, colRef::set)
                        ),
                        s -> consumer.accept(new SelectClause(colRef.get(), s))
                )
        );
    }

    public static <C> ParserPredicate select(SelectRowsMessage cmd) {
        return chars("select", atLeastOnceCommaSeparated(or(colRef(TRUE_PREDICATE, cmd::select))));
    }

    public static <C> ParserPredicate from(AbstractQueryMessage cmd) {
        var joinTableAliasRef = new AtomicReference<String>();
        return chars("from",
                word(
                        word(
                                TRUE_PREDICATE,
                                joinTableAliasRef::set
                        ),
                        s -> cmd.from(s, JoinTypeEnum.INNER, joinTableAliasRef.get())
                )
        );
    }

    public static <C> ParserPredicate join(AbstractQueryMessage cmd) {
        var joinTableNameRef = new AtomicReference<String>();
        var joinTableAliasRef = new AtomicReference<String>();
        return oneOfStrings(List.of("inner", "left", "right"), chars("join",
                        word(
                                word(
                                        chars("on", filter(TRUE_PREDICATE, cmd::where)),
                                        joinTableAliasRef::set
                                ),
                                joinTableNameRef::set
                        )
                ),
                joinDir -> cmd.from(joinTableNameRef.get(), JoinTypeEnum.parse(joinDir), joinTableAliasRef.get())
        );
    }

    public static <C> ParserPredicate joins(SelectRowsMessage cmd) {
        return times(join(cmd));
    }

    public static ParserPredicate quotedString(ParserPredicate next, Consumer<String> consumer) {
        return chars("'", word(chars("'", next), consumer));
    }

    public static ParserPredicate value(ParserPredicate next, Consumer<Integer> integerConsumer, Consumer<String> stringConsumer) {
        return or(
                number(next, integerConsumer),
                quotedString(next, stringConsumer)
        );
    }
}
