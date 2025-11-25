package org.bambrikii.tiny.db.cmd.selectrows;

import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.from;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.joins;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.select;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.where;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.ordered;

public class SelectRowsParser extends AbstractCommandParser<SelectRowsMessage> {
    @Override
    public AbstractMessage parse(ParserInputStream is) {
        var cmd = new SelectRowsMessage();
        return ordered(
                select(cmd),
                from(cmd),
                joins(cmd),
                where(cmd)
        ).test(is)
                ? cmd
                : NO_MESSAGE;
    }
}
