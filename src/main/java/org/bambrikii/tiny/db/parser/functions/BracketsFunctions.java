package org.bambrikii.tiny.db.parser.functions;

import org.bambrikii.tiny.db.parser.predicates.ChainPredicate;
import org.bambrikii.tiny.db.parser.predicates.ConstantResultPredicate;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;

import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.functions.CharsFunctions.chars;
import static org.bambrikii.tiny.db.parser.functions.CharsFunctions.spaces;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.DEFAULT_STRING_CONSUMER;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.or;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.ordered;

public class BracketsFunctions {
    private BracketsFunctions() {
    }


    public static ParserPredicate brackets(ParserPredicate next) {
        return ordered(
                spaces(chars("(", next)),
                spaces(chars(")", DEFAULT_STRING_CONSUMER))
        );
    }

    public static ParserPredicate optionalBrackets(ParserPredicate next, Consumer<Boolean> bracketsConsumer) {
        return or(
                spaces(chars("(", ChainPredicate.link(
                        next,
                        spaces(chars(")", ConstantResultPredicate.of(TRUE_PREDICATE, bracketsConsumer, false)))
                ))),
                ConstantResultPredicate.of(next, bracketsConsumer, false)
        );
    }
}
