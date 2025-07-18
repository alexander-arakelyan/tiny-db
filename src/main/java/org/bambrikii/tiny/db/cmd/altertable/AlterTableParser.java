package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.addCol;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.alter;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.alterCol;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.dropCol;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.table;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.brackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.or;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class AlterTableParser extends AbstractCommandParser {
    @Override
    public AbstractMessage parse(ParserInputStream input) {
        var cmd = new AlterTableMessage();
        return alter(table(
                word(brackets(atLeastOnceCommaSeparated(or(
                                dropCol(cmd),
                                alterCol(cmd),
                                addCol(cmd)
                        ))),
                        cmd::setName
                ))).test(input)
                ? cmd
                : NO_MESSAGE;
    }

}
