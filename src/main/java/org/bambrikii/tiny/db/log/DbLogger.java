package org.bambrikii.tiny.db.log;

import org.bambrikii.tiny.db.cmd.ParserInputStream;

public class DbLogger {
    public static void log(Object obj, ParserInputStream input, String msg) {
        var clsName = obj == null
                ? null
                : obj.getClass().getName();
        System.out.println("Log: " + clsName + " " + input + " " + msg);
    }
}
