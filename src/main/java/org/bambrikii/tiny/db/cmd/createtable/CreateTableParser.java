package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;

public class CreateTableParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(NavigableStreamReader nsr) {
        if (nsr.readWord("create") && nsr.readWord("table")) {
            var cmd = new CreateTableCommand();
            var name = nsr.readWord();
            if (name != null) {
                cmd.name(name);
                if (nsr.readString("(")) {
                    var colName = nsr.readWord();
                    if (colName != null) {
                        var type = nsr.readType();
                        if (type != null) {
                            var nullable = nsr.readBoolean(false);
                            var unique = nsr.readBoolean(false);
                            cmd.column(colName, type, nullable, unique);
                            if (nsr.readString(")") && nsr.readString(";")) {
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
