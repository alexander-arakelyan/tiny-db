package org.bambrikii.tiny.db.cmd.deleterows;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class DeleteRowsParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream input) {
        var cmd = new DeleteRowsCommand();
//        return chars("delete", chars("from", (word(tableNameInput -> {
//            var left1 = new AtomicReference<String>();
//            var operator1 = new AtomicReference<String>();
//            var right1 = new AtomicReference<String>();
//            return chars("where", cond(
//                    (s, s2, s3) -> {
//                        left1.set(s);
//                        operator1.set(s2);
//                        right1.set(s3);
//                    }
//            )),
//            vak -> cmd.filter(left1.get(), operator1.get(), right1.get())
//            }
//            return false;
//        })),cmd::name).test(input, null) ? cmd : NO_COMMAND;
        return cmd;
    }
}
