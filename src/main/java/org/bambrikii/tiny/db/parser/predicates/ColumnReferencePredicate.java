package org.bambrikii.tiny.db.parser.predicates;

import java.util.List;
import java.util.function.Consumer;

public class ColumnReferencePredicate extends CharsRangePredicate {
    public ColumnReferencePredicate(ParserPredicate next, Consumer<String> consumer) {
        super(
                List.of(CharacterUtils::isAsciiLower, CharacterUtils::isAsciiUpper, CharacterUtils::isUnderscore, CharacterUtils::isDot),
                List.of(CharacterUtils::isDigit),
                next, consumer
        );
    }
}
