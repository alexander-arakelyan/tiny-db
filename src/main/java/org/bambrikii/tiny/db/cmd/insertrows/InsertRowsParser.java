package org.bambrikii.tiny.db.cmd.insertrows;

import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.from;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.where;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.brackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.chars;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.number;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.or;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.ordered;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.singleQuoted;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class InsertRowsParser extends AbstractCommandParser<InsertRowsMessage> {
    @Override
    public AbstractMessage parse(ParserInputStream is) {
        var colsByIndex = new ArrayList<String>();
        var valPos = new AtomicInteger(0);
        var cmd = new InsertRowsMessage();
        return chars("insert", chars("into", word(
                        or(
                                ordered(
                                        brackets(atLeastOnceCommaSeparated(word(TRUE_PREDICATE, colsByIndex::add))),
                                        chars("values",
                                                brackets(atLeastOnceCommaSeparated(or(
                                                        word(TRUE_PREDICATE, buildValSetter(cmd, colsByIndex, valPos)),
                                                        singleQuoted(word(TRUE_PREDICATE, buildValSetter(cmd, colsByIndex, valPos))),
                                                        number(buildValSetter(cmd, colsByIndex, valPos))
                                                )))
                                        )
                                ),
                                ordered(
                                        from(cmd),
                                        where(cmd)
                                )
                        ),
                        cmd::into
                )
        )).test(is)
                ? cmd
                : NO_MESSAGE;
    }

    public static <T> Consumer<T> buildValSetter(InsertRowsMessage cmd, List<String> colsByIndex, AtomicInteger valPos) {
        return val -> cmd.columnValue(colsByIndex.get(valPos.getAndIncrement()), val);
    }
}
