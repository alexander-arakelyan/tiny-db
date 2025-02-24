package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.parser.CommandParserFunctions;
import org.bambrikii.tiny.db.parser.TriConsumer;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.nio.file.Files.delete;
import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.*;
import static org.bambrikii.tiny.db.parser.ParserFunctions.any;
import static org.bambrikii.tiny.db.parser.ParserFunctions.word;

public class DeleteRowsParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream input) {
        var cmd = new DeleteRowsCommand();
        return CommandParserFunctions.delete(from(word((tableNameInput, tableName) -> {
            var mark = tableNameInput.pos();
            cmd.name(tableName);
            var left1 = new AtomicReference<String>();
            var operator1 = new AtomicReference<String>();
            var right1 = new AtomicReference<String>();
            var res = where(cond(
                    (s, s2, s3) -> {
                        left1.set(s);
                        operator1.set(s2);
                        right1.set(s3);
                    }
            )).test(tableNameInput, tableName);
            if (res) {
                cmd.filter(left1.get(), operator1.get(), right1.get());
                return true;
            }
            tableNameInput.rollback(mark);
            return false;
        }))).test(input, null) ? cmd : NO_COMMAND;
    }
}
