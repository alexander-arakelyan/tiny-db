package org.bambrikii.tiny.db.parser.predicates;

public class CharacterUtils {
    private CharacterUtils() {
    }

    public static boolean isDigit(byte ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isUnderscore(byte ch) {
        return ch == '_';
    }

    public static boolean isDot(byte ch) {
        return ch == '.';
    }

    public static boolean isAsciiUpper(byte ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public static boolean isAsciiLower(byte ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public static boolean isDash(byte ch) {
        return ch == '-';
    }
}
