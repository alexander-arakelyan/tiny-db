package org.bambrikii.tiny.db.cmd.selectrows;

import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.concurrent.atomic.AtomicReference;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.from;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.joins;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.select;
import static org.bambrikii.tiny.db.parser.impl.CommandParserFunctions.where;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.ordered;

public class SelectRowsParser extends AbstractCommandParser {
    @Override
    public AbstractMessage parse(ParserInputStream is) {
        var cmd = new SelectRowsMessage();
        var joinTableNameRef = new AtomicReference<String>();
        var joinTableAliasRef = new AtomicReference<String>();
        return ordered(
                select(cmd),
                from(cmd, joinTableAliasRef),
                joins(cmd, joinTableNameRef, joinTableAliasRef),
                where(cmd)
        ).test(is)
                ? cmd
                : NO_MESSAGE;
    }
}
