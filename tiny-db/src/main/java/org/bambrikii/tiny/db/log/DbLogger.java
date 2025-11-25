package org.bambrikii.tiny.db.log;

import org.bambrikii.tiny.db.cmd.ParserInputStream;
import org.bambrikii.tiny.db.model.Row;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DbLogger {
    public static void log(Object obj, ParserInputStream input, String msg) {
        var clsName = obj == null
                ? null
                : obj.getClass().getName();
        System.out.println("Log: " + clsName + " " + input + " ? " + msg);
    }

    public static void log(Object obj, String msg, Object... params) {
        var clsName = obj == null
                ? null
                : obj instanceof Class
                ? ((Class<?>) obj).getName()
                : obj.getClass().getName();
        System.out.println("Log: " + clsName + " " + String.format(msg, params));
    }

    public static void print(Row row, String... cols) {
        System.out.printf(
                "Row: rowId = %s, isDeleted = %s, %s%n",
                row.getRowId(),
                row.isDeleted(), Arrays
                        .stream(cols)
                        .map(col -> {
                                    var t = col.split("\\.");
                                    return row.read(t[0], t[1]);
                                }
                        ).collect(Collectors.toList()));
    }
}
