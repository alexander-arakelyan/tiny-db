package org.bambrikii.tiny.db.cmd;

import org.bambrikii.tiny.db.Command;

import java.util.ArrayList;
import java.util.List;

import static org.bambrikii.tiny.db.cmd.CommandParserUtils.*;

public class CommandParser {
    private int pos = 0;
    private String ver = "v0";
    private String cmd = "noop";
    private List<String> args = new ArrayList<>();

    private void reset() {
        pos = 0;
        ver = "v0";
        cmd = "noop";
        args.clear();
    }

    public Command parse(String str) {
        reset();

        int pos0 = this.pos;
        int pos = pos0;

        if (!ver(str, pos)) {
            this.pos = pos;
            return null;
        }
        pos = this.pos;

        if (!delim(str, pos++)) {
            return null;
        }

        if (!cmd(str, pos++)) {
            return null;
        }

        pos = this.pos;

        if (!args(str, pos++)) {
            return null;
        }
        return new Command(ver, this.cmd, args);
    }

    private boolean ver(String str, int pos) {
        int pos0 = pos;
        if (end(str, pos)) {
            return false;
        }
        if (str.charAt(pos++) != 'v') {
            return false;
        }
        if (!digit(str, pos++)) {
            return false;
        }
        while (digit(str, pos)) {
            pos++;
        }
        this.pos = pos;
        this.ver = str.substring(pos0, pos);

        return true;
    }

    private boolean cmd(String str, int pos) {
        int pos0 = pos;
        if (!chr(str, pos++)) {
            return false;
        }
        while (chr(str, pos)) {
            pos++;
        }
        this.pos = pos;
        this.cmd = str.substring(pos0, pos);

        return true;
    }

    private boolean arg(String str, int pos) {
        int pos0 = pos;
        while (chr(str, pos)) {
            pos++;
        }
        while (digit(str, pos)) {
            pos++;
        }
        if (pos0 < pos) {
            args.add(str.substring(pos0, pos));
            this.pos = pos;
        }
        return true;
    }

    private boolean args(String str, int pos) {
        while (true) {
            if (!delim(str, pos++)) {
                break;
            }
            if (!arg(str, pos++)) {
                break;
            }
            pos = this.pos;
        }
        return true;
    }
}
