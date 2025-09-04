package org.bambrikii.tiny.db.parser;

import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoMessage.NO_MESSAGE;
import static org.bambrikii.tiny.db.parser.predicates.WhereFunctions.wherePredicate;

class TestWherePredicate extends AbstractCommandParser {
    @Override
    public AbstractMessage parse(ParserInputStream nsr) {
        var cmd = new TestWhereMessage();
        return wherePredicate(cmd::setWhere).test(nsr)
                ? cmd
                : NO_MESSAGE;
    }
}
