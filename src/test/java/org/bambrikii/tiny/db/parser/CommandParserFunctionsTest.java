package org.bambrikii.tiny.db.parser;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.cmd.NavigableStreamReader;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableParser;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsParser;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CommandParserFunctionsTest {
    @SneakyThrows
    @Test
    void shouldParseCreateTable() {
        // given
        var ctx = new QueryExecutorContext();
        var parser = new CreateTableParser();
        try (var bais = new ByteArrayInputStream("create table table1 (col1 varchar, col2 number(11, 2) nullable)".getBytes(UTF_8));
             var is = new NavigableStreamReader(bais)) {

            // when
            var result = parser.parse(is);

            // then
            var res2 = result.exec(ctx);

            // then
            System.out.println(" " + result);
            System.out.println(" " + res2);
        }
    }

    @SneakyThrows
    @Test
    void shouldParseInsertRows() {
        // given
        var ctx = new QueryExecutorContext();
        var parser = new InsertRowsParser();
        try (var bais = new ByteArrayInputStream("insert into table1 (col1, col2, col3) values (1, 'val1', 'val2')".getBytes(UTF_8));
             var is = new NavigableStreamReader(bais)) {

            // when
            var result = parser.parse(is);

            // then
            var res2 = result.exec(ctx);

            // then
            System.out.println(" " + result);
            System.out.println(" " + res2);
        }
    }
}
