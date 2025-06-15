package org.bambrikii.tiny.db.parser.predicates;

import java.util.List;
import java.util.function.Consumer;

public class WordPredicate extends CharsRangePredicate {
    public WordPredicate(ParserPredicate next, Consumer<String> consumer) {
        super(
                List.of(CharacterUtils::isAsciiLower, CharacterUtils::isAsciiUpper, CharacterUtils::isUnderscore),
                List.of(CharacterUtils::isDigit),
                next, consumer
        );
    }
}
