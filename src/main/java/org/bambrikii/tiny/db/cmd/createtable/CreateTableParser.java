package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.colDef;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.create;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.table;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.atLeastOnceCommaSeparated;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.brackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class CreateTableParser extends AbstractCommandParser {
    @Override
    public AbstractMessage parse(ParserInputStream input) {
        var cmd = new CreateTableMessage();
        return create(table(word(brackets(atLeastOnceCommaSeparated(colDef(cmd))), cmd::name))).test(input)
                ? cmd
                : NO_MESSAGE;
    }
}
