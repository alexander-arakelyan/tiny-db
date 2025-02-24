package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.Arrays;

public class AndPredicate implements ParserPredicate<String> {
    private final ParserPredicate<String>[] next;

    public AndPredicate(ParserPredicate<String>... next) {
        this.next = next;
    }

    @Override
    public boolean test(ParserInputStream input, String output) {
        return Arrays
                .stream(next)
                .allMatch(next1 -> next1.test(input, output));
    }
}
