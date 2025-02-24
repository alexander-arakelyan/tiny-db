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
                    add(brackets(word((colInput, col) -> word((typeInput, type) -> {
                        var precision = new AtomicInteger(0);
                        var scale = new AtomicInteger(0);
                        var nullable = new AtomicBoolean();
                        var unique = new AtomicBoolean();
                        var res = order(
                                brackets(order(
                                        number(precision::set),
                                        optional(comma(number(scale::set)))
                                )),
                                unordered(
                                        optional(nullable(assignTrue(nullable::set))),
                                        optional(unique(assignTrue(unique::set)))
                                )
                        ).test(typeInput, null);
                        if (res) {
                            cmd.addColumn(col, type, precision.get(), scale.get(), nullable.get(), unique.get());
                        }
                        return res;
                    }).test(colInput, col)))),
                    drop(word(assignString(cmd::dropColumn)))
            ).test(input2, null);
        })))
                .test(input, null)
                ? cmd
                : NO_COMMAND;
    }
}
