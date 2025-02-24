package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;

public class CreateTableParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream nsr) {
        if (nsr.word("create") && nsr.word("table")) {
            var cmd = new CreateTableCommand();
            var name = nsr.word();
            if (name != null) {
                cmd.name(name);
                if (nsr.string("(")) {
                    var colName = nsr.word();
                    if (colName != null) {
                        var type = nsr.type();
                        if (type != null) {
                            var nullable = nsr.bool(false);
                            var unique = nsr.bool(false);
                            cmd.addColumn(colName, type, nullable, unique);
                            if (nsr.string(")") && nsr.string(";")) {
                                return cmd;
                            }
                        }
                    }
                }
            }
        }
        return NO_COMMAND;
    }
}
