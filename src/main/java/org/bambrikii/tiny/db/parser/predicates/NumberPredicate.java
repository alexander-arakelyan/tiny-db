package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class NumberPredicate implements ParserPredicate<String> {
    private final ParserPredicate<Integer> next;

    public NumberPredicate(ParserPredicate<Integer> next) {
        this.next = next;
    }

    @Override
    public boolean test(ParserInputStream input, String output) {
        var mark = input.pos();
        var ch = input.advance();
        int pos = 0;
        var val = 0;
        while (ch >= '0' && ch <= '9') {
            ch = input.advance();
            pos++;
            val = val * 10 + (ch - '0');
        }
        if (pos == 0) {
            input.rollback(mark);
            return false;
        }
        next.test(input, val);
        return true;
    }
}
