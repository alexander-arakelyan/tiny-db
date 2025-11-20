package org.bambrikii.tiny.db.parser.functions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bambrikii.tiny.db.parser.utils.CharacterUtils;
import org.bambrikii.tiny.db.parser.predicates.CharsRangePredicate;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;

import java.util.List;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordFunctions {
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

    public static CharsRangePredicate wordWithAnyCharacters(ParserPredicate next, Consumer<String> consumer) {
        return new CharsRangePredicate(
                List.of(CharacterUtils::isAsciiLower, CharacterUtils::isAsciiUpper, CharacterUtils::isUnderscore),
                List.of(CharacterUtils::isDigit, CharacterUtils::isDash, CharacterUtils::isSlash),
                next, consumer
        );
    }
}
