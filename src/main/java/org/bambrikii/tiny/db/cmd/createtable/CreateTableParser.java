package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.colDef;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.create;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.table;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.atLeastOnce;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.brackets;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class CreateTableParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream input) {
        var cmd = new CreateTableCommand();
        return create(table(word(brackets(atLeastOnce(colDef(cmd))), cmd::name))).test(input)
                ? cmd
                : NO_COMMAND;
    }
}
