package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AnyOrderPredicate extends ParserPredicate {
    private final List<ParserPredicate> next;

    public AnyOrderPredicate(ParserPredicate... next) {
        this.next = Arrays.asList(next);
    }

    @Override
    public boolean doTest(ParserInputStream is) {
        var left = new LinkedList<>(next);
        var right = new LinkedList<ParserPredicate>();
        int count;
        do {
            count = 0;
            while (!left.isEmpty()) {
                var popped = left.pop();
                if (popped.test(is)) {
                    count++;
                } else {
                    right.push(popped);
                }
            }
            var tmp = left;
            left = right;
            right = tmp;
        } while (count > 0);

        return left.isEmpty();
    }
}
