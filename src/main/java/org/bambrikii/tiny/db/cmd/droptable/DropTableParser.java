package org.bambrikii.tiny.db.cmd.droptable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.drop;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.table;
import static org.bambrikii.tiny.db.parser.ParserFunctions.word;

public class DropTableParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream input) {
        var cmd = new DropTableCommand();
        return drop(table(word((tableNameInput, tableName) -> {
            cmd.name(tableName);
            return true;
        }))).test(input, null) ? cmd : NO_COMMAND;
    }
}
