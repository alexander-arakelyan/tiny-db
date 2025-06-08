package org.bambrikii.tiny.db.cmd.insertrows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.brackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.chars;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.number;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.or;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.ordered;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.singleQuoted;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class InsertRowsParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream is) {
        var cmd = new InsertRowsCommand();
        return chars("insert", chars("into", word(
                        ordered(
                                brackets(atLeastOnceCommaSeparated(word(TRUE_PREDICATE, cmd::columnName))),
                                chars("values",
                                        brackets(atLeastOnceCommaSeparated(or(
                                                word(TRUE_PREDICATE, cmd::columnValue),
                                                singleQuoted(word(TRUE_PREDICATE, cmd::columnValue)),
                                                number(cmd::columnValue)
                                        )))
                                )
                        ),
                        cmd::table
                )
        )).test(is)
                ? cmd
                : NO_COMMAND;
    }
}
