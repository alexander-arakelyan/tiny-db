package org.bambrikii.tiny.db.parser.functions;

import org.bambrikii.tiny.db.parser.predicates.ChainPredicate;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;

import java.util.function.Consumer;

import static org.bambrikii.tiny.db.parser.functions.CharsFunctions.chars;
import static org.bambrikii.tiny.db.parser.functions.CharsFunctions.spaces;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.DEFAULT_STRING_CONSUMER;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.or;
import static org.bambrikii.tiny.db.parser.functions.WordFunctions.wordOnly;
import static org.bambrikii.tiny.db.parser.functions.WordFunctions.wordWithAnyCharacters;
import static org.bambrikii.tiny.db.parser.functions.WordFunctions.wordWithDashes;

public class QuotedFunctions {
    private QuotedFunctions() {
    }

    public static ParserPredicate singleQuoted(ParserPredicate next) {
        return spaces(chars("'", ChainPredicate.link(next, spaces(chars("'", DEFAULT_STRING_CONSUMER)))));
    }

    public static ParserPredicate quotedString(ParserPredicate next, Consumer<String> consumer) {
        return chars("'", wordOnly(chars("'", next), consumer));
    }

    public static ParserPredicate quotedString(Consumer<String> consumer) {
        return chars("'", wordWithDashes(chars("'", TRUE_PREDICATE), consumer));
    }

    public static ParserPredicate optionalDoubleQuotedString(ParserPredicate next, Consumer<String> consumer) {
        return or(
                spaces(chars("\"", wordWithAnyCharacters(chars("\"", next), consumer))),
                wordOnly(next, consumer)
        );
    }
}
