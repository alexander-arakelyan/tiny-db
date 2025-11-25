package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.functions.BracketsFunctions.brackets;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.addCol;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.alter;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.alterCol;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.dropCol;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.table;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.or;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.word;

public class AlterTableParser extends AbstractCommandParser<AlterTableMessage> {
    @Override
    public AbstractMessage parse(ParserInputStream input) {
        var cmd = new AlterTableMessage();
        return alter(table(
                word(brackets(atLeastOnceCommaSeparated(or(
                                dropCol(cmd),
                                alterCol(cmd),
                                addCol(cmd)
                        ))),
                        cmd::name
                ))).test(input)
                ? cmd
                : NO_MESSAGE;
    }

}
