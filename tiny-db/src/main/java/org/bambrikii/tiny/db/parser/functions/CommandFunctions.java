package org.bambrikii.tiny.db.parser.functions;

import org.bambrikii.tiny.db.cmd.AddColumnCommandable;
import org.bambrikii.tiny.db.cmd.DropColumnCommandable;
import org.bambrikii.tiny.db.cmd.WhereCommandable;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsMessage;
import org.bambrikii.tiny.db.cmd.shared.AbstractQueryMessage;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.JoinTypeEnum;
import org.bambrikii.tiny.db.model.clauses.SelectClause;
import org.bambrikii.tiny.db.model.clauses.WhereClause;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.functions.BracketsFunctions.brackets;
import static org.bambrikii.tiny.db.parser.functions.CharsFunctions.chars;
import static org.bambrikii.tiny.db.parser.functions.CharsFunctions.comma;
import static org.bambrikii.tiny.db.parser.functions.CharsFunctions.spaces;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.DEFAULT_STRING_TO_BOOLEAN_CONSUMER;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.oneOfStrings;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.optional;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.or;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.ordered;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.times;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.unordered;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.word;
import static org.bambrikii.tiny.db.parser.functions.NumberFunctions.number;
import static org.bambrikii.tiny.db.parser.functions.QuotedFunctions.optionalDoubleQuotedString;

public class CommandFunctions {
    private CommandFunctions() {
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

    public static ParserPredicate where(WhereCommandable cmd) {
        return chars("where", conditions(cmd));
    }

    private static ParserPredicate conditions(WhereCommandable cmd) {
        return WhereFunctions.wherePredicate(wherePredicate -> cmd.where(WhereClause.of(wherePredicate)));
    }


    public static <C> ParserPredicate colRef(ParserPredicate next, Consumer<SelectClause> consumer) {
        var colRef = new AtomicReference<String>();
        return spaces(
                word(chars(".",
                                word(next, colRef::set)
                        ),
                        s -> consumer.accept(new SelectClause(s, colRef.get()))
                )
        );
    }

    public static <C> ParserPredicate select(SelectRowsMessage cmd) {
        return chars("select", atLeastOnceCommaSeparated(or(colRef(TRUE_PREDICATE, cmd::select))));
    }

    public static <C> ParserPredicate from(AbstractQueryMessage cmd) {
        var joinTableAliasRef = new AtomicReference<String>();
        return chars("from",
                optionalDoubleQuotedString(
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
                        optionalDoubleQuotedString(
                                word(
                                        chars("on", conditions(cmd)),
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
}
