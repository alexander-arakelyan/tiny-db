package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class OptionalPredicate implements ParserPredicate<String> {
    private final ParserPredicate<String> next;

    public OptionalPredicate(ParserPredicate<String> next) {
        this.next = next;
    }

    @Override
    public boolean test(ParserInputStream input, String output) {
        next.test(input, output);
        return true;
    }
}
