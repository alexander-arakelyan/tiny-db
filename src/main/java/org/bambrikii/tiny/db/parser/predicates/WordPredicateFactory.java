package org.bambrikii.tiny.db.parser.predicates;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordPredicateFactory {
    public static CharsRangePredicate word(ParserPredicate next, Consumer<String> consumer) {
        return new CharsRangePredicate(
                List.of(CharacterUtils::isAsciiLower, CharacterUtils::isAsciiUpper, CharacterUtils::isUnderscore),
                List.of(CharacterUtils::isDigit),
                next, consumer
        );
    }

    public static CharsRangePredicate wordWithDashes(ParserPredicate next, Consumer<String> consumer) {
        return new CharsRangePredicate(
                List.of(CharacterUtils::isAsciiLower, CharacterUtils::isAsciiUpper, CharacterUtils::isUnderscore),
                List.of(CharacterUtils::isDigit, CharacterUtils::isDash),
                next, consumer
        );
    }
}
