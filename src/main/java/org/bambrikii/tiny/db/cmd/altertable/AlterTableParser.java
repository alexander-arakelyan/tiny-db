package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.addCol;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.alter;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.alterCol;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.dropCol;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.table;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.brackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.or;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class AlterTableParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream input) {
        var cmd = new AlterTableCommand();
        return alter(table(
                word(brackets(atLeastOnceCommaSeparated(or(
                                dropCol(cmd),
                                alterCol(cmd),
                                addCol(cmd)
                        ))),
                        cmd::setName
                ))).test(input)
                ? cmd
                : NO_COMMAND;
    }

}
