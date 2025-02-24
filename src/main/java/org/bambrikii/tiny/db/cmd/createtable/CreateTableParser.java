package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.*;
import static org.bambrikii.tiny.db.parser.ParserFunctions.any;
import static org.bambrikii.tiny.db.parser.ParserFunctions.word;

public class CreateTableParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream input) {
        var cmd = new CreateTableCommand();
        return create(table(word((tableNameInput, tableName) -> any(
                        parseAddColumn(cmd)
                ).test(tableNameInput, null)
        ))).test(input, null) ? cmd : NO_COMMAND;
    }
}
