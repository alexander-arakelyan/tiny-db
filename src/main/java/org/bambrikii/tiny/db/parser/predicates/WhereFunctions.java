package org.bambrikii.tiny.db.parser.predicates;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.LogicalOpEnum;
import org.bambrikii.tiny.db.model.select.SelectClause;
import org.bambrikii.tiny.db.model.where.AndPredicate;
import org.bambrikii.tiny.db.model.where.FilterPredicate;
import org.bambrikii.tiny.db.model.where.OrPredicate;
import org.bambrikii.tiny.db.model.where.WherePredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.colRef;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.quotedString;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.number;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.oneOfStrings;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.optionalBrackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.or;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.ordered;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.times;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WhereFunctions {
    public static ParserPredicate wherePredicate(Consumer<WherePredicate> where) {
        var whereRef = new AtomicReference<WherePredicate>();
        var ands = new ArrayList<WherePredicate>();
        var ors = new ArrayList<WherePredicate>();
        var nextWhereRef = new AtomicReference<WherePredicate>();
        var debugCounter = new AtomicInteger();
        return optionalBrackets(ordered(
                basicPredicate(whereRef),
                times(
                        oneOfStrings(
                                List.of("and", "or"),
                                or(
                                        basicPredicate(nextWhereRef),
                                        new ParserPredicate() {
                                            @Override
                                            protected boolean doTest(ParserInputStream is) {
                                                return wherePredicate(nextWhereRef::set).test(is);
                                            }
                                        }), s -> {
                                    var n = debugCounter.incrementAndGet();
                                    switch (Objects.requireNonNull(LogicalOpEnum.parse(s))) {
                                        case AND:
                                            DbLogger.log(whereRef.get(), "Predicate: %s AND %s %s %d", whereRef.get(), nextWhereRef.get(), ands, n);
                                            appendPredicate(whereRef, ands, nextWhereRef.get());
                                            break;
                                        case OR:
                                            DbLogger.log(whereRef.get(), "Predicate: %s OR %s %s %d", whereRef.get(), nextWhereRef.get(), ors, n);
                                            appendPredicate(whereRef, ors, nextWhereRef.get());
                                            break;
                                    }
                                })
                )
        ), withBrackets -> {
            var n = debugCounter.get();
            if (ands.isEmpty() && ors.isEmpty()) {
                DbLogger.log(whereRef.get(), "Predicate: EMPTY %s %d", whereRef.get(), n);
                where.accept(whereRef.get());
            } else if (ands.isEmpty()) {
                DbLogger.log(whereRef.get(), "Predicate: ORS %s %s %d", whereRef.get(), ors, n);
                where.accept(OrPredicate.of(ors));
            } else if (ors.isEmpty()) {
                DbLogger.log(whereRef.get(), "Predicate: ANDS %s %s %d", whereRef.get(), ands, n);
                where.accept(org.bambrikii.tiny.db.model.where.AndPredicate.of(ands));
            } else {
                DbLogger.log(whereRef.get(), "Predicate: ANDS+ORS %s %s %s %d", whereRef.get(), ands, ors, n);
                where.accept(AndPredicate.of(ands).and(OrPredicate.of(ors)));
            }
        });
    }

    private static ParserPredicate basicPredicate(AtomicReference<WherePredicate> whereRef) {
        return or(
                predicateRelation(whereRef::set),
                predicateValue(whereRef::set)
        );
    }

    private static void appendPredicate(
            AtomicReference<WherePredicate> whereRef,
            List<WherePredicate> coll,
            WherePredicate wherePredicate
    ) {
        var first = whereRef.get();
        if (first != null) {
            coll.add(first);
            whereRef.set(null);
        }
        coll.add(wherePredicate);
    }

    private static ParserPredicate predicateRelation(Consumer<FilterPredicate> where) {
        var op = new AtomicReference<ComparisonOpEnum>();
        var colRef2 = new AtomicReference<SelectClause>();
        return colRef(
                oneOfStrings(
                        List.of("=", ">", ">=", "<", "<=", "<>"),
                        colRef(
                                TRUE_PREDICATE,
                                colRef2::set
                        ),
                        s -> op.set(ComparisonOpEnum.parse(s))
                ), clause -> where.accept(FilterPredicate.of(clause, op.get(), colRef2.get()))
        );
    }

    private static ParserPredicate predicateValue(Consumer<FilterPredicate> where) {
        var op = new AtomicReference<ComparisonOpEnum>();
        var val = new AtomicReference<>();
        return colRef(
                oneOfStrings(
                        List.of("=", ">", ">=", "<", "<=", "<>"),
                        or(
                                quotedString(val::set),
                                number(val::set)
                        ), s -> op.set(ComparisonOpEnum.parse(s))),
                clause -> where.accept(FilterPredicate.of(clause, op.get(), val.get()))
        );
    }
}
