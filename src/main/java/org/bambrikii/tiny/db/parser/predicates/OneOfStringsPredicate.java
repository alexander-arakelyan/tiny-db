package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.chars;

public class OneOfStringsPredicate extends ParserPredicate {
    private final ParserPredicate next;
    private final Consumer<String> consumer;
    private final List<String> strings;

    public OneOfStringsPredicate(List<String> strings, ParserPredicate next, Consumer<String> consumer) {
        this.next = next;
        this.strings = strings;
        this.consumer = consumer;
    }

    @Override
    protected boolean doTest(ParserInputStream is) {
        var start = is.pos();
        var ops = new ArrayList<ParserPredicate>();
        for (String str : strings) {
            if (chars(str, next).test(is)) {
                var bytes = is.bytes(start, is.pos());
                consumer.accept(new String(bytes, StandardCharsets.UTF_8));
                return true;
            }
        }
        return false;
    }
}
