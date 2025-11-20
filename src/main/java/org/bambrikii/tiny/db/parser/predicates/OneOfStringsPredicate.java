package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.List;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.chars;

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
        for (var str : strings) {
            if (chars(str, next, consumer).test(is)) {
                return true;
            }
        }
        return false;
    }
}
