package org.bambrikii.tiny.db.parser.predicates;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ConstantResultPredicate<T> extends ParserPredicate {
    private final ParserPredicate next;
    private final Consumer<T> conditionMet;
    private final T value;

    @Override
    protected boolean doTest(ParserInputStream is) {
        var res = next.test(is);
        if (res) {
            conditionMet.accept(value);
        }
        return res;
    }
}
