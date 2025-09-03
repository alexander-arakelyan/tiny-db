package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.from;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.where;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.chars;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.ordered;

public class DeleteRowsParser extends AbstractCommandParser {
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
