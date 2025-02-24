package org.bambrikii.tiny.db.cmd.altertable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.ParserInputStream;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.bambrikii.tiny.db.cmd.none.NoCommand.NO_COMMAND;
import static org.bambrikii.tiny.db.parser.CommandParserFunctions.*;
import static org.bambrikii.tiny.db.parser.ParserFunctions.*;

public class AlterTableParser extends AbstractCommandParser {
    @Override
    public AbstractCommand parse(ParserInputStream input) {
        var cmd = new AlterTableCommand();
        return alter(table(word((input2, output) -> {
            cmd.setName(output);
            return unordered(
                    add(brackets(word((colInput, col) -> {
                        var colMark = colInput.pos();
                        var res = word((typeInput, type) -> {
                            var typeMark = typeInput.pos();
                            var precision = new AtomicInteger(0);
                            var scale = new AtomicInteger(0);
                            var nullable = new AtomicBoolean(false);
                            var unique = new AtomicBoolean(false);
                            var typeRes = order(
                                    brackets(order(
                                            number(precision::set),
                                            optional(comma(number(scale::set)))
                                    )),
                                    unordered(
                                            optional(nullable(nullable::set)),
                                            optional(unique(unique::set))
                                    )).test(typeInput, null);
                            if (typeRes) {
                                cmd.addColumn(col, type, precision.get(), scale.get(), nullable.get(), unique.get());
                            } else {
                                typeInput.rollback(typeMark);
                            }
                            return typeRes;
                        }).test(colInput, col);
                        if (!res) {
                            colInput.rollback(colMark);
                        }
                        return res;
                    }))),
                    drop(word(assignString(cmd::dropColumn)))
            ).test(input2, null);
        }))).test(input, null) ? cmd : NO_COMMAND;
    }
}
