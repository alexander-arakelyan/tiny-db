package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class AnyWordPredicate implements ParserPredicate<String> {
    private final ParserPredicate<String> next;

    public AnyWordPredicate(ParserPredicate<String> next) {
        this.next = next;
    }

    @Override
    public boolean test(ParserInputStream input, String output) {
        var mark1 = input.pos();
        var ch = input.advance();
        if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_'))) {
            input.rollback(mark1);
            return false;
        }
        do {
            ch = input.advance();
        } while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_') || (ch >= '0' && ch <= '9'));
        long mark2 = input.pos();
        if (mark2 - mark1 == 0) {
            input.rollback(mark1);
            return false;
        }
        var str = input.string(mark1, mark2);
        next.test(input, str);
        return true;
    }
}
