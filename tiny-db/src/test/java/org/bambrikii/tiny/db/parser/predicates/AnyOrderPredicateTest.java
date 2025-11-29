package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.NavigableStreamReader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.FALSE_PREDICATE;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.TRUE_PREDICATE;

class AnyOrderPredicateTest {
    public static Stream<Arguments> args() {
        return Stream.of(
                Arguments.of("", new ParserPredicate[]{TRUE_PREDICATE, TRUE_PREDICATE, TRUE_PREDICATE}, true),
                Arguments.of("", new ParserPredicate[]{TRUE_PREDICATE, TRUE_PREDICATE, FALSE_PREDICATE}, false),
                Arguments.of("", new ParserPredicate[]{TRUE_PREDICATE, FALSE_PREDICATE, FALSE_PREDICATE}, false),
                Arguments.of("", new ParserPredicate[]{FALSE_PREDICATE, TRUE_PREDICATE, TRUE_PREDICATE}, false),
                Arguments.of("", new ParserPredicate[]{FALSE_PREDICATE, FALSE_PREDICATE, TRUE_PREDICATE}, false),
                Arguments.of("", new ParserPredicate[]{FALSE_PREDICATE, FALSE_PREDICATE, FALSE_PREDICATE}, false)
        );
    }

    @ParameterizedTest
    @MethodSource("args")
    void shouldTest(String str, ParserPredicate[] predicates, boolean expected) throws IOException {
        // given
        try (
                var bis = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
                var is = new NavigableStreamReader(bis);
        ) {
            // when
            var actual = new AnyOrderPredicate(predicates).test(is);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }
}
