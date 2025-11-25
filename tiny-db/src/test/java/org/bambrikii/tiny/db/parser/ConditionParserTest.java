package org.bambrikii.tiny.db.parser;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.cmd.NavigableStreamReader;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

class ConditionParserTest {
    public static Stream<Arguments> conditions() {
        return Stream.of(
                Arguments.of("t1.col1 = t2.col2 and t3.col3 = t4.col4"),
                Arguments.of("t1.col1 = t2.col2 or t3.col3 = t4.col4"),
                Arguments.of("t1.col1 = 1 or t3.col3 = 'some-str'"),
                Arguments.of("t1.col1 = t1.col2 or t2.col2 = 1 and t3.col3 = 'some-str'"),
                Arguments.of("t1.col1 = t2.col2 and (t3.col3 = t4.col4 or t5.col5 = t6.col6) or t7.col7 = 1 and t8.col8 = 'some-str'"),
                Arguments.of("t1.col1 = t2.col2 and (t3.col3 = t4.col4 or t5.col5 = t6.col6) or t7.col7 = 1 and t8.col8 = 'some-str'"),
                Arguments.of("t1.col1 = t2.col2 and t3.col3 = t4.col4 or t5.col5 = t6.col6 or t7.col7 = 1 and t8.col8 = 'some-str'")
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("conditions")
    void shouldParse(String cond) {
        // given
        var parser = new CreateTableParser();
        try (var bais = new ByteArrayInputStream(cond.getBytes(UTF_8));
             var is = new NavigableStreamReader(bais)) {

            // when
            var result = new TestWherePredicate().parse(is);

            // then
            System.out.println(" " + result);
            assertThat(result).isInstanceOf(TestWhereMessage.class);
        }
    }
}
