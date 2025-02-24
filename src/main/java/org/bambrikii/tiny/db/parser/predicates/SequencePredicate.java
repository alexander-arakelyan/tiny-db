package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class SequencePredicate implements ParserPredicate<String> {
    private final String s;
    private final ParserPredicate next;

    public SequencePredicate(String s, ParserPredicate next) {
        this.s = s;
        this.next = next;
    }

    @Override
    public boolean test(ParserInputStream input, String output) {
        var mark1 = input.pos();
        var ch = input.advance();
        int pos = 0;
        int len = s.length();
        while ((pos < len && ch == s.charAt(pos))) {
            ch = input.advance();
            pos++;
        }
        if (len != pos) {
            input.rollback(mark1);
            return false;
        }

        next.test(input, true);
        return true;
    }
}
