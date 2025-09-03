package org.bambrikii.tiny.db.parser;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.NavigableStreamReader;
import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableParser;
import org.bambrikii.tiny.db.model.ComparisonOpEnum;
import org.bambrikii.tiny.db.model.LogicalOpEnum;
import org.bambrikii.tiny.db.model.select.SelectClause;
import org.bambrikii.tiny.db.model.select.WherePredicate;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.colRef;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.quotedString;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.number;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.oneOfStrings;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.optional;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.optionalBrackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.or;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.ordered;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.times;

class ConditionParserTest {
    @SneakyThrows
    @Test
    void shouldParse() {
        // given
        var parser = new CreateTableParser();
        try (var bais = new ByteArrayInputStream("t1.col1 = t2.col2 and (t3.col3 = t4.col4 or t5.col5 = t6.col6) or t7.col7 = 1 and t8.col8 = 'some-str'".getBytes(UTF_8));
             var is = new NavigableStreamReader(bais)) {

            // when
            var predicate = new WherePredicate();
            var result = new TestPredicate().parse(is);

            // then
            System.out.println(" " + result);
            assertThat(result).isInstanceOf(TestMessage.class);
        }
    }

    static class TestPredicate extends AbstractCommandParser {
        @Override
        public AbstractMessage parse(ParserInputStream nsr) {
            var cmd = new TestMessage();
            return nestedPredicate(cmd::setWhere).test(nsr)
                    ? cmd
                    : NO_MESSAGE;
        }

        private ParserPredicate nestedPredicate(Consumer<WherePredicate> where) {
            var whereRef = new AtomicReference<WherePredicate>();
            var nextWhereRef = new AtomicReference<WherePredicate>();
            return optionalBrackets(ordered(
                    or(
                            predicateRelation(whereRef::set),
                            predicateValue(whereRef::set)
                    ), optional(
                            times(
                                    oneOfStrings(
                                            List.of("and", "or"),
                                            new ParserPredicate() {
                                                @Override
                                                protected boolean doTest(ParserInputStream is) {
                                                    return nestedPredicate(nextWhereRef::set).test(is);
                                                }
                                            }, s -> {
                                                var op = LogicalOpEnum.parse(s);
                                                if (op == LogicalOpEnum.AND) {
                                                    whereRef.get().setAnd(nextWhereRef.get());
                                                } else {
                                                    whereRef.get().setOr(nextWhereRef.get());
                                                }
                                            })
                            )
                    )
            ), withBrackets -> where.accept(whereRef.get()));
        }

        private static ParserPredicate predicateRelation(Consumer<WherePredicate> where) {
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
                    ), clause -> where.accept(WherePredicate.of(clause, op.get(), colRef2.get()))
            );
        }

        private static ParserPredicate predicateValue(Consumer<WherePredicate> where) {
            var op = new AtomicReference<ComparisonOpEnum>();
            var val = new AtomicReference<>();
            return colRef(
                    oneOfStrings(
                            List.of("=", ">", ">=", "<", "<=", "<>"),
                            or(
                                    quotedString(val::set),
                                    number(val::set)
                            ), s -> op.set(ComparisonOpEnum.parse(s))),
                    clause -> where.accept(WherePredicate.of(clause, op.get(), val.get()))
            );
        }
    }

    @ToString
    @Setter
    static class TestMessage implements AbstractMessage {
        private WherePredicate where;
    }
}
