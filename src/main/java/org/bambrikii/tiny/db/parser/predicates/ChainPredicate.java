package org.bambrikii.tiny.db.parser.predicates;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

@RequiredArgsConstructor
public class ChainPredicate extends ParserPredicate {
    private final ParserPredicate prev;
    private final ParserPredicate next;

    @Override
    protected boolean doTest(ParserInputStream is) {
        return prev.test(is) && next.test(is);
    }

    public static ChainPredicate link(ParserPredicate prev, ParserPredicate next) {
        return new ChainPredicate(prev, next);
    }
}
