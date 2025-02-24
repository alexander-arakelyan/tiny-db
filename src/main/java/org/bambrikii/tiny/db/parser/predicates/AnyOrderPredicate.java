package org.bambrikii.tiny.db.parser.predicates;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AnyOrderPredicate implements ParserPredicate<String> {
    private final List<ParserPredicate<String>> next;

    public AnyOrderPredicate(ParserPredicate<String>... next) {
        this.next = Arrays.asList(next);
    }

    @Override
    public boolean test(ParserInputStream input, String output) {
        var left = new LinkedList<>(next);
        var right = new LinkedList<ParserPredicate<String>>();
        int count;
        do {
            count = 0;
            while (!left.isEmpty()) {
                var popped = left.pop();
                if (popped.test(input, null)) {
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
