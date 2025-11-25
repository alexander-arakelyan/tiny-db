package org.bambrikii.tiny.db.cmd;

public class CommandParserUtils {
    private CommandParserUtils() {
    }

    static boolean digit(String str, int pos) {
        if (end(str, pos)) {
            return false;
        }
        char ch = str.charAt(pos);
        if (ch < '0' || ch > '9') {
            return false;
        }
        return true;
    }

    static boolean chr(String str, int pos) {
        if (end(str, pos)) {
            return false;
        }
        char ch = str.charAt(pos);
        if (ch < 'a' || ch > 'z') {
            return false;
        }
        return true;
    }

    static boolean delim(String str, int pos) {
        if (end(str, pos)) {
            return false;
        }
        char ch = str.charAt(pos);
        if (ch != ':') {
            return false;
        }
        return true;
    }

    static boolean end(String str, int pos) {
        return pos >= str.length();
    }
}
