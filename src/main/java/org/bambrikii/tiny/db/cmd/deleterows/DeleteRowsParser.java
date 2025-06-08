package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.model.Filter;
import org.bambrikii.tiny.db.model.OperatorEnum;
import org.bambrikii.tiny.db.parser.predicates.ParserPredicate;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.TRUE_PREDICATE;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.chars;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.oneOfStrings;
import static org.bambrikii.tiny.db.parser.predicates.ParserFunctions.word;

public class DeleteRowsParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream input) {
        var cmd = new DeleteRowsCommand();
        return chars("delete", chars("from", (word(chars("where", cond(cmd::filter)), cmd::name)))).test(input)
                ? cmd
                : NO_COMMAND;
    }

    private ParserPredicate cond(Consumer<Filter> consumer) {
        var left = new AtomicReference<String>();
        var op = new AtomicReference<OperatorEnum>();
        return word(oneOfStrings(
                        OperatorEnum.sqlRepresentations(),
                        word(TRUE_PREDICATE, left::set),
                        s -> op.set(OperatorEnum.parse(s))
                ),
                s -> consumer.accept(new Filter(left.get(), op.get(), s))
        );
    }
}
