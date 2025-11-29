package org.bambrikii.tiny.db.parser.functions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.LogicalOpEnum;
import org.bambrikii.tiny.db.model.clauses.SelectClause;
import org.bambrikii.tiny.db.model.nodes.AndNode;
import org.bambrikii.tiny.db.model.nodes.FilterByValueNode;
import org.bambrikii.tiny.db.model.nodes.JoinNode;
import org.bambrikii.tiny.db.model.nodes.OrNode;
import org.bambrikii.tiny.db.model.nodes.WhereNode;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.functions.BracketsFunctions.optionalBrackets;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.colRef;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.oneOfStrings;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.or;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.ordered;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.times;
import static org.bambrikii.tiny.db.parser.functions.NumberFunctions.number;
import static org.bambrikii.tiny.db.parser.functions.QuotedFunctions.quotedString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WhereFunctions {
    public static ParserPredicate wherePredicate(Consumer<WhereNode> where) {
        var whereRef = new AtomicReference<WhereNode>();
        var ands = new ArrayList<WhereNode>();
        var ors = new ArrayList<WhereNode>();
        var nextWhereRef = new AtomicReference<WhereNode>();
        var debugCounter = new AtomicInteger();
        return optionalBrackets(ordered(
                basicPredicate(whereRef),
                times(oneOfStrings(
                        List.of("and", "or"),
                        or(
                                basicPredicate(nextWhereRef),
                                new ParserPredicate() {
                                    @Override
                                    protected boolean doTest(ParserInputStream is) {
                                        return wherePredicate(nextWhereRef::set).test(is);
                                    }
                                }
                        ), s -> {
                            var n = debugCounter.incrementAndGet();
                            switch (Objects.requireNonNull(LogicalOpEnum.parse(s))) {
                                case AND:
                                    DbLogger.log(whereRef.get(), "Predicate: %s AND %s %s %d", whereRef.get(), nextWhereRef.get(), ands, n);
                                    mergeWhereRefsTo(ands, whereRef, nextWhereRef);
                                    break;
                                case OR:
                                    DbLogger.log(whereRef.get(), "Predicate: %s OR %s %s %d", whereRef.get(), nextWhereRef.get(), ors, n);
                                    mergeWhereRefsTo(ors, whereRef, nextWhereRef);
                                    break;
                            }
                        }
                ))
        ), withBrackets -> {
            var n = debugCounter.get();
            if (ands.isEmpty() && ors.isEmpty()) {
                DbLogger.log(whereRef.get(), "Predicate: EMPTY %s %d", whereRef.get(), n);
                where.accept(whereRef.get());
            } else if (ands.isEmpty()) {
                DbLogger.log(whereRef.get(), "Predicate: ORS %s %s %d", whereRef.get(), ors, n);
                where.accept(OrNode.of(ors));
            } else if (ors.isEmpty()) {
                DbLogger.log(whereRef.get(), "Predicate: ANDS %s %s %d", whereRef.get(), ands, n);
                where.accept(AndNode.of(ands));
            } else {
                DbLogger.log(whereRef.get(), "Predicate: ANDS+ORS %s %s %s %d", whereRef.get(), ands, ors, n);
                where.accept(AndNode.of(ands).and(OrNode.of(ors)));
            }
        });
    }

    private static ParserPredicate basicPredicate(AtomicReference<WhereNode> whereRef) {
        return or(
                predicateRelation(whereRef::set),
                predicateValue(whereRef::set)
        );
    }

    private static void mergeWhereRefsTo(
            List<WhereNode> coll,
            AtomicReference<WhereNode> whereRef,
            AtomicReference<WhereNode> whereNode
    ) {
        var first = whereRef.get();
        if (first != null) {
            coll.add(first);
            whereRef.set(null);
        }
        coll.add(whereNode.get());
    }

    private static ParserPredicate predicateRelation(Consumer<JoinNode> where) {
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
                ), clause -> where.accept(JoinNode.of(clause, op.get(), colRef2.get()))
        );
    }

    private static ParserPredicate predicateValue(Consumer<FilterByValueNode> where) {
        var op = new AtomicReference<ComparisonOpEnum>();
        var v = new AtomicReference<>();
        return colRef(
                oneOfStrings(
                        List.of("=", ">", ">=", "<", "<=", "<>"),
                        or(
                                quotedString(v::set),
                                number(v::set)
                        ), s -> op.set(ComparisonOpEnum.parse(s))),
                clause -> where.accept(FilterByValueNode.of(clause, op.get(), v.get()))
        );
    }
}
