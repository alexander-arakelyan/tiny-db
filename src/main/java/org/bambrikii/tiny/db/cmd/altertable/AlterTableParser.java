package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.*;
import static org.bambrikii.tiny.db.parser.ParserFunctions.unordered;
import static org.bambrikii.tiny.db.parser.ParserFunctions.word;

public class AlterTableParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream input) {
        var cmd = new AlterTableCommand();
        return alter(table(word((tableNameInput, tableName) -> {
            cmd.setName(tableName);
            return unordered(
                    parseAddColumn(cmd),
                    parseDropColumn(cmd)
            ).test(tableNameInput, null);
        }))).test(input, null) ? cmd : NO_COMMAND;
    }

}
