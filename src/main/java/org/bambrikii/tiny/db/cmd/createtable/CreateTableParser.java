package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.colDef;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.create;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.table;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.brackets;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.word;

public class CreateTableParser extends AbstractCommandParser<CreateTableMessage> {
    @Override
    public AbstractMessage parse(ParserInputStream input) {
        var cmd = new CreateTableMessage();
        return create(table(word(brackets(atLeastOnceCommaSeparated(colDef(cmd))), cmd::name))).test(input)
                ? cmd
                : NO_MESSAGE;
    }
}
