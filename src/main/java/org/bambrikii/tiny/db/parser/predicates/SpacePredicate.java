package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class SpacePredicate implements ParserPredicate<String> {
    private final ParserPredicate<String> next;

    public SpacePredicate(ParserPredicate<String> next) {
        this.next = next;
    }

    @Override
    public boolean test(ParserInputStream input, String output) {
        var mark = input.pos();
        var ch = input.advance();
        while ((ch == ' ')) {
            ch = input.advance();
        }
        var res = next.test(input, output);
        if (res) {
            return true;
        }

        input.rollback(mark);
        return false;
    }
}
