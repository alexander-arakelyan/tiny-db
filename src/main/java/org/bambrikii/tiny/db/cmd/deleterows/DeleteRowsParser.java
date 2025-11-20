package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.from;
import static org.bambrikii.tiny.db.parser.functions.CommandFunctions.where;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.chars;
import static org.bambrikii.tiny.db.parser.functions.CompositeFunctions.ordered;

public class DeleteRowsParser extends AbstractCommandParser<DeleteRowsMessage> {
    @Override
    public AbstractMessage parse(ParserInputStream input) {
        var cmd = new DeleteRowsMessage();
        return chars("delete",
                ordered(
                        from(cmd),
                        where(cmd)
                )
        ).test(input)
                ? cmd
                : NO_MESSAGE;
    }
}
