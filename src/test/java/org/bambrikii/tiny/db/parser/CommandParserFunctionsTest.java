package org.bambrikii.tiny.db.parser;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.cmd.NavigableStreamReader;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableParser;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsParser;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

@ExtendWith(MockitoExtension.class)
class CommandParserFunctionsTest {
    @SneakyThrows
    @Test
    void shouldParseCreateTable() {
        // given
        var parser = new CreateTableParser();
        try (var bais = new ByteArrayInputStream("create table table1 (col1 varchar, col2 number(11, 2) nullable)".getBytes(UTF_8));
             var is = new NavigableStreamReader(bais)) {

            // when
            var result = parser.parse(is);

            // then
            System.out.println(" " + result);
        }
    }

    @SneakyThrows
    @Test
    void shouldParseInsertRows() {
        // given
        var parser = new InsertRowsParser();
        try (var bais = new ByteArrayInputStream("insert into table1 (col1, col2, col3) values (1, 'val1', 'val2')".getBytes(UTF_8));
             var is = new NavigableStreamReader(bais)) {

            // when
            var result = parser.parse(is);

            // then
            System.out.println(" " + result);
        }
    }

    @SneakyThrows
    @Test
    void shouldParseSelectRows() {
        // given
        var parser = new SelectRowsParser();
        try (var bais = new ByteArrayInputStream("select col1, col2, col3 from table1 where col4 = col5".getBytes(UTF_8));
             var is = new NavigableStreamReader(bais)) {

            // when
            var result = parser.parse(is);

            // then
            System.out.println(" " + result);
        }
    }

    @SneakyThrows
    @Test
    void shouldParseSelectRowsWithJoins() {
        // given
        var parser = new SelectRowsParser();
        try (var bais = new ByteArrayInputStream(("select t1.col1, col2, col3" +
                " from table1 t1" +
                " inner join table2 t2 on table1.col2 = table2.col2" +
                " left join table3 t3 on table2.col3 = t3.col3" +
                " where col4 = col5"
        ).getBytes(UTF_8));
             var is = new NavigableStreamReader(bais)) {

            // when
            var result = parser.parse(is);

            // then
            System.out.println(" " + result);
        }
    }
}
